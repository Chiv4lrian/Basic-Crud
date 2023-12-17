package com.example.crud;

import java.sql.Connection;
import java.sql.DriverManager;

@SuppressWarnings("CallToPrintStackTrace")
public class DBConnect {
    public Connection dblink;

    public Connection getConnection(){
        String databaseUser = "root";
        String databasePassword = "pass12345";
        String url = "jdbc:mysql://localhost:3306/crudy";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            dblink = DriverManager.getConnection(url,databaseUser,databasePassword);
        }catch(Exception e){
            e.printStackTrace();
        }
        return dblink;
    }


}
