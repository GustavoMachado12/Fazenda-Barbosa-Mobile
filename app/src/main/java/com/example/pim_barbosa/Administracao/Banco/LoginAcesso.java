package com.example.pim_barbosa.Administracao.Banco;

import com.example.pim_barbosa.Conexao.ConSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginAcesso {
    Connection connection;
    String ConnectionResult;

    public String[] verificaAcesso(String login, String senha) {
        int tipoUsuario = 0;
        String nomeFuncionario = null;
        String cargoFuncionario = null;

        try {
            ConSQL sql = new ConSQL();
            connection = sql.con();
            if (connection != null) {
                String qu = "EXEC pVerificaDadosLogin '" + login + "', '" + senha + "'";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(qu);

                while (rs.next()) {
                    String tipo = rs.getString("Tipo");
                    switch (tipo) {
                        case "Cliente":
                            tipoUsuario = 1;
                            break;
                        case "Funcionario":
                            tipoUsuario = 2;
                            nomeFuncionario = rs.getString("Nome").split(" ")[0];
                            cargoFuncionario = rs.getString("Cargo");
                            break;
                        case "Administrador":
                            tipoUsuario = 3;
                            break;
                    }
                }

                ConnectionResult = "Success";
                connection.close();
            } else {
                ConnectionResult = "Failed";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new String[]{String.valueOf(tipoUsuario), nomeFuncionario, cargoFuncionario};
    }
}