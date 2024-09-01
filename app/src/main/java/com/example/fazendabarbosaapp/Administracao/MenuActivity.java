package com.example.fazendabarbosaapp.Administracao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.fazendabarbosaapp.Administracao.Cliente.ClienteConsulta;
import com.example.fazendabarbosaapp.R;

public class MenuActivity extends AppCompatActivity {
    CardView cardCliente, cardFornecedor, cardProducao, cardEstoque, cardVenda, cardSobre;
    TextView textNome, textCargo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        cardCliente = findViewById(R.id.cardCliente);
        cardFornecedor = findViewById(R.id.cardFornecedor);
        cardProducao = findViewById(R.id.cardProducao);
        cardEstoque = findViewById(R.id.cardEstoque);
        cardVenda = findViewById(R.id.cardVenda);
        cardSobre = findViewById(R.id.cardSobre);

        textNome = findViewById(R.id.textNomeDash);
        textCargo = findViewById(R.id.textCargoDash);


        textNome.setText("Gustavo");
        textCargo.setText("Auxiliar Administrativo");


        //textConsulta.setPaintFlags(textConsulta.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        cardCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cliente = new Intent(MenuActivity.this, ClienteConsulta.class);
                startActivity(cliente);
            }
        });
    }
}
