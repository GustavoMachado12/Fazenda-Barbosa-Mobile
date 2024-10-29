package com.example.pim_barbosa.Administracao.Fornecedor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pim_barbosa.Conexao.ConSQL;
import com.example.pim_barbosa.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FornecedorConsulta extends AppCompatActivity {
    Connection connection;
    ConSQL bd = new ConSQL();

    List<DtoFornecedor> listaFornecedor;
    List<DtoFornecedor> listaFornecedorFiltrado;
    DtoFornecedor fornecedor = new DtoFornecedor();

    private TextView textNome, textCargo;
    private EditText txtBuscaPorNome;

    //ListGrid
    private ListView listView;
    SimpleAdapter ad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fornecedor_consulta);

        //BARRA DO TOPO TRANSPARENTE
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        textNome = findViewById(R.id.textNomeDash);
        textCargo = findViewById(R.id.textCargoDash);

        txtBuscaPorNome = findViewById(R.id.editTextBusca);

        textNome.setText("Gustavo");
        textCargo.setText("Auxiliar Administrativo");

        //LIST VIEW
        listView = findViewById(R.id.listaFornecedor);
        listaFornecedor = new ArrayList<>();
        listaFornecedorFiltrado = new ArrayList<>();

        txtBuscaPorNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                listarFornecedor();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    listarFornecedor();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String nome = txtBuscaPorNome.getText().toString().trim();
                if (!nome.isEmpty()) {
                    pesquisaPorNome(nome);
                } else {
                    listarFornecedor();
                }
            }
        });

        listarFornecedor();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                DtoFornecedor fornecedorSelecionado = listaFornecedorFiltrado.get(position);
                fornecedor = fornecedorSelecionado;

                registerForContextMenu(listView);
                return false;
            }
        });
    }

    public void listarFornecedor() {
        connection = bd.con();
        if (connection == null) {
            Toast.makeText(FornecedorConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String query = "EXEC pSelectFornecedor_Descripto";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            List<Map<String, String>> listaMapfornecedor = new ArrayList<>();

            listaFornecedor.clear();

            while (rs.next()) {
                DtoFornecedor fornecedor = new DtoFornecedor();
                fornecedor.setId(rs.getInt("Codigo"));
                fornecedor.setNome(rs.getString("Nome"));
                fornecedor.setEmail(rs.getString("Email"));
                fornecedor.setEndereco(rs.getString("Endereco"));
                fornecedor.setTelefone(rs.getString("Telefone"));
                fornecedor.setDocumento(rs.getString("CNPJ"));
                listaFornecedor.add(fornecedor);

                Map<String, String> fornecedorMap = new HashMap<>();
                fornecedorMap.put("fornecedor_ID", String.valueOf(fornecedor.getId()));
                fornecedorMap.put("fornecedor_Nome", fornecedor.getNome());
                fornecedorMap.put("fornecedor_CNPJ", fornecedor.getDocumento());
                fornecedorMap.put("fornecedor_Email", fornecedor.getEmail());
                fornecedorMap.put("fornecedor_Telefone", fornecedor.getTelefone());
                fornecedorMap.put("fornecedor_Endereco", fornecedor.getEndereco());
                listaMapfornecedor.add(fornecedorMap);
            }

            listaFornecedorFiltrado.clear();
            listaFornecedorFiltrado.addAll(listaFornecedor);

            String[] Fromw = {"fornecedor_ID", "fornecedor_Nome", "fornecedor_CNPJ", "fornecedor_Email", "fornecedor_Telefone", "fornecedor_Endereco"};
            int[] Tow = {R.id.Fornecedor_ID, R.id.Fornecedor_Nome, R.id.Fornecedor_CNPJ, R.id.Fornecedor_Email, R.id.Fornecedor_Telefone, R.id.Fornecedor_Endereco};

            ad = new SimpleAdapter(FornecedorConsulta.this, listaMapfornecedor, R.layout.list_view_layout_fornecedor, Fromw, Tow);
            listView.setAdapter(ad);

        } catch (SQLException e) {
            Toast.makeText(FornecedorConsulta.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void pesquisaPorNome(String nome) {
        connection = bd.con();
        if (connection == null) {
            Toast.makeText(FornecedorConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String query = "EXEC pFiltraFornecedor_Descripto @Nome = '" + nome + "'";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            List<Map<String, String>> listaMapfornecedor = new ArrayList<>();

            listaFornecedor.clear();

            while (rs.next()) {
                DtoFornecedor fornecedor = new DtoFornecedor();
                fornecedor.setId(rs.getInt("Codigo"));
                fornecedor.setNome(rs.getString("Nome"));
                fornecedor.setEmail(rs.getString("Email"));
                fornecedor.setEndereco(rs.getString("Endereco"));
                fornecedor.setTelefone(rs.getString("Telefone"));
                fornecedor.setDocumento(rs.getString("CNPJ"));
                listaFornecedor.add(fornecedor);


                Map<String, String> fornecedorMap = new HashMap<>();
                fornecedorMap.put("fornecedor_ID", String.valueOf(fornecedor.getId()));
                fornecedorMap.put("fornecedor_Nome", fornecedor.getNome());
                fornecedorMap.put("fornecedor_CNPJ", fornecedor.getDocumento());
                fornecedorMap.put("fornecedor_Email", fornecedor.getEmail());
                fornecedorMap.put("fornecedor_Telefone", fornecedor.getTelefone());
                fornecedorMap.put("fornecedor_Endereco", fornecedor.getEndereco());
                listaMapfornecedor.add(fornecedorMap);
            }

            listaFornecedorFiltrado.clear();
            listaFornecedorFiltrado.addAll(listaFornecedor);

            String[] Fromw = {"fornecedor_ID", "fornecedor_Nome", "fornecedor_CNPJ", "fornecedor_Email", "fornecedor_Telefone", "fornecedor_Endereco"};
            int[] Tow = {R.id.Fornecedor_ID, R.id.Fornecedor_Nome, R.id.Fornecedor_CNPJ, R.id.Fornecedor_Email, R.id.Fornecedor_Telefone, R.id.Fornecedor_Endereco};

            ad = new SimpleAdapter(FornecedorConsulta.this, listaMapfornecedor, R.layout.list_view_layout_fornecedor, Fromw, Tow);
            listView.setAdapter(ad);

        } catch (SQLException e) {
            Toast.makeText(FornecedorConsulta.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
        }
    }
    
    //MENU 
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0,0,0, "Alterar");
        menu.add(0,1,1, "Deletar");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == 0){
            pegaDadosEndereco(fornecedor);
            Intent intent = new Intent(FornecedorConsulta.this, FornecedorAltera.class);

            intent.putExtra("ID", fornecedor.getId());
            intent.putExtra("Nome", fornecedor.getNome());
            intent.putExtra("CNPJ", fornecedor.getDocumento());
            intent.putExtra("Email", fornecedor.getEmail());
            intent.putExtra("Telefone", fornecedor.getTelefone());
            intent.putExtra("CEP", fornecedor.getCep());
            intent.putExtra("Logradouro", fornecedor.getLogradouro());
            intent.putExtra("Municipio", fornecedor.getMunicipio());
            intent.putExtra("UF", fornecedor.getUf());
            intent.putExtra("Complemento", fornecedor.getComplemento());
            intent.putExtra("Bairro", fornecedor.getBairro());
            startActivity(intent);

        } else if(item.getItemId() == 1){
            excluiDados(fornecedor);
        }

        return super.onContextItemSelected(item);
    }

    //PEGA A STRING DE ENDEREÇO E SEPARA OS DADOS QUE ESTÃO SEPARADOS POR ','
    public void pegaDadosEndereco(DtoFornecedor fornecedor) {
        String[] dadosEndereco = fornecedor.getEndereco().split(",");

        try {
            if (dadosEndereco.length >= 4) {
                fornecedor.setCep(dadosEndereco[0].trim());
                fornecedor.setLogradouro(dadosEndereco[1].trim());
                fornecedor.setBairro(dadosEndereco[2].trim());
                fornecedor.setMunicipio(dadosEndereco[3].trim());
                fornecedor.setUf(dadosEndereco[4].trim());

                if (dadosEndereco.length > 5 && !dadosEndereco[4].trim().isEmpty()) {
                    fornecedor.setComplemento(dadosEndereco[4].trim());
                } else {
                    fornecedor.setComplemento("");
                }
            } else {
                throw new IllegalArgumentException("Endereço com formato inválido.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {

            Toast.makeText(getApplicationContext(), "Erro: Formato de endereço incompleto", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void excluiDados(DtoFornecedor fornecedor){
        AlertDialog.Builder msg = new AlertDialog.Builder(FornecedorConsulta.this);
        msg.setMessage("Deseja excluir?");
        msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                connection = bd.con();
                if (connection == null) {
                    Toast.makeText(FornecedorConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
                    return;
                }

                try{
                    String query = "EXEC pExcluiFornecedor " +
                            "@CodForn = " + fornecedor.getId() + ";";
                    Statement st = connection.createStatement();
                    boolean rs = st.execute(query);

                    if(!rs){
                        Toast.makeText(FornecedorConsulta.this, "Excluido com sucesso", Toast.LENGTH_SHORT).show();
                        listarFornecedor();
                    } else {
                        Toast.makeText(FornecedorConsulta.this, "Erro ao excluir", Toast.LENGTH_SHORT).show();
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


            }
        });


        msg.setNegativeButton("Não", null);
        msg.show();
    }
}
