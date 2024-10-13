package com.example.pim_barbosa.Administracao.Banco;

import com.example.pim_barbosa.Administracao.Fornecedor.DtoFornecedor;
import com.example.pim_barbosa.Conexao.ConSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewFornecedor {

    Connection connection;
    String ConnectionResult = "";

    DtoFornecedor dtoForn = new DtoFornecedor();

    public List<Map<String, String>> getList()
    {
        List<Map<String, String>> data = null;
        data = new ArrayList<Map<String, String>>();
        try {
            ConSQL sql = new ConSQL();
            connection = sql.con();
            if(connection != null){
                String qu = "EXEC pSelectFornecedorDescripto";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(qu);

                while (rs.next())
                {
                    Map<String, String > dtname = new HashMap<String, String>();
                    dtname.put("Cddigo", rs.getString(1));
                    dtname.put("Nome", rs.getString(2));
                    dtname.put("CNPJ", rs.getString(3));
                    dtname.put("Email", rs.getString(4));
                    dtname.put("Endereco", rs.getString(5));
                    dtname.put("Telefone", rs.getString(6));
                    data.add(dtname);
                }

                ConnectionResult = "Sucess";
                connection.close();
            } else {
                ConnectionResult = "Failed";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return data;
    }

}
