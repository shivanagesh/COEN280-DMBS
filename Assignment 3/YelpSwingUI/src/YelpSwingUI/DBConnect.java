/**
 * @author Shivanagesh Chandra Mar 3, 2016 1:45:38 AM
 * fileDBConnect.java
 * 
 */
package YelpSwingUI;

import java.sql.*;

/**
 * @author Shivanagesh Chandra Mar 3, 2016 1:45:38 AM
 * fileDBConnect.java
 */
public class DBConnect
{

    //Declare variables for driver,username,password and connection url
    private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DB_CONNECTION = "jdbc:oracle:thin:@localhost:1521/XE";
    private static final String DB_USER = "COEN280A3";
    private static final String DB_PASSWORD = "s";
    public Connection conn;
    public Statement stmt;
    public ResultSet rslt;

    //Locate the JDBC driver and create database connection with given credentials
    public static Connection getDBConnection()
    {

        Connection dbConnection = null;

        //Getting oracle JDBC driver
        try {

            Class.forName(DB_DRIVER);

        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());

        }

        //Creating connection and return the connection caller function
        try {

            dbConnection = DriverManager.getConnection(
                    DB_CONNECTION, DB_USER, DB_PASSWORD);
//            Statement stmt = dbConnection.createStatement();
//
//            //Execute the sql
//            stmt.execute("set define off'");
//            stmt.close();
//            return dbConnection;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return dbConnection;

    } // End of Connection Method

}// End of DBConnect Class

