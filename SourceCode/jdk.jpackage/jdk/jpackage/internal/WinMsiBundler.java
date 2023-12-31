/*
 * Copyright (c) 2012, 2022, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package jdk.jpackage.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import jdk.jpackage.internal.AppImageFile.LauncherInfo;

import static jdk.jpackage.internal.OverridableResource.createResource;
import static jdk.jpackage.internal.StandardBundlerParam.ABOUT_URL;
import static jdk.jpackage.internal.StandardBundlerParam.APP_NAME;
import static jdk.jpackage.internal.StandardBundlerParam.INSTALLER_NAME;
import static jdk.jpackage.internal.StandardBundlerParam.CONFIG_ROOT;
import static jdk.jpackage.internal.StandardBundlerParam.DESCRIPTION;
import static jdk.jpackage.internal.StandardBundlerParam.LICENSE_FILE;
import static jdk.jpackage.internal.StandardBundlerParam.RESOURCE_DIR;
import static jdk.jpackage.internal.StandardBundlerParam.TEMP_ROOT;
import static jdk.jpackage.internal.StandardBundlerParam.VENDOR;
import static jdk.jpackage.internal.StandardBundlerParam.VERSION;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * WinMsiBundler
 *
 * Produces .msi installer from application image. Uses WiX Toolkit to build
 * .msi installer.
 * <p>
 * {@link #execute} method creates a number of source files with the description
 * of installer to be processed by WiX tools. Generated source files are stored
 * in "config" subdirectory next to "app" subdirectory in the root work
 * directory. The following WiX source files are generated:
 * <ul>
 * <li>main.wxs. Main source file with the installer description
 * <li>bundle.wxf. Source file with application and Java run-time directory tree
 * description.
 * <li>ui.wxf. Source file with UI description of the installer.
 * </ul>
 *
 * <p>
 * main.wxs file is a copy of main.wxs resource from
 * jdk.jpackage.internal.resources package. It is parametrized with the
 * following WiX variables:
 * <ul>
 * <li>JpAppName. Name of the application. Set to the value of --name command
 * line option
 * <li>JpAppVersion. Version of the application. Set to the value of
 * --app-version command line option
 * <li>JpAppVendor. Vendor of the application. Set to the value of --vendor
 * command line option
 * <li>JpAppDescription. Description of the application. Set to the value of
 * --description command line option
 * <li>JpProductCode. Set to product code UUID of the application. Random value
 * generated by jpackage every time {@link #execute} method is called
 * <li>JpProductUpgradeCode. Set to upgrade code UUID of the application. Random
 * value generated by jpackage every time {@link #execute} method is called if
 * --win-upgrade-uuid command line option is not specified. Otherwise this
 * variable is set to the value of --win-upgrade-uuid command line option
 * <li>JpAllowUpgrades. Set to "yes", but all that matters is it is defined.
 * <li>JpAllowDowngrades. Defined for application installers, and undefined for
 * Java runtime installers.
 * <li>JpConfigDir. Absolute path to the directory with generated WiX source
 * files.
 * <li>JpIsSystemWide. Set to "yes" if --win-per-user-install command line
 * option was not specified. Undefined otherwise
 * <li>JpAppSizeKb. Set to estimated size of the application in kilobytes
 * <li>JpHelpURL. Set to value of --win-help-url command line option if it
 * was specified. Undefined otherwise
 * <li>JpAboutURL. Set to value of --about-url command line option if it
 * was specified. Undefined otherwise
 * <li>JpUpdateURL. Set to value of --win-update-url command line option if it
 * was specified. Undefined otherwise
 * </ul>
 *
 * <p>
 * ui.wxf file is generated based on --license-file, --win-shortcut-prompt,
 * --win-dir-chooser command line options. It is parametrized with the following
 * WiX variables:
 * <ul>
 * <li>JpLicenseRtf. Set to the value of --license-file command line option.
 * Undefined if --license-file command line option was not specified
 * </ul>
 */
public class WinMsiBundler  extends AbstractBundler {

    public static final BundlerParamInfo<Path> MSI_IMAGE_DIR =
            new StandardBundlerParam<>(
            "win.msi.imageDir",
            Path.class,
            params -> {
                Path imagesRoot = IMAGES_ROOT.fetchFrom(params);
                if (!Files.exists(imagesRoot)) {
                    try {
                        Files.createDirectories(imagesRoot);
                    } catch (IOException ioe) {
                        return null;
                    }
                }
                return imagesRoot.resolve("win-msi.image");
            },
            (s, p) -> null);

    public static final BundlerParamInfo<Path> WIN_APP_IMAGE =
            new StandardBundlerParam<>(
            "win.app.image",
            Path.class,
            null,
            (s, p) -> null);

    static final StandardBundlerParam<InstallableFile> SERVICE_INSTALLER
            = new StandardBundlerParam<>(
                    "win.msi.serviceInstaller",
                    InstallableFile.class,
                    null,
                    null
            );

    public static final StandardBundlerParam<Boolean> MSI_SYSTEM_WIDE  =
            new StandardBundlerParam<>(
                    Arguments.CLIOptions.WIN_PER_USER_INSTALLATION.getId(),
                    Boolean.class,
                    params -> true, // MSIs default to system wide
                    // valueOf(null) is false,
                    // and we actually do want null
                    (s, p) -> (s == null || "null".equalsIgnoreCase(s))? null
                            : Boolean.valueOf(s)
            );

    public static final StandardBundlerParam<String> PRODUCT_VERSION =
            new StandardBundlerParam<>(
                    "win.msi.productVersion",
                    String.class,
                    VERSION::fetchFrom,
                    (s, p) -> s
            );

    private static final BundlerParamInfo<String> HELP_URL =
            new StandardBundlerParam<>(
            Arguments.CLIOptions.WIN_HELP_URL.getId(),
            String.class,
            null,
            (s, p) -> s);

    private static final BundlerParamInfo<String> UPDATE_URL =
            new StandardBundlerParam<>(
            Arguments.CLIOptions.WIN_UPDATE_URL.getId(),
            String.class,
            null,
            (s, p) -> s);

    private static final BundlerParamInfo<String> UPGRADE_UUID =
            new StandardBundlerParam<>(
            Arguments.CLIOptions.WIN_UPGRADE_UUID.getId(),
            String.class,
            null,
            (s, p) -> s);

    private static final BundlerParamInfo<String> INSTALLER_FILE_NAME =
            new StandardBundlerParam<> (
            "win.installerName",
            String.class,
            params -> {
                String nm = INSTALLER_NAME.fetchFrom(params);
                if (nm == null) return null;

                String version = VERSION.fetchFrom(params);
                if (version == null) {
                    return nm;
                } else {
                    return nm + "-" + version;
                }
            },
            (s, p) -> s);

    public WinMsiBundler() {
        appImageBundler = new WinAppBundler().setDependentTask(true);
        wixFragments = Stream.of(
                Map.entry("bundle.wxf", new WixAppImageFragmentBuilder()),
                Map.entry("ui.wxf", new WixUiFragmentBuilder())
        ).<WixFragmentBuilder>map(e -> {
            e.getValue().setOutputFileName(e.getKey());
            return e.getValue();
        }).toList();
    }

    @Override
    public String getName() {
        return I18N.getString("msi.bundler.name");
    }

    @Override
    public String getID() {
        return "msi";
    }

    @Override
    public String getBundleType() {
        return "INSTALLER";
    }

    @Override
    public boolean supported(boolean platformInstaller) {
        try {
            if (wixToolset == null) {
                wixToolset = WixTool.toolset();
            }
            return true;
        } catch (ConfigException ce) {
            Log.error(ce.getMessage());
            if (ce.getAdvice() != null) {
                Log.error(ce.getAdvice());
            }
        } catch (Exception e) {
            Log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    private static UUID getUpgradeCode(Map<String, ? super Object> params) {
        String upgradeCode = UPGRADE_UUID.fetchFrom(params);
        if (upgradeCode != null) {
            return UUID.fromString(upgradeCode);
        }
        return createNameUUID("UpgradeCode", params, List.of(VENDOR, APP_NAME));
    }

    private static UUID getProductCode(Map<String, ? super Object> params) {
        return createNameUUID("ProductCode", params, List.of(VENDOR, APP_NAME,
                VERSION));
    }

    private static UUID createNameUUID(String prefix,
            Map<String, ? super Object> params,
            List<StandardBundlerParam<String>> components) {
        String key = Stream.concat(Stream.of(prefix), components.stream().map(
                c -> c.fetchFrom(params))).collect(Collectors.joining("/"));
        return UUID.nameUUIDFromBytes(key.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean validate(Map<String, ? super Object> params)
            throws ConfigException {
        try {
            appImageBundler.validate(params);

            if (wixToolset == null) {
                wixToolset = WixTool.toolset();
            }

            try {
                getUpgradeCode(params);
            } catch (IllegalArgumentException ex) {
                throw new ConfigException(ex);
            }

            for (var toolInfo: wixToolset.values()) {
                Log.verbose(MessageFormat.format(I18N.getString(
                        "message.tool-version"), toolInfo.path.getFileName(),
                        toolInfo.version));
            }

            wixFragments.forEach(wixFragment -> wixFragment.setWixVersion(
                    wixToolset.get(WixTool.Light).version));

            wixFragments.get(0).logWixFeatures();

            /********* validate bundle parameters *************/

            try {
                String version = PRODUCT_VERSION.fetchFrom(params);
                MsiVersion.of(version);
            } catch (IllegalArgumentException ex) {
                throw new ConfigException(ex.getMessage(), I18N.getString(
                        "error.version-string-wrong-format.advice"), ex);
            }

            FileAssociation.verify(FileAssociation.fetchFrom(params));

            var serviceInstallerResource = initServiceInstallerResource(params);
            if (serviceInstallerResource != null) {
                if (!Files.exists(serviceInstallerResource.getExternalPath())) {
                    throw new ConfigException(I18N.getString(
                            "error.missing-service-installer"), I18N.getString(
                                    "error.missing-service-installer.advice"));
                }
            }

            return true;
        } catch (RuntimeException re) {
            if (re.getCause() instanceof ConfigException) {
                throw (ConfigException) re.getCause();
            } else {
                throw new ConfigException(re);
            }
        }
    }

    private void prepareProto(Map<String, ? super Object> params)
                throws PackagerException, IOException {
        Path appImage = StandardBundlerParam.getPredefinedAppImage(params);
        String appName = APP_NAME.fetchFrom(params);
        Path appDir;
        if (appName == null) {
            // Can happen when no name is given, and using a foreign app-image
            throw new PackagerException("error.no.name");
        }

        // we either have an application image or need to build one
        if (appImage != null) {
            appDir = MSI_IMAGE_DIR.fetchFrom(params).resolve(appName);
            // copy everything from appImage dir into appDir/name
            IOUtils.copyRecursive(appImage, appDir);
        } else {
            appDir = appImageBundler.execute(params, MSI_IMAGE_DIR.fetchFrom(
                    params));
        }

        // Configure installer icon
        if (StandardBundlerParam.isRuntimeInstaller(params)) {
            // Use icon from java launcher.
            // Assume java.exe exists in Java Runtime being packed.
            // Ignore custom icon if any as we don't want to copy anything in
            // Java Runtime image.
            installerIcon = ApplicationLayout.javaRuntime()
                    .resolveAt(appDir)
                    .runtimeDirectory()
                    .resolve(Path.of("bin", "java.exe"));
        } else {
            var appLayout = ApplicationLayout.windowsAppImage().resolveAt(appDir);

            installerIcon = appLayout.launchersDirectory()
                    .resolve(appName + ".exe");

            new PackageFile(appName).save(appLayout);
        }
        installerIcon = installerIcon.toAbsolutePath();

        params.put(WIN_APP_IMAGE.getID(), appDir);

        String licenseFile = LICENSE_FILE.fetchFrom(params);
        if (licenseFile != null) {
            // need to copy license file to the working directory
            // and convert to rtf if needed
            Path lfile = Path.of(licenseFile);
            Path destFile = CONFIG_ROOT.fetchFrom(params)
                    .resolve(lfile.getFileName());

            IOUtils.copyFile(lfile, destFile);
            destFile.toFile().setWritable(true);
            ensureByMutationFileIsRTF(destFile);
        }

        var serviceInstallerResource = initServiceInstallerResource(params);
        if (serviceInstallerResource != null) {
            var serviceInstallerPath = serviceInstallerResource.getExternalPath();
            params.put(SERVICE_INSTALLER.getID(), new InstallableFile(
                    serviceInstallerPath, serviceInstallerPath.getFileName()));
        }
    }

    @Override
    public Path execute(Map<String, ? super Object> params,
            Path outputParentDir) throws PackagerException {

        IOUtils.writableOutputDir(outputParentDir);

        Path imageDir = MSI_IMAGE_DIR.fetchFrom(params);
        try {
            Files.createDirectories(imageDir);

            prepareProto(params);

            for (var wixFragment : wixFragments) {
                wixFragment.initFromParams(params);
                wixFragment.addFilesToConfigRoot();
            }

            Map<String, String> wixVars = prepareMainProjectFile(params);

            new ScriptRunner()
            .setDirectory(imageDir)
            .setResourceCategoryId("resource.post-app-image-script")
            .setScriptNameSuffix("post-image")
            .setEnvironmentVariable("JpAppImageDir", imageDir.toAbsolutePath().toString())
            .run(params);

            return buildMSI(params, wixVars, outputParentDir);
        } catch (IOException ex) {
            Log.verbose(ex);
            throw new PackagerException(ex);
        }
    }

    private long getAppImageSizeKb(Map<String, ? super Object> params) throws
            IOException {
        ApplicationLayout appLayout;
        if (StandardBundlerParam.isRuntimeInstaller(params)) {
            appLayout = ApplicationLayout.javaRuntime();
        } else {
            appLayout = ApplicationLayout.windowsAppImage();
        }
        appLayout = appLayout.resolveAt(WIN_APP_IMAGE.fetchFrom(params));

        long size = appLayout.sizeInBytes() >> 10;

        return size;
    }

    private Map<String, String> prepareMainProjectFile(
            Map<String, ? super Object> params) throws IOException {
        Map<String, String> data = new HashMap<>();

        final UUID productCode = getProductCode(params);
        final UUID upgradeCode = getUpgradeCode(params);

        data.put("JpProductCode", productCode.toString());
        data.put("JpProductUpgradeCode", upgradeCode.toString());

        Log.verbose(MessageFormat.format(I18N.getString("message.product-code"),
                productCode));
        Log.verbose(MessageFormat.format(I18N.getString("message.upgrade-code"),
                upgradeCode));

        data.put("JpAllowUpgrades", "yes");
        if (!StandardBundlerParam.isRuntimeInstaller(params)) {
            data.put("JpAllowDowngrades", "yes");
        }

        data.put("JpAppName", APP_NAME.fetchFrom(params));
        data.put("JpAppDescription", DESCRIPTION.fetchFrom(params));
        data.put("JpAppVendor", VENDOR.fetchFrom(params));
        data.put("JpAppVersion", PRODUCT_VERSION.fetchFrom(params));
        if (Files.exists(installerIcon)) {
            data.put("JpIcon", installerIcon.toString());
        }

        Optional.ofNullable(HELP_URL.fetchFrom(params)).ifPresent(value -> {
            data.put("JpHelpURL", value);
        });

        Optional.ofNullable(UPDATE_URL.fetchFrom(params)).ifPresent(value -> {
            data.put("JpUpdateURL", value);
        });

        Optional.ofNullable(ABOUT_URL.fetchFrom(params)).ifPresent(value -> {
            data.put("JpAboutURL", value);
        });

        data.put("JpAppSizeKb", Long.toString(getAppImageSizeKb(params)));

        final Path configDir = CONFIG_ROOT.fetchFrom(params);

        data.put("JpConfigDir", configDir.toAbsolutePath().toString());

        if (MSI_SYSTEM_WIDE.fetchFrom(params)) {
            data.put("JpIsSystemWide", "yes");
        }

        // Copy standard l10n files.
        for (String loc : Arrays.asList("de", "en", "ja", "zh_CN")) {
            String fname = "MsiInstallerStrings_" + loc + ".wxl";
            createResource(fname, params)
                    .setCategory(I18N.getString("resource.wxl-file"))
                    .saveToFile(configDir.resolve(fname));
        }

        createResource("main.wxs", params)
                .setCategory(I18N.getString("resource.main-wix-file"))
                .saveToFile(configDir.resolve("main.wxs"));

        createResource("overrides.wxi", params)
                .setCategory(I18N.getString("resource.overrides-wix-file"))
                .saveToFile(configDir.resolve("overrides.wxi"));

        return data;
    }

    private Path buildMSI(Map<String, ? super Object> params,
            Map<String, String> wixVars, Path outdir)
            throws IOException {

        Path msiOut = outdir.resolve(INSTALLER_FILE_NAME.fetchFrom(params) + ".msi");

        Log.verbose(MessageFormat.format(I18N.getString(
                "message.preparing-msi-config"), msiOut.toAbsolutePath()
                        .toString()));

        WixPipeline wixPipeline = new WixPipeline()
        .setToolset(wixToolset.entrySet().stream().collect(
                Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue().path)))
        .setWixObjDir(TEMP_ROOT.fetchFrom(params).resolve("wixobj"))
        .setWorkDir(WIN_APP_IMAGE.fetchFrom(params))
        .addSource(CONFIG_ROOT.fetchFrom(params).resolve("main.wxs"), wixVars);

        for (var wixFragment : wixFragments) {
            wixFragment.configureWixPipeline(wixPipeline);
        }

        Log.verbose(MessageFormat.format(I18N.getString(
                "message.generating-msi"), msiOut.toAbsolutePath().toString()));

        wixPipeline.addLightOptions("-sice:ICE27");

        if (!MSI_SYSTEM_WIDE.fetchFrom(params)) {
            wixPipeline.addLightOptions("-sice:ICE91");
        }

        // Filter out custom l10n files that were already used to
        // override primary l10n files. Ignore case filename comparison,
        // both lists are expected to be short.
        List<Path> primaryWxlFiles = getWxlFilesFromDir(params, CONFIG_ROOT);
        List<Path> customWxlFiles = getWxlFilesFromDir(params, RESOURCE_DIR).stream()
                .filter(custom -> primaryWxlFiles.stream().noneMatch(primary ->
                        primary.getFileName().toString().equalsIgnoreCase(
                                custom.getFileName().toString())))
                .peek(custom -> Log.verbose(MessageFormat.format(
                        I18N.getString("message.using-custom-resource"),
                                String.format("[%s]", I18N.getString("resource.wxl-file")),
                                custom.getFileName().toString())))
                .toList();

        // All l10n files are supplied to WiX with "-loc", but only
        // Cultures from custom files and a single primary Culture are
        // included into "-cultures" list
        for (var wxl : primaryWxlFiles) {
            wixPipeline.addLightOptions("-loc", wxl.toAbsolutePath().normalize().toString());
        }

        List<String> cultures = new ArrayList<>();
        for (var wxl : customWxlFiles) {
            wixPipeline.addLightOptions("-loc", wxl.toAbsolutePath().normalize().toString());
            cultures.add(getCultureFromWxlFile(wxl));
        }

        // Append a primary culture bases on runtime locale.
        final Path primaryWxlFile = CONFIG_ROOT.fetchFrom(params).resolve(
                I18N.getString("resource.wxl-file-name"));
        cultures.add(getCultureFromWxlFile(primaryWxlFile));

        // Build ordered list of unique cultures.
        Set<String> uniqueCultures = new LinkedHashSet<>();
        uniqueCultures.addAll(cultures);
        wixPipeline.addLightOptions(uniqueCultures.stream().collect(
                Collectors.joining(";", "-cultures:", "")));

        wixPipeline.buildMsi(msiOut.toAbsolutePath());

        return msiOut;
    }

    private static List<Path> getWxlFilesFromDir(Map<String, ? super Object> params,
            StandardBundlerParam<Path> pathParam) throws IOException {
        Path dir = pathParam.fetchFrom(params);
        if (dir == null) {
            return Collections.emptyList();
        }

        final String glob = "glob:**/*.wxl";
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(
                glob);

        try (var walk = Files.walk(dir, 1)) {
            return walk
                    .filter(Files::isReadable)
                    .filter(pathMatcher::matches)
                    .sorted((a, b) -> a.getFileName().toString().compareToIgnoreCase(b.getFileName().toString()))
                    .toList();
        }
    }

    private static String getCultureFromWxlFile(Path wxlPath) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(wxlPath.toFile());

            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList) xPath.evaluate(
                    "//WixLocalization/@Culture", doc,
                    XPathConstants.NODESET);
            if (nodes.getLength() != 1) {
                throw new IOException(MessageFormat.format(I18N.getString(
                        "error.extract-culture-from-wix-l10n-file"),
                        wxlPath.toAbsolutePath()));
            }

            return nodes.item(0).getNodeValue();
        } catch (XPathExpressionException | ParserConfigurationException
                | SAXException ex) {
            throw new IOException(MessageFormat.format(I18N.getString(
                    "error.read-wix-l10n-file"), wxlPath.toAbsolutePath()), ex);
        }
    }

    private static void ensureByMutationFileIsRTF(Path f) {
        if (f == null || !Files.isRegularFile(f)) return;

        try {
            boolean existingLicenseIsRTF = false;

            try (InputStream fin = Files.newInputStream(f)) {
                byte[] firstBits = new byte[7];

                if (fin.read(firstBits) == firstBits.length) {
                    String header = new String(firstBits);
                    existingLicenseIsRTF = "{\\rtf1\\".equals(header);
                }
            }

            if (!existingLicenseIsRTF) {
                List<String> oldLicense = Files.readAllLines(f);
                try (Writer w = Files.newBufferedWriter(
                        f, Charset.forName("Windows-1252"))) {
                    w.write("{\\rtf1\\ansi\\ansicpg1252\\deff0\\deflang1033"
                            + "{\\fonttbl{\\f0\\fnil\\fcharset0 Arial;}}\n"
                            + "\\viewkind4\\uc1\\pard\\sa200\\sl276"
                            + "\\slmult1\\lang9\\fs20 ");
                    oldLicense.forEach(l -> {
                        try {
                            for (char c : l.toCharArray()) {
                                // 0x00 <= ch < 0x20 Escaped (\'hh)
                                // 0x20 <= ch < 0x80 Raw(non - escaped) char
                                // 0x80 <= ch <= 0xFF Escaped(\ 'hh)
                                // 0x5C, 0x7B, 0x7D (special RTF characters
                                // \,{,})Escaped(\'hh)
                                // ch > 0xff Escaped (\\ud###?)
                                if (c < 0x10) {
                                    w.write("\\'0");
                                    w.write(Integer.toHexString(c));
                                } else if (c > 0xff) {
                                    w.write("\\ud");
                                    w.write(Integer.toString(c));
                                    // \\uc1 is in the header and in effect
                                    // so we trail with a replacement char if
                                    // the font lacks that character - '?'
                                    w.write("?");
                                } else if ((c < 0x20) || (c >= 0x80) ||
                                        (c == 0x5C) || (c == 0x7B) ||
                                        (c == 0x7D)) {
                                    w.write("\\'");
                                    w.write(Integer.toHexString(c));
                                } else {
                                    w.write(c);
                                }
                            }
                            // blank lines are interpreted as paragraph breaks
                            if (l.length() < 1) {
                                w.write("\\par");
                            } else {
                                w.write(" ");
                            }
                            w.write("\r\n");
                        } catch (IOException e) {
                            Log.verbose(e);
                        }
                    });
                    w.write("}\r\n");
                }
            }
        } catch (IOException e) {
            Log.verbose(e);
        }

    }

    private static OverridableResource initServiceInstallerResource(
            Map<String, ? super Object> params) {
        if (StandardBundlerParam.isRuntimeInstaller(params)) {
            // Runtime installer doesn't install launchers,
            // service installer not needed
            return null;
        }

        if (!AppImageFile.getLaunchers(
                StandardBundlerParam.getPredefinedAppImage(params), params).stream().anyMatch(
                LauncherInfo::isService)) {
            // Not a single launcher is requested to be installed as a service,
            // service installer not needed
            return null;
        }

        var result = createResource(null, params)
                .setPublicName("service-installer.exe")
                .setSourceOrder(OverridableResource.Source.External);
        if (result.getResourceDir() == null) {
            return null;
        }

        return result.setExternal(result.getResourceDir().resolve(
                result.getPublicName()));
    }

    private Path installerIcon;
    private Map<WixTool, WixTool.ToolInfo> wixToolset;
    private AppImageBundler appImageBundler;
    private final List<WixFragmentBuilder> wixFragments;
}
