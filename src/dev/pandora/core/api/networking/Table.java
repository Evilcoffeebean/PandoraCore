package dev.pandora.core.api.networking;

import dev.pandora.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;

/**
 * Created by Synch on 2017-03-13.
 */
public class Table {


    //scarger
    private static Connection connection;
    private final String HOST = "149.56.243.156";
    private final String USER = "fbd2918c_RfrHz15";
    private final int DEFAULT_PORT = 3306;
    private String DB_PATH = "s15_core";
    private final String PASSWORD = "1jUjfKnRddQEqvaS";
    private final String PREFIX = ChatColor.GOLD+"Pandora>";
    private String table;


    public Table(String table){
        this.table = table;
        try {
            openConnection();
            buildTable();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Table(String table,String DB_PATH){
        this.table = table;
        this.DB_PATH = DB_PATH;
        try {
            openConnection();
            buildTable();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return connection;
    }

    //Has to be called in the constructor, or place of initialization
    public synchronized void openConnection() throws SQLException, ClassNotFoundException {
        //Vengenacey's comment - you weren't specifying the default sql driver, it threw an NPE
        String path = "jdbc:mysql://" + HOST + ":" + DEFAULT_PORT + "/" + DB_PATH;
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(path, USER, PASSWORD);
    }
    public synchronized void closeConnection() throws SQLException{
        connection.close();
    }

    public synchronized void buildTable() throws SQLException {
            Statement statement = connection.createStatement();
            statement.executeUpdate(String.format("CREATE TABLE IF NOT EXISTS %s (id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY (id));",table));
    }

    //scarger
    public synchronized ResultSet getQuery(String query) throws SQLException{

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        //statement.close();

        return resultSet;

    }
    /*
    public synchronized boolean isThere(Player player) throws SQLException,ClassNotFoundException{
        String statement = String.format("SELECT * FROM %s WHERE UUID='"+"%s"+"'",table,player.getUniqueId().toString()); //Not sure if `` will affect formatting.
        ResultSet result = getQuery(statement);
        boolean there = result.next();
        result.close();
        return there;
    }

    public synchronized void addPlayer(Player player) throws SQLException{

        Statement statement = connection.createStatement();
        statement.executeUpdate(String.format("INSERT INTO %s (UUID) values('"+"%s"+"')",table,player.getUniqueId().toString()));

    }
    */

    public synchronized void addSection(String section, SQLTYPE data_type) throws SQLException{
        String type = getRealValue(data_type);
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getColumns(null, null, table, section);
        if (resultSet.next()) {
            Bukkit.getServer().getConsoleSender().sendMessage(PREFIX+ ChatColor.GREEN+" SQL table "+ChatColor.AQUA+table+ChatColor.GREEN+" column "+ChatColor.AQUA+section+ChatColor.GREEN+ " is already setup :)");
        }
        else {
            Bukkit.getServer().getConsoleSender().sendMessage(PREFIX+ChatColor.YELLOW+" SQL table "+ChatColor.AQUA+table+ChatColor.GREEN+" column "+ChatColor.AQUA+section+ChatColor.GREEN+" has not been setup!");
            Bukkit.getServer().getConsoleSender().sendMessage(PREFIX+ChatColor.YELLOW+" creating...");
            Statement statement = connection.createStatement();
            statement.executeUpdate(String.format("ALTER TABLE %s ADD COLUMN %s %s", table, section, type));
            Bukkit.getServer().getConsoleSender().sendMessage(PREFIX+ChatColor.GREEN+" SQL table "+ChatColor.AQUA+table+ChatColor.GREEN+" column "+ChatColor.AQUA+section+ChatColor.GREEN+" has been setup!");
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD+"**********************************************************");

        }


    }

    public synchronized void removeSection(String section) throws SQLException{
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getColumns(null, null, table, section);
        if (resultSet.next()) {
            Bukkit.getServer().getConsoleSender().sendMessage(PREFIX+ChatColor.YELLOW+" SQL table "+ChatColor.AQUA+table+ChatColor.GREEN+" column "+ChatColor.AQUA+section+ChatColor.GREEN+" has not been deleted!");
            Bukkit.getServer().getConsoleSender().sendMessage(PREFIX+ChatColor.YELLOW+" deleting...");
            Statement statement = connection.createStatement();
            statement.executeUpdate(String.format("ALTER TABLE %s DROP COLUMN %s", table, section));
            Bukkit.getServer().getConsoleSender().sendMessage(PREFIX+ChatColor.GREEN+" SQL table "+ChatColor.AQUA+table+ChatColor.GREEN+" column "+ChatColor.AQUA+section+ChatColor.GREEN+" has been deleted!");
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD+"**********************************************************");
        }
        else {
            Bukkit.getServer().getConsoleSender().sendMessage(PREFIX+ChatColor.GREEN+" SQL table "+ChatColor.AQUA+table+ChatColor.GREEN+" column "+ChatColor.AQUA+section+ChatColor.GREEN+ " is already deleted :)");

        }
    }


    /*
    @Deprecated
    public synchronized void setValue(Player player, String section, int value) throws SQLException{
        Statement statement = connection.createStatement();
        statement.executeUpdate(String.format("UPDATE %s SET %s=%d WHERE UUID='"+"%s"+"'",table,section,value,player.getUniqueId().toString()));
    }

    public synchronized void setValue(Player player, String section, String value) throws SQLException{
        Statement statement = connection.createStatement();
        statement.executeUpdate(String.format("UPDATE %s SET %s='"+"%s"+"' WHERE UUID='"+"%s"+"'",table,section,value,player.getUniqueId().toString()));
    }
    */
    public String getTable(){
        return table;
    }

    private String getRealValue(SQLTYPE type){
        String datatype;
        switch (type){
            case FLOAT:
                datatype = "FLOAT";
                break;
            case INT:
                datatype = "INT";
                break;
            case TEXT:
                datatype = "TEXT";
                break;
            case VARCHAR:
                datatype = "VARCHAR";
                break;
            case BOOLEAN:
                datatype = "BOOLEAN";
                break;
            default:
                datatype = "VARCHAR";
                break;
        }
        return datatype;
    }
}
