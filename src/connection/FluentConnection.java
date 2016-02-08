package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FluentConnection {

    protected static String url;
    protected static String username;
    protected static String password;
    private static Connection connector = null;
    private static FluentConnection connection = null;

    private FluentConnection() {
    }

    public static void init(String url, String dbName, String username, String password) {
        FluentConnection.url = String.format("jdbc:mysql://%s/%s", url, dbName);
        FluentConnection.username = username;
        FluentConnection.password = password;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        connector = null;
    }

    public static FluentConnection instance() throws ConnectionException {
        try {
            connector = connector == null ? DriverManager.getConnection(url, username, password) : connector;
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n" + Dialog.exceptionMessage());
            throw new ConnectionException();
        }
        return connection = (connection == null) ? new FluentConnection() : connection;
    }

    public void close() throws ConnectionException {
        try {
            connector.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ConnectionException();
        }
    }

    public boolean isClosed() throws ConnectionException {
        try {
            return connector.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ConnectionException();
        }
    }
}
