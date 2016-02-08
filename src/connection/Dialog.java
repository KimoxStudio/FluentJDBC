package connection;

public class Dialog {
    public static String connectionParameters(){
        return String.format("ERROR STABILISING CONNECTION TO:\n" +
                        "URL:\t\t%s\n" +
                        "CHECK USERNAME AND PASSWORD.",
                FluentConnection.url);
    }
}
