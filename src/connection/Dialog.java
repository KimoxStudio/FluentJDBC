package connection;

public class Dialog {
    protected static String exceptionMessage(){
        return String.format("ERROR STABILISING CONNECTION TO:\n" +
                        "URL:\t\t%s\n" +
                        "USERNAME:\t%s\n" +
                        "PASSWORD:\t%s",
                FluentConnection.url, FluentConnection.username, FluentConnection.password);
    }
}
