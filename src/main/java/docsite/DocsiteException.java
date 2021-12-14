package docsite;

public class DocsiteException extends RuntimeException {

    public DocsiteException(Throwable throwable) {
        super(throwable);
    }

    public DocsiteException(String message) {
        super(message);
    }

}
