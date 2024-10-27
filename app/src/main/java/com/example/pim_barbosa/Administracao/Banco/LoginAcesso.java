package com.example.pim_barbosa.Administracao.Banco;

import com.example.pim_barbosa.Conexao.ConSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class LoginAcesso {
    Connection connection;
    String ConnectionResult;

    public int verificaAcesso(String login, String senha){
        int tipoUsuario = 0;

        try {
            ConSQL sql = new ConSQL();
            connection = sql.con();
            if(connection != null){
                String qu = "EXEC pVerificaLogin '" + login + "', '" + senha + "'";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(qu);

                while (rs.next())
                {
                    tipoUsuario = rs.getInt("TIPO");
                }

                ConnectionResult = "Sucess";
                connection.close();
            } else {
                ConnectionResult = "Failed";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return tipoUsuario;
    }
}


