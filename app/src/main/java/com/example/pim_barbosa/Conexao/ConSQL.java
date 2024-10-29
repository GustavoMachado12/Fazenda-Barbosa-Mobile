package com.example.pim_barbosa.Conexao;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConSQL {

    String classe = "net.sourceforge.jtds.jdbc.Driver";
    protected static String ip = "192.168.0.157";
    protected static String port = "1433";
    protected static String db = "db_FazendaPIM";
    protected static String un = "sa";
    protected static String password = "root";

    public Connection con(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;
        try {
            Class.forName(classe);
            String conUrl = "jdbc:jtds:sqlserver://"+ ip + ";"
                    + "databaseName=" +  db + ";"
                    + "user=" + un + ";"
                    + "password=" + password + ";";

            con = DriverManager.getConnection(conUrl);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        return con;
    }
}