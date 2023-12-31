package sun.net.httpserver.simpleserver.resources;

import java.util.ListResourceBundle;

public final class simpleserver_zh_CN extends ListResourceBundle {
    protected final Object[][] getContents() {
        return new Object[][] {
            { "err.invalid.arg", "\u4E3A {0} \u63D0\u4F9B\u7684\u503C\u65E0\u6548\uFF1A{1}" },
            { "err.missing.arg", "\u6CA1\u6709\u4E3A{0}\u6307\u5B9A\u503C" },
            { "err.server.config.failed", "\u670D\u52A1\u5668\u914D\u7F6E\u5931\u8D25\uFF1A{0}" },
            { "err.server.handle.failed", "\u670D\u52A1\u5668\u4EA4\u6362\u5904\u7406\u5931\u8D25\uFF1A{0}" },
            { "err.unknown.option", "\u672A\u77E5\u9009\u9879: {0}" },
            { "error.prefix", "\u9519\u8BEF:" },
            { "html.dir.list", "\u4EE5\u4E0B\u9879\u7684\u76EE\u5F55\u5217\u8868" },
            { "html.not.found", "\u627E\u4E0D\u5230\u6587\u4EF6" },
            { "loopback.info", "\u9ED8\u8BA4\u60C5\u51B5\u4E0B\u7ED1\u5B9A\u5230\u73AF\u56DE\u3002\u5982\u679C\u8981\u8868\u793A\u6240\u6709\u63A5\u53E3\uFF0C\u8BF7\u4F7F\u7528 \"-b 0.0.0.0\" \u6216 \"-b ::\"\u3002" },
            { "msg.start.anylocal", "\u4E3A 0.0.0.0\uFF08\u6240\u6709\u63A5\u53E3\uFF09\u7AEF\u53E3 {2} \u4E0A\u7684 {0} \u53CA\u5B50\u76EE\u5F55\u63D0\u4F9B\u670D\u52A1\nURL http://{1}:{2}/" },
            { "msg.start.other", "\u4E3A {1} \u7AEF\u53E3 {2} \u4E0A\u7684 {0} \u53CA\u5B50\u76EE\u5F55\u63D0\u4F9B\u670D\u52A1\nURL http://{1}:{2}/" },
            { "opt.bindaddress", "-b, --bind-address    - \u8981\u7ED1\u5B9A\u5230\u7684\u5730\u5740\u3002\u9ED8\u8BA4\u503C\uFF1A{0}\uFF08\u73AF\u56DE\uFF09\u3002\n                        \u5982\u679C\u8981\u8868\u793A\u6240\u6709\u63A5\u53E3\uFF0C\u8BF7\u4F7F\u7528 \"-b 0.0.0.0\" \u6216 \"-b ::\"\u3002" },
            { "opt.directory", "-d, --directory       - \u8981\u4E3A\u5176\u63D0\u4F9B\u670D\u52A1\u7684\u76EE\u5F55\u3002\u9ED8\u8BA4\u503C\uFF1A\u5F53\u524D\u76EE\u5F55\u3002" },
            { "opt.output", "-o, --output          - \u8F93\u51FA\u683C\u5F0F\u3002none|info|verbose\u3002\u9ED8\u8BA4\u503C\uFF1Ainfo\u3002" },
            { "opt.port", "-p, --port            - \u8981\u76D1\u542C\u7684\u7AEF\u53E3\u3002\u9ED8\u8BA4\u503C\uFF1A8000\u3002" },
            { "options", "\u9009\u9879\uFF1A\n-b, --bind-address    - \u8981\u7ED1\u5B9A\u5230\u7684\u5730\u5740\u3002\u9ED8\u8BA4\u503C\uFF1A{0}\uFF08\u73AF\u56DE\uFF09\u3002\n                        \u5982\u679C\u8981\u8868\u793A\u6240\u6709\u63A5\u53E3\uFF0C\u8BF7\u4F7F\u7528 \"-b 0.0.0.0\" \u6216 \"-b ::\"\u3002\n-d, --directory       - \u8981\u4E3A\u5176\u63D0\u4F9B\u670D\u52A1\u7684\u76EE\u5F55\u3002\u9ED8\u8BA4\u503C\uFF1A\u5F53\u524D\u76EE\u5F55\u3002\n-o, --output          - \u8F93\u51FA\u683C\u5F0F\u3002none|info|verbose\u3002\u9ED8\u8BA4\u503C\uFF1Ainfo\u3002\n-p, --port            - \u8981\u76D1\u542C\u7684\u7AEF\u53E3\u3002\u9ED8\u8BA4\u503C\uFF1A8000\u3002\n-h, -?, --help        - \u8F93\u51FA\u6B64\u5E2E\u52A9\u6D88\u606F\u5E76\u9000\u51FA\u3002\n-version, --version   - \u8F93\u51FA\u7248\u672C\u4FE1\u606F\u5E76\u9000\u51FA\u3002\n\u8981\u505C\u6B62\u670D\u52A1\u5668\uFF0C\u8BF7\u6309 Ctrl + C\u3002" },
            { "usage.java", "\u7528\u6CD5\uFF1Ajava -m jdk.httpserver [-b \u7ED1\u5B9A\u5730\u5740] [-p \u7AEF\u53E3] [-d \u76EE\u5F55]\n                              [-o none|info|verbose] [-h \u663E\u793A\u9009\u9879]\n                              [-version \u663E\u793A\u7248\u672C\u4FE1\u606F]" },
            { "usage.jwebserver", "\u7528\u6CD5\uFF1Ajwebserver [-b \u7ED1\u5B9A\u5730\u5740] [-p \u7AEF\u53E3] [-d \u76EE\u5F55]\n                              [-o none|info|verbose] [-h \u663E\u793A\u9009\u9879]\n                              [-version \u663E\u793A\u7248\u672C\u4FE1\u606F]" },
            { "version", "{0} {1}" },
        };
    }
}
