package com.example.pim_barbosa.Administracao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.pim_barbosa.Administracao.Cliente.ClienteConsulta;
import com.example.pim_barbosa.Administracao.Fornecedor.FornecedorConsulta;
import com.example.pim_barbosa.R;

public class MenuActivity extends AppCompatActivity {
    CardView cardCliente, cardFornecedor, cardProducao, cardEstoque, cardVenda, cardSobre;
    TextView textNome, textCargo;
    TextView dash_Consulta, dash_Relatorio, dash_Outros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //BARRA DO TOPO TRANSPARENTE
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        cardCliente = findViewById(R.id.cardCliente);
        cardFornecedor = findViewById(R.id.cardFornecedor);
        cardProducao = findViewById(R.id.cardProducao);
        cardEstoque = findViewById(R.id.cardEstoque);
        cardVenda = findViewById(R.id.cardVenda);
        cardSobre = findViewById(R.id.cardSobre);

        textNome = findViewById(R.id.textNomeDash);
        textCargo = findViewById(R.id.textCargoDash);

        dash_Consulta = findViewById(R.id.textDashboardConsulta);
        dash_Relatorio = findViewById(R.id.textDashboardRelatorio);
        dash_Outros= findViewById(R.id.textDashboardOutro);

        textNome.setText("Gustavo");
        textCargo.setText("Auxiliar Administrativo");

        dash_Consulta.setText("Consultas");
        dash_Relatorio.setText("Relatórios");
        dash_Outros.setText("Outros");


        //textConsulta.setPaintFlags(textConsulta.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        cardCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cliente = new Intent(MenuActivity.this, ClienteConsulta.class);
                startActivity(cliente);
            }
        });

        cardFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fornecedor = new Intent(MenuActivity.this, FornecedorConsulta.class);
                startActivity(fornecedor);
            }
        });

    }
}
