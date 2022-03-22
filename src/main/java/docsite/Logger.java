package docsite;

import java.util.Objects;
import java.util.function.Consumer;

public class Logger {

    private static Logger instance;

    public static Logger instance() {
        return Objects.requireNonNull(instance);
    }

    public static void initialize(Logger logger) {
        instance = Objects.requireNonNull(logger);
    }


    private final Consumer<Throwable> debugStackMessager;
    private final Consumer<String> debugMessager;
    private final Consumer<String> infoMessager;
    private final Consumer<String> warnMessager;
    private final Consumer<String> errorMessager;


    public Logger(
        Consumer<Throwable> debugStackMessager,
        Consumer<String> debugMessager,
        Consumer<String> infoMessager,
        Consumer<String> warnMessager,
        Consumer<String> errorMessager
    ) {
        this.debugStackMessager = debugStackMessager;
        this.debugMessager = debugMessager;
        this.infoMessager = infoMessager;
        this.warnMessager = warnMessager;
        this.errorMessager = errorMessager;
    }


    public void debug(String message, Object...args) {
        debugMessager.accept(format(message,args));
    }

    public void debug(Throwable e) {
        debugStackMessager.accept(e);
    }

    public void info(String message, Object...args) {
        infoMessager.accept(format(message,args));
    }

    public void warn(String message, Object... args) {
        warnMessager.accept(format(message,args));
    }

    public void error(String message, Object...args) {
        errorMessager.accept(format(message,args));
    }

    private static String format (String message, Object[] args) {
        return String.format(message.replace("{}","%s"),args);
    }

}