package capstone.server.commons;


@FunctionalInterface
public interface ExceptionFuntion<T, R> {
    R apply(T r) throws Exception;
}
