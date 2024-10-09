package com.example.pim_barbosa.Administracao.Cliente;

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
import com.example.pim_barbosa.Conexao.ConSQL;
import com.example.pim_barbosa.R;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

public class ClienteAltera extends AppCompatActivity {
    EditText txtNome, txtDocumento, txtEmail, txtTelefone, txtCEP, txtRua, txtMunicipio, txtEstado, txtComplemento;
    Button btnAlterar;
    String url = "https://cep.awesomeapi.com.br/json/";
    String txtEndereco;
    int id;

    DtoCliente cliente;

    //BANCO
    Connection connection;
    ConSQL bd = new ConSQL();
    String str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cliente_alterar);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        txtNome = findViewById(R.id.cliente_alterar_nome);
        txtDocumento = findViewById(R.id.cliente_alterar_documento);
        txtEmail = findViewById(R.id.cliente_alterar_email);
        txtTelefone = findViewById(R.id.cliente_alterar_telefone);
        txtCEP = findViewById(R.id.cliente_alterar_CEP);
        txtRua = findViewById(R.id.cliente_alterar_endereco);
        txtMunicipio = findViewById(R.id.cliente_alterar_municipio);
        txtEstado = findViewById(R.id.cliente_alterar_estado);
        txtComplemento = findViewById(R.id.cliente_alterar_complemento);
        btnAlterar = findViewById(R.id.btn_cliente_alterar);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("ID");
        txtNome.setText(bundle.getString("Nome"));
        txtDocumento.setText(bundle.getString("Documento"));
        txtEmail.setText(bundle.getString("Email"));
        txtTelefone.setText(bundle.getString("Telefone"));
        txtCEP.setText(bundle.getString("CEP"));
        txtRua.setText(bundle.getString("Logradouro"));
        txtMunicipio.setText(bundle.getString("Municipio"));
        txtEstado.setText(bundle.getString("UF"));
        txtComplemento.setText(bundle.getString("Complemento"));



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
                                txtMunicipio.setText(state);
                                txtEstado.setText(city);

                                txtEndereco = txtCEP.getText().toString() + ", " +
                                            address + ", " +
                                            state + ", " +
                                            city + ", " +
                                            txtComplemento.getText().toString();


                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    Volley.newRequestQueue(ClienteAltera.this).add(request);

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
                    } else if (txtDocumento.getText().toString().length() == 11) {
                        alteraClienteF();  // Pessoa física
                    } else if (txtDocumento.getText().toString().length() == 14) {
                        alteraClienteJ();  // Pessoa jurídica
                    } else {
                        Toast.makeText(ClienteAltera.this, "Dados Incorretos", Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(ClienteAltera.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ClienteAltera.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    //ALTERA DADOS DE CLIENTE FISICO
    public void alteraClienteF(){
        connection = bd.con();
        if(bd == null)
            Toast.makeText(ClienteAltera.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();

        try{
            String query =  "EXEC pAlteraClienteF " +
                    "@CodCli = " + id + ", " +
                    "@NomeCli = '" + txtNome.getText().toString() + "', " +
                    "@EmailCli = '" + txtEmail.getText().toString() + "', " +
                    "@EnderecoCli = '" + txtEndereco + "', " +
                    "@TelefoneCli = '" + txtTelefone.getText().toString() + "', " +
                    "@CPF_Cli = '" + txtDocumento.getText().toString().trim() + "';";
            Statement st = connection.createStatement();
            int rs = st.executeUpdate(query);

            if(rs == 0){
                Toast.makeText(ClienteAltera.this, "Alterado com sucesso", Toast.LENGTH_SHORT).show();
                Intent back = new Intent(ClienteAltera.this, ClienteConsulta.class);
                startActivity(back);
            }
        } catch (SQLException e){
            Toast.makeText(ClienteAltera.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
        }


    }

    //ALTERA DADOS DE CLIENTE JURIDICO
    public void alteraClienteJ(){
        connection = bd.con();
        if(bd == null)
            Toast.makeText(ClienteAltera.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();

        try{
            String query =  "EXEC pAlteraClienteJ " +
                    "@CodCli = " + id + ", " +
                    "@NomeCli = '" + txtNome.getText().toString() + "', " +
                    "@EmailCli = '" + txtEmail.getText().toString() + "', " +
                    "@EnderecoCli = '" + txtEndereco + "', " +
                    "@TelefoneCli = '" + txtTelefone.getText().toString() + "', " +
                    "@CNPJ_Cli = '" + txtDocumento.getText().toString() + "';";
            Statement st = connection.createStatement();
            int rs = st.executeUpdate(query);

            if(rs == 0){
                Toast.makeText(ClienteAltera.this, "Alterado com sucesso", Toast.LENGTH_SHORT).show();
                Intent back = new Intent(ClienteAltera.this, ClienteConsulta.class);
                startActivity(back);
            }

        } catch (SQLException e){
            Toast.makeText(ClienteAltera.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
        }
    }
}
