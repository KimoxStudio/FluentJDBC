package connection.exceptions;

import connection.builder.Dialog;

public class ConnectionException extends Throwable {
    @Override
    public String getMessage() {
        return super.getMessage() + "\n" + Dialog.connectionParameters();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
