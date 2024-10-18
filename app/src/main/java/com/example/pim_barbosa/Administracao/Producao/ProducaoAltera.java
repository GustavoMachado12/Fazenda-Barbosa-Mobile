package com.example.pim_barbosa.Administracao.Producao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pim_barbosa.Administracao.Banco.DtoStatusProducao;
import com.example.pim_barbosa.Conexao.ConSQL;
import com.example.pim_barbosa.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ProducaoAltera extends AppCompatActivity {
    EditText txtNomeProduto, txtDataPlantio, txtDataColheita, txtValor;
    AutoCompleteTextView txtStatus;
    ImageView imgMenu;
    Button btnAlterar;
    int id, codStatus;
    String nomeStatus;

    SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    Connection connection;
    ConSQL bd = new ConSQL();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producao_alterar);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        txtNomeProduto = findViewById(R.id.producao_alterar_produto);
        txtDataPlantio = findViewById(R.id.producao_alterar_data_plantio);
        txtDataColheita = findViewById(R.id.producao_alterar_data_colheita);
        txtStatus = findViewById(R.id.producao_alterar_status);
        txtValor = findViewById(R.id.produto_alterar_valor);
        imgMenu = findViewById(R.id.img_producao_spinner);
        btnAlterar = findViewById(R.id.btn_producao_alterar);

        try {
            Bundle bundle = getIntent().getExtras();
            id = bundle.getInt("ID");
            txtNomeProduto.setText(bundle.getString("Produto"));

            String dtPlantio = bundle.getString("dtPlantio");
            if (dtPlantio != null && !dtPlantio.isEmpty()) {
                Date datePlantio = sqlDateFormat.parse(dtPlantio);
                txtDataPlantio.setText(sqlDateFormat.format(datePlantio));
            } else {
                txtDataPlantio.setError("Data de plantio inválida");
            }

            String dtColheita = bundle.getString("dtColheita");
            if (dtColheita != null && !dtColheita.isEmpty()) {
                Date dateColheita = sqlDateFormat.parse(dtColheita);
                txtDataColheita.setText(sqlDateFormat.format(dateColheita));
            } else {
                txtDataColheita.setError("Data de colheita inválida");
            }

            txtStatus.setText(bundle.getString("Status"));
            nomeStatus = txtStatus.getText().toString();
            obterCodigoStatusProducaoPorNome(nomeStatus); // Pega o ID do status

            float valor = bundle.getFloat("Valor");
            txtValor.setText(String.valueOf(valor));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<DtoStatusProducao> producao = listarStatusProducao();
                if (!producao.isEmpty()) {
                    txtStatus.showDropDown();
                }
            }
        });

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (txtNomeProduto.getText().toString().isEmpty()) {
                        txtNomeProduto.setError("Campo obrigatório");
                    } else if (txtDataPlantio.getText().toString().isEmpty()) {
                        txtDataPlantio.setError("Campo obrigatório");
                    } else if (txtDataColheita.getText().toString().isEmpty()) {
                        txtDataColheita.setError("Campo obrigatório");
                    } else if (txtStatus.getText().toString().isEmpty()) {
                        txtStatus.setError("Campo obrigatório");
                    } else if (txtValor.getText().toString().isEmpty()) {
                        txtValor.setError("Campo obrigatório");
                    } else {
                        alteraProducao();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(ProducaoAltera.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public ArrayList<DtoStatusProducao> listarStatusProducao() {
        connection = bd.con();
        ArrayList<DtoStatusProducao> listaStatusProducao = new ArrayList<>();
        HashMap<String, Integer> statusProducaoMap = new HashMap<>();

        if (connection == null) {
            Toast.makeText(ProducaoAltera.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return listaStatusProducao;
        }

        try {
            String query = "SELECT * FROM tb_StatusColheita";
            PreparedStatement st = connection.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                DtoStatusProducao statusProducao = new DtoStatusProducao();
                statusProducao.setId(rs.getInt("cd_Status"));
                statusProducao.setNomeStatus(rs.getString("nm_Status"));
                listaStatusProducao.add(statusProducao);
                statusProducaoMap.put(statusProducao.getNomeStatus(), statusProducao.getId());
            }

            ArrayList<String> nomesStatus = new ArrayList<>();
            for (DtoStatusProducao cat : listaStatusProducao) {
                nomesStatus.add(cat.getNomeStatus());
            }

            ArrayAdapter<String> noCoreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nomesStatus);
            txtStatus.setAdapter(noCoreAdapter);

            txtStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String nomeStatusSelecionado = (String) parent.getItemAtPosition(position);
                    codStatus = statusProducaoMap.get(nomeStatusSelecionado);
                }
            });

        } catch (SQLException e) {
            Toast.makeText(ProducaoAltera.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            fecharConexao();
        }

        return listaStatusProducao;
    }


    public int obterCodigoStatusProducaoPorNome(String nomeStatus) {
        connection = bd.con();

        if (connection == null) {
            Toast.makeText(ProducaoAltera.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return codStatus;
        }

        try {
            String query = "SELECT cd_Status FROM tb_StatusColheita WHERE nm_Status = ?";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, nomeStatus);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                codStatus = rs.getInt("cd_Status");
            }

        } catch (SQLException e) {
            Toast.makeText(ProducaoAltera.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            fecharConexao();
        }

        return codStatus;
    }


    public void alteraProducao() {
        connection = bd.con();

        if (connection == null) {
            Toast.makeText(ProducaoAltera.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String query = "EXEC pAlteraProducao @cdProducao = ?, @nmProduto = ?, @dtPlantio = ?, @dtColheita = ?, @cdStatus = ?, @vlLote = ?";
            PreparedStatement st = connection.prepareStatement(query);
            st.setInt(1, id);
            st.setString(2, txtNomeProduto.getText().toString());
            st.setString(3, txtDataPlantio.getText().toString());
            st.setString(4, txtDataColheita.getText().toString());
            st.setInt(5, codStatus);
            st.setFloat(6, Float.parseFloat(txtValor.getText().toString().trim()));

            int rs = st.executeUpdate();

            if (rs > 0) {
                Toast.makeText(ProducaoAltera.this, "Alterado com sucesso", Toast.LENGTH_SHORT).show();
                Intent back = new Intent(ProducaoAltera.this, ProducaoConsulta.class);
                startActivity(back);
            }
        } catch (SQLException e) {
            Toast.makeText(ProducaoAltera.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ProducaoAltera.this, "Erro ao fechar conexão: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
