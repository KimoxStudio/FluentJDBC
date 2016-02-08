package connection;

public class ConnectionException extends Throwable {
    @Override
    public String getMessage() {
        return super.getMessage() + "\n" + Dialog.exceptionMessage();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
