package com.example.pim_barbosa;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pim_barbosa.Administracao.Banco.LoginAcesso;
import com.example.pim_barbosa.Administracao.MenuActivity;

public class MainActivity extends AppCompatActivity {
    EditText editTextLogin, editTextSenha;
    Button buttonLogin;

    LoginAcesso loginAcesso = new LoginAcesso();
    int nvlLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                String[] loginData = loginAcesso.verificaAcesso(editTextLogin.getText().toString(), editTextSenha.getText().toString());
                nvlLogin = Integer.parseInt(loginData[0]);

                if (nvlLogin != 0) {
                    Toast.makeText(MainActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                    if (nvlLogin != 1) {
                        Intent loginIntent = new Intent(MainActivity.this, MenuActivity.class);
                        loginIntent.putExtra("Nome", loginData[1]);
                        loginIntent.putExtra("Cargo", loginData[2]);
                        startActivity(loginIntent);
                    } else {
                        Toast.makeText(MainActivity.this, "Acess Home Client", Toast.LENGTH_LONG).show();
                    }
                } else {
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