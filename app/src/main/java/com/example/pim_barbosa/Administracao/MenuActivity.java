package com.example.pim_barbosa.Administracao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.pim_barbosa.Administracao.Banco.Funcionario.DtoFuncionario;
import com.example.pim_barbosa.Administracao.Banco.Relatorio.RelatorioConsulta;
import com.example.pim_barbosa.Administracao.Cliente.ClienteConsulta;
import com.example.pim_barbosa.Administracao.Fornecedor.FornecedorConsulta;
import com.example.pim_barbosa.Administracao.Producao.ProducaoConsulta;
import com.example.pim_barbosa.Administracao.Produto.ProdutoConsulta;
import com.example.pim_barbosa.Administracao.Venda.VendaConsulta;
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
        //dash_Outros= findViewById(R.id.textDashboardOutro);


        //DASHBOARD c/ NOME e CARGO
        String nomeFuncionario = getIntent().getStringExtra("Nome");
        String cargoFuncionario = getIntent().getStringExtra("Cargo");
        if (nomeFuncionario != null && cargoFuncionario != null) {
            textNome.setText(nomeFuncionario);
            textCargo.setText(cargoFuncionario);
        }

        dash_Consulta.setText("Consultas");
        dash_Relatorio.setText("Relatórios");
        //dash_Outros.setText("Outros");

        cardCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cliente = new Intent(MenuActivity.this, ClienteConsulta.class);
                cliente.putExtra("Nome", nomeFuncionario);
                cliente.putExtra("Cargo", cargoFuncionario);
                startActivity(cliente);
            }
        });

        cardFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fornecedor = new Intent(MenuActivity.this, FornecedorConsulta.class);
                fornecedor.putExtra("Nome", nomeFuncionario);
                fornecedor.putExtra("Cargo", cargoFuncionario);
                startActivity(fornecedor);
            }
        });

        cardEstoque.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent produto = new Intent(MenuActivity.this, ProdutoConsulta.class);
                produto.putExtra("Nome", nomeFuncionario);
                produto.putExtra("Cargo", cargoFuncionario);
                startActivity(produto);
            }
        });

        cardProducao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent producao = new Intent(MenuActivity.this, ProducaoConsulta.class);
                producao.putExtra("Nome", nomeFuncionario);
                producao.putExtra("Cargo", cargoFuncionario);
                startActivity(producao);
            }
        });

        cardVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent venda = new Intent(MenuActivity.this, VendaConsulta.class);
                venda.putExtra("Nome", nomeFuncionario);
                venda.putExtra("Cargo", cargoFuncionario);
                startActivity(venda);
            }
        });

        //------------------------------------------------------------------------------------------
        dash_Relatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent relatorio = new Intent(MenuActivity.this, RelatorioConsulta.class);
                relatorio.putExtra("Nome", nomeFuncionario);
                relatorio.putExtra("Cargo", cargoFuncionario);
                startActivity(relatorio);

            }
        });
    }
}
