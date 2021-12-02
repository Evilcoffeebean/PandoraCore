package dev.pandora.core.api.networking.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Zvijer on 30.3.2017..
 */
public abstract class Database {

    protected Connection connection;

    public Database() {
        this.connection = null;
    }

    public abstract Connection openConnection() throws SQLException, ClassNotFoundException;

    public Connection getConnection() {
        return connection;
    }

    public boolean checkConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean closeConnection() {
        try {
            if (connection == null) return false;
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet query(String path) {
        try {
            if (!checkConnection()) openConnection();
            Statement s = getConnection().createStatement();
            return s.executeQuery(path);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int update(String path) {
        try {
            if (!checkConnection()) openConnection();
            Statement s = getConnection().createStatement();
            return s.executeUpdate(path);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
