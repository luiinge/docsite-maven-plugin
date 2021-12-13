package docsite;

import java.util.function.Consumer;

public class Logger {

    private final Consumer<String> debug;
    private final Consumer<String> info;
    private final Consumer<String> warn;
    private final Consumer<String> error;


    public Logger(Consumer<String> debug, Consumer<String> info, Consumer<String> warn, Consumer<String> error) {
        this.debug = debug;
        this.info = info;
        this.warn = warn;
        this.error = error;
    }


    void debug(String message, Object...args) {
        debug.accept(format(message,args));
    }

    void info(String message, Object...args) {
        info.accept(format(message,args));
    }

    void warn(String message, Object...args) {
        warn.accept(format(message,args));
    }

    void error(String message, Object...args) {
        error.accept(format(message,args));
    }

    private static String format (String message, Object[] args) {
        return String.format(message.replace("{}","%s"),args);
    }

}
