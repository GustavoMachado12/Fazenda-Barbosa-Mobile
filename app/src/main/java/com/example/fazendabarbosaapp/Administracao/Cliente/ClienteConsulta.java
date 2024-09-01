package com.example.fazendabarbosaapp.Administracao.Cliente;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fazendabarbosaapp.R;

public class ClienteConsulta extends AppCompatActivity {

    TextView textNome, textCargo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_consulta);


        textNome = findViewById(R.id.textNomeDash);
        textCargo = findViewById(R.id.textCargoDash);
        textNome.setText("Gustavo");
        textCargo.setText("Auxiliar Administrativo");


        
    }
}
