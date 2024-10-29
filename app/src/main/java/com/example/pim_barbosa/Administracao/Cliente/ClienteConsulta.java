package com.example.pim_barbosa.Administracao.Cliente;

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

public class ClienteConsulta extends AppCompatActivity {
    Connection connection;
    ConSQL bd = new ConSQL();

    List<DtoCliente> listaCliente;
    List<DtoCliente> listaClienteFiltrado;
    DtoCliente cliente = new DtoCliente();


    private TextView textNome, textCargo, textID;
    private EditText txtBuscaPorNome;

    //ListGrid
    private ListView listView;
    SimpleAdapter ad;
    private int idCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_consulta);

        //BARRA DO TOPO TRANSPARENTE
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        textNome = findViewById(R.id.textNomeDash);
        textCargo = findViewById(R.id.textCargoDash);

        txtBuscaPorNome = findViewById(R.id.editTextBusca);

        textNome.setText(getIntent().getStringExtra("Nome"));
        textCargo.setText(getIntent().getStringExtra("Cargo"));

        //LIST VIEW
        listView = findViewById(R.id.listaCliente);
        listaCliente = new ArrayList<>();
        listaClienteFiltrado = new ArrayList<>();


        //BARRA DE PESQUISA | NOME
        txtBuscaPorNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                listarCliente();
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    listarCliente();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String nome = txtBuscaPorNome.getText().toString().trim();
                if (!nome.isEmpty()) {
                    pesquisaPorNome(nome);
                } else {
                    listarCliente();
                }
            }
        });


        //MOSTRA A LISTA NA LISTVIEW NO LAYOUT
        listarCliente();

        //CRIA UM MENU QUANDO UM ITEM DA LISTA FOR SELECIONADO POR MUITO TEMPO
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                DtoCliente clienteSelecionado = listaClienteFiltrado.get(position);
                cliente = clienteSelecionado;

                registerForContextMenu(listView);
                return false;
            }
        });
    }


    public void listarCliente() {
        connection = bd.con();
        if (connection == null) {
            Toast.makeText(ClienteConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String query = "EXEC pSelectCliente_Descripto";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            List<Map<String, String>> listaMapCliente = new ArrayList<>();

            listaCliente.clear();

            while (rs.next()) {
                DtoCliente cliente = new DtoCliente();
                cliente.setId(rs.getInt("Codigo"));
                cliente.setNome(rs.getString("Nome"));
                cliente.setEmail(rs.getString("Email"));
                cliente.setEndereco(rs.getString("Endereco"));
                cliente.setTelefone(rs.getString("Telefone"));
                cliente.setDocumento(rs.getString("Documento"));
                listaCliente.add(cliente); // Adiciona à lista completa

                // Cria o Map para o SimpleAdapter
                Map<String, String> clienteMap = new HashMap<>();
                clienteMap.put("Cliente_ID", String.valueOf(cliente.getId()));
                clienteMap.put("Cliente_Nome", cliente.getNome());
                clienteMap.put("Cliente_CPF_CNPJ", cliente.getDocumento());
                clienteMap.put("Cliente_Email", cliente.getEmail());
                clienteMap.put("Cliente_Telefone", cliente.getTelefone());
                clienteMap.put("Cliente_Endereco", cliente.getEndereco());
                listaMapCliente.add(clienteMap);
            }

            listaClienteFiltrado.clear();
            listaClienteFiltrado.addAll(listaCliente);

            String[] Fromw = {"Cliente_ID", "Cliente_Nome", "Cliente_CPF_CNPJ", "Cliente_Email", "Cliente_Telefone", "Cliente_Endereco"};
            int[] Tow = {R.id.Cliente_ID, R.id.Cliente_Nome, R.id.Cliente_CPF_CNPJ, R.id.Cliente_Email, R.id.Cliente_Telefone, R.id.Cliente_Endereco};

            ad = new SimpleAdapter(ClienteConsulta.this, listaMapCliente, R.layout.list_view_layout_cliente, Fromw, Tow);
            listView.setAdapter(ad);

        } catch (SQLException e) {
            Toast.makeText(ClienteConsulta.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void pesquisaPorNome(String nome) {
        connection = bd.con();
        if (connection == null) {
            Toast.makeText(ClienteConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String query = "EXEC pFiltra_Nome_Cliente @Nome = '" + nome + "'";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            List<Map<String, String>> listaMapCliente = new ArrayList<>();

            listaClienteFiltrado.clear();

            while (rs.next()) {
                DtoCliente cliente = new DtoCliente();
                cliente.setId(rs.getInt("Codigo"));
                cliente.setNome(rs.getString("Nome"));
                cliente.setEmail(rs.getString("Email"));
                cliente.setEndereco(rs.getString("Endereco"));
                cliente.setTelefone(rs.getString("Telefone"));
                cliente.setDocumento(rs.getString("Documento"));
                listaClienteFiltrado.add(cliente);

                // Cria o Map para o SimpleAdapter
                Map<String, String> clienteMap = new HashMap<>();
                clienteMap.put("Cliente_ID", String.valueOf(cliente.getId()));
                clienteMap.put("Cliente_Nome", cliente.getNome());
                clienteMap.put("Cliente_CPF_CNPJ", cliente.getDocumento());
                clienteMap.put("Cliente_Email", cliente.getEmail());
                clienteMap.put("Cliente_Telefone", cliente.getTelefone());
                clienteMap.put("Cliente_Endereco", cliente.getEndereco());
                listaMapCliente.add(clienteMap);
            }

            String[] Fromw = {"Cliente_ID", "Cliente_Nome", "Cliente_CPF_CNPJ", "Cliente_Email", "Cliente_Telefone", "Cliente_Endereco"};
            int[] Tow = {R.id.Cliente_ID, R.id.Cliente_Nome, R.id.Cliente_CPF_CNPJ, R.id.Cliente_Email, R.id.Cliente_Telefone, R.id.Cliente_Endereco};

            ad = new SimpleAdapter(ClienteConsulta.this, listaMapCliente, R.layout.list_view_layout_cliente, Fromw, Tow);
            listView.setAdapter(ad);

        } catch (SQLException e) {
            Toast.makeText(ClienteConsulta.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0,0,0, "Alterar");
        menu.add(0,1,1, "Deletar");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == 0){
            pegaDadosEndereco(cliente);
            Intent intent = new Intent(ClienteConsulta.this, ClienteAltera.class);

            intent.putExtra("NomeDash", textNome.getText().toString());
            intent.putExtra("CargoDash", textCargo.getText().toString());

            intent.putExtra("ID", cliente.getId());
            intent.putExtra("Nome", cliente.getNome());
            intent.putExtra("Documento", cliente.getDocumento());
            intent.putExtra("Email", cliente.getEmail());
            intent.putExtra("Telefone", cliente.getTelefone());

            intent.putExtra("CEP", cliente.getCep());
            intent.putExtra("Logradouro", cliente.getLogradouro());
            intent.putExtra("Bairro", cliente.getBairro());
            intent.putExtra("Municipio", cliente.getMunicipio());
            intent.putExtra("UF", cliente.getUf());
            intent.putExtra("Complemento", cliente.getComplemento());
            startActivity(intent);

        } else if(item.getItemId() == 1){
            excluiDados(cliente);
        }

        return super.onContextItemSelected(item);
    }

    //PEGA A STRING DE ENDEREÇO E SEPARA OS DADOS QUE ESTÃO SEPARADOS POR ','
    public void pegaDadosEndereco(DtoCliente cliente) {
        String[] dadosEndereco = cliente.getEndereco().split(",");

        try {
            if (dadosEndereco.length >= 5) {
                cliente.setCep(dadosEndereco[0].trim());
                cliente.setLogradouro(dadosEndereco[1].trim());
                cliente.setBairro(dadosEndereco[2].trim());
                cliente.setMunicipio(dadosEndereco[3].trim());
                cliente.setUf(dadosEndereco[4].trim());

                if (dadosEndereco.length > 5) {
                    cliente.setComplemento(dadosEndereco[5].trim());
                } else {
                    cliente.setComplemento("");
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



    public void excluiDados(DtoCliente cliente){
        AlertDialog.Builder msg = new AlertDialog.Builder(ClienteConsulta.this);
        msg.setMessage("Deseja excluir?");
        msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                connection = bd.con();
                if (connection == null) {
                    Toast.makeText(ClienteConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
                    return;
                }

                try{
                    String query = "EXEC pExcluiCliente " +
                            "@CodCliente = " + cliente.getId() + ";";
                    Statement st = connection.createStatement();
                    boolean rs = st.execute(query);

                    if(!rs){
                        Toast.makeText(ClienteConsulta.this, "Excluido com sucesso", Toast.LENGTH_SHORT).show();
                        listarCliente();
                    } else {
                        Toast.makeText(ClienteConsulta.this, "Erro ao excluir", Toast.LENGTH_SHORT).show();
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


