/*
 * Copyright (c) 2016, 2022, Oracle and/or its affiliates. All rights reserved.
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
package jdk.jshell.execution;

import java.lang.Character.UnicodeBlock;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.IntStream;
import jdk.jshell.spi.ExecutionControl;
import jdk.jshell.spi.SPIResolutionException;

/**
 * An {@link ExecutionControl} implementation that runs in the current process.
 * May be used directly, or over a channel with
 * {@link Util#forwardExecutionControl(ExecutionControl, java.io.ObjectInput, java.io.ObjectOutput) }.
 *
 * @author Robert Field
 * @author Jan Lahoda
 * @since 9
 */
public class DirectExecutionControl implements ExecutionControl {

    private static final String[] charRep;

    static {
        charRep = new String[256];
        for (int i = 0; i < charRep.length; ++i) {
            charRep[i] = Character.isISOControl(i)
                    ? String.format("\\%03o", i)
                    : "" + (char) i;
        }
        charRep['\b'] = "\\b";
        charRep['\t'] = "\\t";
        charRep['\n'] = "\\n";
        charRep['\f'] = "\\f";
        charRep['\r'] = "\\r";
        charRep['\\'] = "\\\\";
    }

    private final LoaderDelegate loaderDelegate;

    /**
     * Creates an instance, delegating loader operations to the specified
     * delegate.
     *
     * @param loaderDelegate the delegate to handle loading classes
     */
    public DirectExecutionControl(LoaderDelegate loaderDelegate) {
        this.loaderDelegate = loaderDelegate;
    }

    /**
     * Create an instance using the default class loading.
     */
    public DirectExecutionControl() {
        this(new DefaultLoaderDelegate());
    }

    @Override
    public void load(ClassBytecodes[] cbcs)
            throws ClassInstallException, NotImplementedException, EngineTerminationException {
        loaderDelegate.load(cbcs);
    }

    @Override
    public void redefine(ClassBytecodes[] cbcs)
            throws ClassInstallException, NotImplementedException, EngineTerminationException {
        throw new NotImplementedException("redefine not supported");
    }

    /**Notify that classes have been redefined.
     *
     * @param cbcs the class name and bytecodes to redefine
     * @throws NotImplementedException if not implemented
     * @throws EngineTerminationException the execution engine has terminated
     */
    protected void classesRedefined(ClassBytecodes[] cbcs)
            throws NotImplementedException, EngineTerminationException {
        loaderDelegate.classesRedefined(cbcs);
    }

    /**
     * @throws ExecutionControl.UserException {@inheritDoc}
     * @throws ExecutionControl.ResolutionException {@inheritDoc}
     * @throws ExecutionControl.StoppedException {@inheritDoc}
     */
    @Override
    public String invoke(String className, String methodName)
            throws RunException, InternalException, EngineTerminationException {
        Method doitMethod;
        try {
            Class<?> klass = findClass(className);
            doitMethod = klass.getDeclaredMethod(methodName, new Class<?>[0]);
            doitMethod.setAccessible(true);
        } catch (Throwable ex) {
            throw new InternalException(ex.toString());
        }

        try {
            clientCodeEnter();
            String result = invoke(doitMethod);
            System.out.flush();
            return result;
        } catch (RunException | InternalException | EngineTerminationException ex) {
            throw ex;
        } catch (SPIResolutionException ex) {
            return throwConvertedInvocationException(ex);
        } catch (InvocationTargetException ex) {
            return throwConvertedInvocationException(ex.getCause());
        } catch (Throwable ex) {
            return throwConvertedOtherException(ex);
        } finally {
            clientCodeLeave();
        }
    }

    /**
     * @throws ExecutionControl.UserException {@inheritDoc}
     * @throws ExecutionControl.ResolutionException {@inheritDoc}
     * @throws ExecutionControl.StoppedException {@inheritDoc}
     */
    @Override
    public String varValue(String className, String varName)
            throws RunException, EngineTerminationException, InternalException {
        Object val;
        try {
            Class<?> klass = findClass(className);
            Field var = klass.getDeclaredField(varName);
            var.setAccessible(true);
            val = var.get(null);
        } catch (Throwable ex) {
            throw new InternalException(ex.toString());
        }

        try {
            clientCodeEnter();
            return valueString(val);
        } catch (Throwable ex) {
            return throwConvertedInvocationException(ex);
        } finally {
            clientCodeLeave();
        }
    }

    @Override
    public void addToClasspath(String cp)
            throws EngineTerminationException, InternalException {
        loaderDelegate.addToClasspath(cp);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Not supported.
     */
    @Override
    public void stop()
            throws EngineTerminationException, InternalException {
        throw new NotImplementedException("stop: Not supported.");
    }

    /**
     * @throws ExecutionControl.UserException {@inheritDoc}
     * @throws ExecutionControl.ResolutionException {@inheritDoc}
     * @throws ExecutionControl.StoppedException {@inheritDoc}
     * @throws ExecutionControl.EngineTerminationException {@inheritDoc}
     * @throws ExecutionControl.NotImplementedException {@inheritDoc}
     */
    @Override
    public Object extensionCommand(String command, Object arg)
            throws RunException, EngineTerminationException, InternalException {
        throw new NotImplementedException("Unknown command: " + command);
    }

    @Override
    public void close() {
    }

    /**
     * Finds the class with the specified binary name.
     *
     * @param name the binary name of the class
     * @return the Class Object
     * @throws ClassNotFoundException if the class could not be found
     */
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return loaderDelegate.findClass(name);
    }

    /**
     * Invoke the specified "doit-method", a static method with no parameters.
     * The {@link DirectExecutionControl#invoke(java.lang.String, java.lang.String) }
     * in this class will call this to invoke.
     *
     * @param doitMethod the Method to invoke
     * @return the value or null
     * @throws Exception any exceptions thrown by
     * {@link java.lang.reflect.Method#invoke(Object, Object...) }
     * or any {@link ExecutionControl.ExecutionControlException}
     * to pass-through.
     */
    protected String invoke(Method doitMethod) throws Exception {
        Object res = doitMethod.invoke(null, new Object[0]);
        return valueString(res);
    }

    /**
     * Converts the {@code Object} value from
     * {@link ExecutionControl#invoke(String, String)  } or
     * {@link ExecutionControl#varValue(String, String)   } to {@code String}.
     *
     * @param value the value to convert
     * @return the {@code String} representation
     */
    protected static String valueString(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            StringBuilder result = new StringBuilder();
            result.append("\"");
            var cpIt = ((String) value).codePoints().iterator();
            int idx = 0;
            while (cpIt.hasNext()) {
                int cp = cpIt.nextInt();
                if (cp == '"') {
                    result.append("\\\"");
                } else {
                    appendEscapedChar(idx, cp, result);
                }
                idx++;
            }
            result.append("\"");
            return result.toString();
        } else if (value instanceof Character) {
            char cp = (char) (Character) value;
            StringBuilder result = new StringBuilder();
            result.append("'");
            if (cp == '\'') {
                result.append("\\\'");
            } else {
                appendEscapedChar(0, cp, result);
            }
            result.append("'");
            return result.toString();
        } else if (value.getClass().isArray()) {
            int dims = 0;
            Class<?> t = value.getClass();
            while (true) {
                Class<?> ct = t.getComponentType();
                if (ct == null) {
                    break;
                }
                ++dims;
                t = ct;
            }
            String tn = t.getTypeName();
            int len = Array.getLength(value);
            StringBuilder sb = new StringBuilder();
            sb.append(tn.substring(tn.lastIndexOf('.') + 1, tn.length()));
            sb.append("[");
            sb.append(len);
            sb.append("]");
            for (int i = 1; i < dims; ++i) {
                sb.append("[]");
            }
            sb.append(" { ");
            for (int i = 0; i < len; ++i) {
                sb.append(valueString(Array.get(value, i)));
                if (i < len - 1) {
                    sb.append(", ");
                }
            }
            sb.append(" }");
            return sb.toString();
        } else {
            return value.toString();
        }
    }

    private static void appendEscapedChar(int idx, int cp, StringBuilder target) {
        if (cp < 256) {
            target.append(charRep[cp]);
        } else if (needsEscape(idx, cp)) {
            target.append(String.format("\\u%04X", cp));
        } else {
            target.appendCodePoint(cp);
        }
    }

    private static boolean needsEscape(int idx, int cp) {
        UnicodeBlock block = UnicodeBlock.of(cp);
        if (block == UnicodeBlock.COMBINING_DIACRITICAL_MARKS ||
            block == UnicodeBlock.COMBINING_DIACRITICAL_MARKS_EXTENDED ||
            block == UnicodeBlock.COMBINING_DIACRITICAL_MARKS_SUPPLEMENT) {
            //escape leading combining diacritical marks,
            //as those might be confusingly merged into the leading quotes:
            return idx == 0;
        } else {
            return false;
        }
    }

    /**
     * Converts incoming exceptions in user code into instances of subtypes of
     * {@link ExecutionControl.ExecutionControlException} and throws the
     * converted exception.
     *
     * @param cause the exception to convert
     * @return never returns as it always throws
     * @throws ExecutionControl.RunException for normal exception occurrences
     * @throws ExecutionControl.InternalException for internal problems
     */
    protected String throwConvertedInvocationException(Throwable cause) throws RunException, InternalException {
        throw asRunException(cause);
    }

    private RunException asRunException(Throwable ex) {
        if (ex instanceof SPIResolutionException) {
            SPIResolutionException spire = (SPIResolutionException) ex;
            return new ResolutionException(spire.id(), spire.getStackTrace());
        } else {
            UserException ue = new UserException(ex.getMessage(),
                    ex.getClass().getName(),
                    ex.getStackTrace());
            Throwable cause = ex.getCause();
            ue.initCause(cause == null ? null : asRunException(cause));
            return ue;
        }
    }

    /**
     * Converts incoming exceptions in agent code into instances of subtypes of
     * {@link ExecutionControl.ExecutionControlException} and throws the
     * converted exception.
     *
     * @param ex the exception to convert
     * @return never returns as it always throws
     * @throws ExecutionControl.RunException for normal exception occurrences
     * @throws ExecutionControl.InternalException for internal problems
     */
    protected String throwConvertedOtherException(Throwable ex) throws RunException, InternalException {
        throw new InternalException(ex.toString());
    }

    /**
     * Marks entry into user code.
     *
     * @throws ExecutionControl.InternalException in unexpected failure cases
     */
    protected void clientCodeEnter() throws InternalException {
    }

    /**
     * Marks departure from user code.
     *
     * @throws ExecutionControl.InternalException in unexpected failure cases
     */
    protected void clientCodeLeave() throws InternalException {
    }

}
