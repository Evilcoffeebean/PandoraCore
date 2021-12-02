package dev.pandora.core.api.networking.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Zvijer on 30.3.2017..
 */
public class MySQL extends Database {

    private final String host, username, password, database;
    private final int port;

    public MySQL(int port, String password, String database) {
        this(null, port, null, password, database);
    }

    public MySQL(String host, int port, String username, String password, String database) {
        this.host = host != null ? host : "localhost";
        this.port = port;
        this.username = username != null ? username : "root";
        this.password = password != null ? password : "12345";
        this.database = database;
    }

    @Override
    public Connection openConnection() {
        try {
            if (checkConnection()) return getConnection();
            String url = "jdbc:mysql://" + host + ":" + port;
            if (database != null)
                url = url + "/" + database;
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
                return connection;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
