package capstone.server.commons;

import java.util.function.Function;

public class Utils {
    public static <T, R> Function<T, R> wrap(ExceptionFuntion<T,R>f) {
        return (T r) -> {
            try {
                return f.apply(r);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
