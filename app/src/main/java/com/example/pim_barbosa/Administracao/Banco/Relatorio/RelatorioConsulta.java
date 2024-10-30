package com.example.pim_barbosa.Administracao.Banco.Relatorio;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pim_barbosa.R;

public class RelatorioConsulta extends AppCompatActivity {
    private TextView textNome, textCargo, textConsultaOpcao;

    //TOTAL DE VENDAS
    private TextView txtTotalVendas_Relatorio, txtTotalVendas_Ano_Relatorio, txtTotalVendas_QuantidadeTotal_Relatorio;

    GeraRelatorio geraRelatorio = new GeraRelatorio();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_relatorio);

        //BARRA DO TOPO TRANSPARENTE
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        textNome = findViewById(R.id.textNomeDash);
        textCargo = findViewById(R.id.textCargoDash);
        textNome.setText(getIntent().getStringExtra("Nome"));
        textCargo.setText(getIntent().getStringExtra("Cargo"));

        //TOTAL DE VENDAS
        txtTotalVendas_Relatorio = findViewById(R.id.txtTotalVendas_Relatorio);
        txtTotalVendas_Ano_Relatorio = findViewById(R.id.txtTotalVendas_Ano_Relatorio);
        txtTotalVendas_QuantidadeTotal_Relatorio = findViewById(R.id.txtTotalVendas_QuantidadeTotal_Relatorio);

        geraRelatorio.totalVendas(2024, RelatorioConsulta.this);




        textConsultaOpcao = findViewById(R.id.textDashboardRelatorio_Consulta);
        textConsultaOpcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
