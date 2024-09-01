package com.example.fazendabarbosaapp;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.fazendabarbosaapp.Administracao.MenuActivity;

public class MainActivity extends AppCompatActivity {
    EditText editTextLogin, editTextSenha;
    Button buttonLogin;

    private Switch myswitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //BARRA DO TOPO TRANSPARENTE
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        editTextLogin = findViewById(R.id.editTextLoginClient);
        editTextSenha = findViewById(R.id.editTextPasswordLogin);
        buttonLogin = findViewById(R.id.buttonLogin);


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //VERIFICA SE ESTÁ VAZIO
                if(editTextLogin.getText().toString().isEmpty()){
                    editTextLogin.requestFocus();
                    Toast.makeText(MainActivity.this, "Login vazio.", Toast.LENGTH_SHORT).show();
                } else if (editTextSenha.getText().toString().isEmpty()) {
                    editTextSenha.requestFocus();
                    Toast.makeText(MainActivity.this, "Senha vazia.", Toast.LENGTH_SHORT).show();
                }

                //VERIFICA O TIPO DE PERFIL
                else if(editTextLogin.getText().toString().equals("adm")) { //FUNCIONARIO
                    if (editTextSenha.getText().toString().equals("123")) {
                        Toast.makeText(MainActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(login);
                    } else {
                        editTextSenha.requestFocus();
                        Toast.makeText(MainActivity.this, "Senha incorreta ou vazio.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    editTextLogin.requestFocus();
                    Toast.makeText(MainActivity.this, "Login incorreto ou vazio.", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }

    //MOSTRA SENHA ESCONDIDA
    public void ShowHidePass(View view){
        if(view.getId()==R.id.show_password){
            if(editTextSenha.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_password_off);
                //MOSTRA SENHA
                editTextSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView)(view)).setImageResource(R.drawable.ic_password_on);
                //OCULTA SENHA
                editTextSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

}