package com.example.pim_barbosa.Administracao.Fornecedor;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pim_barbosa.Administracao.Cliente.ClienteConsulta;
import com.example.pim_barbosa.Conexao.ConSQL;
import com.example.pim_barbosa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class FornecedorAltera extends AppCompatActivity{
    EditText txtNome, txtDocumento, txtEmail, txtTelefone, txtCEP, txtRua, txtMunicipio, txtEstado, txtComplemento;
    Button btnAlterar;
    String url = "https://cep.awesomeapi.com.br/json/";
    String txtEndereco;
    int id;

    //BANCO
    Connection connection;
    ConSQL bd = new ConSQL();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fornecedor_alterar);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        txtNome = findViewById(R.id.fornecedor_alterar_nome);
        txtDocumento = findViewById(R.id.fornecedor_alterar_documento);
        txtEmail = findViewById(R.id.fornecedor_alterar_email);
        txtTelefone = findViewById(R.id.fornecedor_alterar_telefone);


        txtCEP = findViewById(R.id.fornecedor_alterar_CEP);
        txtRua = findViewById(R.id.fornecedor_alterar_endereco);
        txtMunicipio = findViewById(R.id.fornecedor_alterar_municipio);
        txtEstado = findViewById(R.id.fornecedor_alterar_estado);
        txtComplemento = findViewById(R.id.fornecedor_alterar_complemento);
        btnAlterar = findViewById(R.id.btn_fornecedor_alterar);

        //BUNDLES EXTRAS
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("ID");
        txtNome.setText(bundle.getString("Nome"));
        txtDocumento.setText(bundle.getString("CNPJ"));
        txtEmail.setText(bundle.getString("Email"));
        txtTelefone.setText(bundle.getString("Telefone"));
        txtCEP.setText(bundle.getString("CEP"));
        txtRua.setText(bundle.getString("Logradouro"));
        txtMunicipio.setText(bundle.getString("Municipio"));
        txtEstado.setText(bundle.getString("UF"));
        txtComplemento.setText(bundle.getString("Complemento"));

        //ADICIONA O ENDEREÇO COMPLETO PARA O BANCO
        txtEndereco = txtCEP.getText().toString() + ", " +
                txtRua.getText().toString() + ", " +
                txtMunicipio.getText().toString() + ", " +
                txtEstado.getText().toString() + ", " +
                txtComplemento.getText().toString();


        //API PARA BUSCAR CEP QUANDO DIGITADO POR COMPLETO
        txtCEP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //QUANDO O USUÁRIO DIGITAR O CEP COMPLETO A API VAI BUSCAR O ENDEREÇO
            @Override
            public void afterTextChanged(Editable s) {
                if(txtCEP.toString().trim().length() > 7){

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + txtCEP.getText().toString(), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String address = response.getString("address");
                                String state = response.getString("state");
                                String city = response.getString("city");

                                txtRua.setText(address);
                                txtEstado.setText(state);
                                txtMunicipio.setText(city);

                                txtEndereco = txtCEP.getText().toString() + ", " +
                                        address + ", " +
                                        city + ", " +
                                        state;



                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    Volley.newRequestQueue(FornecedorAltera.this).add(request);

                }
            }
        });


        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (txtNome.getText().toString().isEmpty()) {
                        txtNome.setError("Campo obrigatório");
                    } else if (txtDocumento.getText().toString().isEmpty()) {
                        txtDocumento.setError("Campo obrigatório");
                    } else if (txtEmail.getText().toString().isEmpty()) {
                        txtEmail.setError("Campo obrigatório");
                    } else if (txtTelefone.getText().toString().isEmpty()) {
                        txtTelefone.setError("Campo obrigatório");
                    } else if (txtEndereco == null) {
                        txtCEP.setError("Campo obrigatório");
                    } else if (txtCEP.getText().toString().isEmpty()) {
                        txtCEP.setError("Campo obrigatório");
                    } else if (txtDocumento.getText().toString().length() == 14) {
                        txtEndereco += ", " + txtComplemento.getText().toString();
                        alteraFornecedor();  // Pessoa jurídica
                    } else {
                        Toast.makeText(FornecedorAltera.this, "Dados Incorretos", Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(FornecedorAltera.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(FornecedorAltera.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    //ALTERA DADOS DO FORNECEDOR
    public void alteraFornecedor() {
        connection = bd.con();
        if (bd == null)
            Toast.makeText(FornecedorAltera.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();

        try {
            String query = "EXEC pAlteraFornecedor " +
                    "@CodForn = " + id + ", " +
                    "@NomeForn = '" + txtNome.getText().toString() + "', " +
                    "@EmailForn = '" + txtEmail.getText().toString() + "', " +
                    "@EnderecoForn = '" + txtEndereco + "', " +
                    "@TelefoneForn = '" + txtTelefone.getText().toString() + "', " +
                    "@CNPJ = '" + txtDocumento.getText().toString() + "';";
            Statement st = connection.createStatement();
            int rs = st.executeUpdate(query);

            if (rs != 0) {
                Toast.makeText(FornecedorAltera.this, "Alterado com sucesso", Toast.LENGTH_SHORT).show();
                Intent back = new Intent(FornecedorAltera.this, FornecedorConsulta.class);
                startActivity(back);
            }

        } catch (SQLException e) {
            Toast.makeText(FornecedorAltera.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
        }
    }
}