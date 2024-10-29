package com.example.pim_barbosa.Administracao.Produto;

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

public class ProdutoConsulta extends AppCompatActivity {
    Connection connection;
    ConSQL bd = new ConSQL();

    List<DtoProduto> listaProduto;
    List<DtoProduto> listaProdutoFiltrado;
    DtoProduto produto = new DtoProduto();

    private TextView textNome, textCargo, textID;
    private EditText txtBuscaPorNome;

    private ListView listView;
    SimpleAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_consulta);

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
        listView = findViewById(R.id.listaProduto);
        listaProduto = new ArrayList<>();
        listaProdutoFiltrado = new ArrayList<>();


        //BARRA DE PESQUISA | NOME
        txtBuscaPorNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                listarProduto();
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    listarProduto();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String nome = txtBuscaPorNome.getText().toString().trim();
                if (!nome.isEmpty()) {
                    pesquisaPorNome(nome);
                } else {
                    listarProduto();
                }
            }
        });


        listarProduto();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                DtoProduto produtoSelecionado = listaProdutoFiltrado.get(position);
                produto = produtoSelecionado;

                registerForContextMenu(listView);
                return false;
            }
        });
    }


    public void listarProduto() {
        connection = bd.con();
        if (connection == null) {
            Toast.makeText(ProdutoConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String query = "EXEC pSelectProduto";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            List<Map<String, String>> listaMapProduto = new ArrayList<>();

            while (rs.next()) {
                DtoProduto produto = new DtoProduto();
                produto.setId(rs.getInt("Codigo"));
                produto.setProduto(rs.getString("Nome"));
                produto.setCategoria(rs.getString("Categoria"));
                produto.setPreco(rs.getFloat("Valor"));
                produto.setQuantidade(rs.getInt("Quantidade"));
                listaProduto.add(produto); // Adiciona à lista completa

                Map<String, String> produtoMap = new HashMap<>();
                produtoMap.put("produto_ID", String.valueOf(produto.getId()));
                produtoMap.put("produto_Nome", produto.getProduto());
                produtoMap.put("produto_Categoria", produto.getCategoria());
                produtoMap.put("produto_Valor", String.valueOf(produto.getPreco()));
                produtoMap.put("produto_Quantidade", String.valueOf(produto.getQuantidade()));
                listaMapProduto.add(produtoMap);
            }

            listaProdutoFiltrado.clear();
            listaProdutoFiltrado.addAll(listaProduto);

            String[] Fromw = {"produto_ID", "produto_Nome", "produto_Categoria", "produto_Valor", "produto_Quantidade"};
            int[] Tow = {R.id.Produto_ID, R.id.Produto_Nome, R.id.Produto_Categoria, R.id.Produto_Valor, R.id.Produto_Quantidade};

            ad = new SimpleAdapter(ProdutoConsulta.this, listaMapProduto, R.layout.list_view_layout_produto, Fromw, Tow);
            listView.setAdapter(ad);

        } catch (SQLException e) {
            Toast.makeText(ProdutoConsulta.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void pesquisaPorNome(String nome) {
        connection = bd.con();
        if (connection == null) {
            Toast.makeText(ProdutoConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String query = "EXEC pFiltraProdutos @Produto = '" + nome + "'";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            List<Map<String, String>> listaMapproduto = new ArrayList<>();

            listaProdutoFiltrado.clear();

            while (rs.next()) {
                DtoProduto produto = new DtoProduto();
                produto.setId(rs.getInt("Codigo"));
                produto.setProduto(rs.getString("Nome"));
                produto.setCategoria(rs.getString("Categoria"));
                produto.setPreco(rs.getFloat("Valor"));
                produto.setQuantidade(rs.getInt("Quantidade"));
                listaProdutoFiltrado.add(produto);

                Map<String, String> produtoMap = new HashMap<>();
                produtoMap.put("produto_ID", String.valueOf(produto.getId()));
                produtoMap.put("produto_Nome", produto.getProduto());
                produtoMap.put("produto_Categoria", produto.getCategoria());
                produtoMap.put("produto_Valor", String.valueOf(produto.getPreco()));
                produtoMap.put("produto_Quantidade", String.valueOf(produto.getQuantidade()));
                listaMapproduto.add(produtoMap);
            }

            String[] Fromw = {"produto_ID", "produto_Nome", "produto_Categoria", "produto_Valor", "produto_Quantidade"};
            int[] Tow = {R.id.Produto_ID, R.id.Produto_Nome, R.id.Produto_Categoria, R.id.Produto_Valor, R.id.Produto_Quantidade};

            ad = new SimpleAdapter(ProdutoConsulta.this, listaMapproduto, R.layout.list_view_layout_produto, Fromw, Tow);
            listView.setAdapter(ad);

        } catch (SQLException e) {
            Toast.makeText(ProdutoConsulta.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
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
            try {
                Intent altera = new Intent(ProdutoConsulta.this, ProdutoAltera.class);
                altera.putExtra("NomeDash", textNome.getText().toString());
                altera.putExtra("CargoDash", textCargo.getText().toString());

                altera.putExtra("ID", produto.getId());
                altera.putExtra("Nome", produto.getProduto());
                altera.putExtra("Categoria", produto.getCategoria());
                altera.putExtra("Valor", produto.getPreco());
                altera.putExtra("Quantidade", produto.getQuantidade());
                startActivity(altera);
            } catch (Exception e){
                e.printStackTrace();
            }


        } else if(item.getItemId() == 1){
            excluiDados(produto);
        }

        return super.onContextItemSelected(item);
    }

    public void excluiDados(DtoProduto produto){
        AlertDialog.Builder msg = new AlertDialog.Builder(ProdutoConsulta.this);
        msg.setMessage("Deseja excluir?");
        msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                connection = bd.con();
                if (connection == null) {
                    Toast.makeText(ProdutoConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
                    return;
                }

                try{
                    String query = "EXEC pDeletaProduto " +
                            "@CodProduto = " + produto.getId() + ";";
                    Statement st = connection.createStatement();
                    boolean rs = st.execute(query);

                    if(!rs){
                        Toast.makeText(ProdutoConsulta.this, "Excluido com sucesso", Toast.LENGTH_SHORT).show();
                        listarProduto();
                    } else {
                        Toast.makeText(ProdutoConsulta.this, "Erro ao excluir", Toast.LENGTH_SHORT).show();
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


