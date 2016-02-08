package connection.exceptions;

public class MalformedSelectException extends Throwable {
    @Override
    public String getMessage() {
        return "ERROR: from method can't be called after select method.";
    }

    @Override
    public void printStackTrace() {
        System.out.println("ERROR: from method can't be called after select method.");
    }
}
