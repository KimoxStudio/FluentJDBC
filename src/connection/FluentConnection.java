package connection;

import connection.builder.Dialog;
import connection.builder.QueryBuilder;
import connection.exceptions.ConnectionException;

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

    public static FluentConnection connect() throws ConnectionException {
        try {
            connector = connector == null ? DriverManager.getConnection(url, username, password) : connector;
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\n" + Dialog.connectionParameters());
            throw new ConnectionException();
        }
        return connection = instance();
    }

    public static FluentConnection instance() {
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

    public static QueryBuilder with(){
        return new QueryBuilder();
    }

    public String url() {
        return url;
    }
}
