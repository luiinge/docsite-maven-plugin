package docsite;

import java.util.function.Consumer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class Logger {

    public final Consumer<String> debug;
    public final Consumer<String> info;
    public final Consumer<String> warn;
    public final Consumer<String> error;

}
