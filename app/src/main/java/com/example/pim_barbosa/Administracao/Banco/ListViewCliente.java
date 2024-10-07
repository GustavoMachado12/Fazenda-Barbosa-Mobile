package com.example.pim_barbosa.Administracao.Banco;

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

public class ListViewCliente {
    Connection connection;
    String ConnectionResult = "";

    public List<Map<String, String>> getList()
    {
        List<Map<String, String>> data = null;
        data = new ArrayList<Map<String, String>>();
        try {
            ConSQL sql = new ConSQL();
            connection = sql.con();
            if(connection != null){
                String qu = "SELECT * FROM vw_Cliente ORDER BY CodigoCliente ASC";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(qu);

                while (rs.next())
                {
                    Map<String, String > dtname = new HashMap<String, String>();
                    dtname.put("Cliente_ID", rs.getString(1));
                    dtname.put("Cliente_Nome", rs.getString(2));
                    dtname.put("Cliente_CPF_CNPJ", rs.getString(7));
                    dtname.put("Cliente_Email", rs.getString(3));
                    dtname.put("Cliente_Telefone", rs.getString(5));
                    dtname.put("Cliente_Endereco", rs.getString(4));

                    data.add(dtname);
                }

                data.sort(Comparator.comparing(m -> m.get("Cliente_ID")));

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
