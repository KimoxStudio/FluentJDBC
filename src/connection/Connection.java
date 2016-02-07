package connection;

import java.sql.DriverManager;

public class Connection {

    private final String url;
    private final String username;
    private final String password;
    private java.sql.Connection connection = null;

    public Connection(String url, String dbName, String username, String password) {
        this.url = String.format("jdbc:mysql://%s/%s", url, dbName);
        this.username = username;
        this.password = password;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public boolean connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
            System.out.println(String.format("ERROR STABILISING CONNECTION TO:\n" +
                    "URL:\t\t%s\n" +
                    "USERNAME:\t%s\n" +
                    "PASSWORD:\t%s",
                    url, username, password));
            return false;
        }
        return true;
    }
}
