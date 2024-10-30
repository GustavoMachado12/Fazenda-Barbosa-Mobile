package com.example.pim_barbosa.Administracao.Banco.Relatorio;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.example.pim_barbosa.Conexao.ConSQL;
import com.example.pim_barbosa.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GeraRelatorio {
    Connection connection;
    String ConnectionResult;

    //TOTAL VENDAS
    public void totalVendas(int ano, Context context){
        try {
            ConSQL sql = new ConSQL();
            connection = sql.con();
            if (connection != null) {
                String qu = "SELECT * FROM vTotalVendas WHERE ANO = " + ano;
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(qu);

                while (rs.next()){
                    int totalVendas = rs.getInt("TOTAL");
                    TextView txtTotalVendas = ((Activity) context).findViewById(R.id.txtTotalVendas_QuantidadeTotal_Relatorio);
                    txtTotalVendas.setText(String.valueOf(totalVendas));
                    TextView txtAno = ((Activity) context).findViewById(R.id.txtTotalVendas_Ano_Relatorio);
                    txtAno.setText(String.valueOf(ano));
                }

                ConnectionResult = "Success";
                connection.close();
            } else {
                ConnectionResult = "Failed";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
