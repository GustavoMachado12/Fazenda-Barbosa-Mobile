package com.example.pim_barbosa.Administracao.Venda;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pim_barbosa.Administracao.Producao.ProducaoConsulta;
import com.example.pim_barbosa.Conexao.ConSQL;
import com.example.pim_barbosa.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VendaAltera extends AppCompatActivity {
    EditText txtCliente, txtProduto, txtQuantidade, txtData;
    TextView textNome, textCargo;
    Button btnAlterar;
    int id;
    int idCliente, idProduto, numQuantidade;
    String nmProduto;

    //BANCO
    Connection connection;
    ConSQL bd = new ConSQL();

    SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_alterar);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        textNome = findViewById(R.id.textNomeDash);
        textCargo = findViewById(R.id.textCargoDash);

        txtCliente = findViewById(R.id.produto_alterar_cliente);
        txtProduto = findViewById(R.id.produto_alterar_produto);
        txtQuantidade = findViewById(R.id.produto_alterar_quantidade);
        txtData = findViewById(R.id.produto_alterar_data_venda);
        btnAlterar = findViewById(R.id.btn_produto_alterar);

        textNome.setText(getIntent().getStringExtra("NomeDash"));
        textCargo.setText(getIntent().getStringExtra("CargoDash"));

        try {

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                id = bundle.getInt("ID");
                idCliente = bundle.getInt("IdCliente");
                txtCliente.setText(bundle.getString("Cliente"));
                txtProduto.setText(bundle.getString("Produto"));
                numQuantidade = bundle.getInt("Quantidade");
                txtQuantidade.setText(String.valueOf(numQuantidade));
                String dt = bundle.getString("Data");
                Date date = sqlDateFormat.parse(dt);
                txtData.setText(sqlDateFormat.format(date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (txtCliente.getText().toString().isEmpty()) {
                        txtCliente.setError("Campo obrigatório");
                    } else if (txtProduto.getText().toString().isEmpty()) {
                        txtProduto.setError("Campo obrigatório");
                    } else if (txtQuantidade.getText().toString().isEmpty()) {
                        txtQuantidade.setError("Campo obrigatório");
                    } else if (txtData.getText().toString().isEmpty()) {
                        txtData.setError("Campo obrigatório");
                    } else {
                        alteraVenda();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(VendaAltera.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void alteraVenda() {
        connection = bd.con();

        if (connection == null) {
            Toast.makeText(VendaAltera.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String query = "EXEC pAlteraVenda @cdVenda = " + id + ","
                    + "@nmProduto = '" + txtProduto.getText().toString().trim() + "',"
                    + "@cdCliente = " + idCliente + ","
                    + "@qtProduto = " + txtQuantidade.getText().toString().trim() + ","
                    + "@dtPedido = '" + txtData.getText().toString().trim() + "'";

            PreparedStatement st = connection.prepareStatement(query);
            int rs = st.executeUpdate();

            if (rs > 0) {
                Toast.makeText(VendaAltera.this, "Alterado com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (SQLException e) {
            Toast.makeText(VendaAltera.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            fecharConexao();
        }
    }

    private void fecharConexao() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            Toast.makeText(VendaAltera.this, "Erro ao fechar conexão: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
