package capstone.server.commons;


@FunctionalInterface
public interface ExceptionFunction<T, R> {
    R apply(T r) throws Exception;
}
