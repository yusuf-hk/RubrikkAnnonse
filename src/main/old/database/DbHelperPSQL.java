package org.yusuf.javabrain.database;

import java.sql.*;

public class DbHelperPSQL
{
    public static Connection con = null;

    public void queryDatabase()
    {
        try
        {
            Class.forName("org.postgresql.Driver");
            // database information
            String dbtype = "jdbc:postgresql:";
            String dbUrl = "//localhost:";
            String dbPort = "5432/";
            String dbName = "postgres";
            String dbUser = "postgres";
            String dbPass = "password";
            // Establishing connection
            con = DriverManager.getConnection(dbtype+dbUrl+dbPort+dbName,dbUser,dbPass);

            //Statement stmt = con.createStatement();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM message ORDER BY id");
            ps.execute();
            //stmt.executeUpdate("SELECT * FROM message ORDER BY id;");
            ps.close();
            con.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println(con);
        System.out.println("Table created successfully.");
    }
}
