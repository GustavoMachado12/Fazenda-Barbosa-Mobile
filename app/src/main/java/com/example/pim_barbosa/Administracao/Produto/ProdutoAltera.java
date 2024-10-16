package com.example.pim_barbosa.Administracao.Produto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.pim_barbosa.Administracao.Banco.DtoCategoria;
import com.example.pim_barbosa.Conexao.ConSQL;
import com.example.pim_barbosa.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class ProdutoAltera extends AppCompatActivity {
    EditText txtNome, txtQuantidade, txtPreco;
    AutoCompleteTextView txtCategoria;
    Button btnAlterar;
    ImageView imgCategoria;
    int id;

    //BANCO
    Connection connection;
    ConSQL bd = new ConSQL();

    private  int codigoCategoria;
    private String nmCategoria;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_produto_alterar);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        txtNome = findViewById(R.id.produto_alterar_nome);
        txtCategoria = findViewById(R.id.produto_alterar_categoria);
        txtQuantidade = findViewById(R.id.produto_alterar_quantidade);
        txtPreco = findViewById(R.id.produto_alterar_valor);
        imgCategoria = findViewById(R.id.img_produto_spinner);
        btnAlterar = findViewById(R.id.btn_produto_alterar);

        try{

            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                id = bundle.getInt("ID");

                txtNome.setText(bundle.getString("Nome"));
                txtCategoria.setText(bundle.getString("Categoria"));
                nmCategoria = String.valueOf(txtCategoria.getText());
                obterCodigoCategoriaPorNome(nmCategoria);

                float preco = bundle.getFloat("Valor");
                txtPreco.setText(String.valueOf(preco));
                int quantidade = bundle.getInt("Quantidade");
                txtQuantidade.setText(String.valueOf(quantidade));


            }
        } catch (Exception e){
            e.printStackTrace();
        }


        imgCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<DtoCategoria> categorias = listarCategoria();

                if (!categorias.isEmpty()) {
                    txtCategoria.showDropDown();
                }

            }
        });

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (txtNome.getText().toString().isEmpty()) {
                        txtNome.setError("Campo obrigatório");
                    } else if (txtCategoria.getText().toString().isEmpty()) {
                        txtCategoria.setError("Campo obrigatório");
                    } else if (txtQuantidade.getText().toString().isEmpty()) {
                        txtQuantidade.setError("Campo obrigatório");
                    } else if (txtPreco.getText().toString().isEmpty()) {
                        txtPreco.setError("Campo obrigatório");
                    } else {
                        alteraDadosProduto();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(ProdutoAltera.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    public ArrayList<DtoCategoria> listarCategoria() {
        connection = bd.con();
        final ArrayList<DtoCategoria> listaCategoria = new ArrayList<>();
        final HashMap<String, Integer> categoriaMap = new HashMap<>(); // Para armazenar o nome e o código da categoria

        if (connection == null) {
            Toast.makeText(ProdutoAltera.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return listaCategoria;
        }

        try {
            String query = "SELECT * FROM tb_Categoria";
            PreparedStatement st = connection.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                DtoCategoria categoria = new DtoCategoria();
                categoria.setId(rs.getInt("cd_Categoria"));
                categoria.setCategoria(rs.getString("nm_Categoria"));
                listaCategoria.add(categoria);
                categoriaMap.put(categoria.getCategoria(), categoria.getId()); // Associa o nome ao código da categoria
            }

            ArrayList<String> nomesCategorias = new ArrayList<>();
            for (DtoCategoria cat : listaCategoria) {
                nomesCategorias.add(cat.getCategoria());
            }

            ArrayAdapter<String> noCoreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nomesCategorias);
            txtCategoria.setAdapter(noCoreAdapter);

            txtCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String nomeCategoriaSelecionada = (String) parent.getItemAtPosition(position);
                    codigoCategoria = categoriaMap.get(nomeCategoriaSelecionada);
                }
            });

        } catch (SQLException e) {
            Toast.makeText(ProdutoAltera.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close(); // Fecha a conexão
                }
            } catch (SQLException e) {
                Toast.makeText(ProdutoAltera.this, "Erro ao fechar conexão: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return listaCategoria;
    }

    public int obterCodigoCategoriaPorNome(String nomeCategoria) {
        connection = bd.con();

        if (connection == null) {
            Toast.makeText(ProdutoAltera.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return codigoCategoria;
        }

        try {
            String query = "SELECT cd_Categoria FROM tb_Categoria WHERE nm_Categoria = '" + nmCategoria + "'";
            PreparedStatement st = connection.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                codigoCategoria = rs.getInt("cd_Categoria");           }

        } catch (SQLException e) {
            Toast.makeText(ProdutoAltera.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                Toast.makeText(ProdutoAltera.this, "Erro ao fechar conexão: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return codigoCategoria;
    }


    public void alteraDadosProduto(){
        connection = bd.con();
        if(bd == null) {
            Toast.makeText(ProdutoAltera.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
        }if(codigoCategoria == 0) {
            txtCategoria.setError("Categoria vazia");
        }
        try{
            String query =  "EXEC pAlteraProduto " +
                    "@CodProd = " + id + ", " +
                    "@NomeProd = '" + txtNome.getText().toString() + "', " +
                    "@Categoria = " + codigoCategoria + ", " +
                    "@Quantidade = '" + txtQuantidade.getText().toString() + "', " +
                    "@Valor = '" +  txtPreco.getText().toString() + "'";
            Statement st = connection.createStatement();
            int rs = st.executeUpdate(query);

            if(rs != 0){
                Toast.makeText(ProdutoAltera.this, "Alterado com sucesso", Toast.LENGTH_SHORT).show();
                Intent back = new Intent(ProdutoAltera.this, ProdutoConsulta.class);
                startActivity(back);
            }

        } catch (SQLException e){
            Toast.makeText(ProdutoAltera.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
        }
    }

}


