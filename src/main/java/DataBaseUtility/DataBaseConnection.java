package DataBaseUtility;

import java.sql.*;


public class DataBaseConnection {
    private final static String url = "jdbc:postgresql://localhost:5432/inventory_management_db";
    private final static String username = "postgres";
    private final static String password = "p";

    private static Connection connection;

    public static Connection getConnection(){
        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url,username,password);
        }catch(SQLException | ClassNotFoundException e){
            e.getStackTrace();
            System.out.println("Getting connection failed");
        }
        return connection;
    }

    private DataBaseConnection(){

    }

    public static void closeConnection() {
        try{
            if(connection!=null){
                connection.close();
            }
        }catch(SQLException e){
            e.getStackTrace();
            System.out.println("Closing the connections failed");
        }
        finally {
            try{
                if(connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Closing the connection failed");
            }
        }
    }
}
