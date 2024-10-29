package com.example.pim_barbosa.Administracao.Venda;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pim_barbosa.Conexao.ConSQL;
import com.example.pim_barbosa.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendaConsulta extends AppCompatActivity {
    Connection connection;
    ConSQL bd = new ConSQL();

    List<DtoVenda> listaVendaDTO;
    List<DtoVenda> listavendaFiltradoDTO;
    DtoVenda venda = new DtoVenda();

    private TextView textNome, textCargo;
    private EditText txtBusca;

    private ListView listView;
    SimpleAdapter ad;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_consulta);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );


        textNome = findViewById(R.id.textNomeDash);
        textCargo = findViewById(R.id.textCargoDash);

        txtBusca = findViewById(R.id.editTextBusca);

        textNome.setText(getIntent().getStringExtra("Nome"));
        textCargo.setText(getIntent().getStringExtra("Cargo"));

        //LIST VIEW
        listView = findViewById(R.id.listaVenda);
        listaVendaDTO = new ArrayList<>();
        listavendaFiltradoDTO = new ArrayList<>();


        txtBusca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                listarVenda();
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    listarVenda();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String nome = txtBusca.getText().toString().trim();
                if (!nome.isEmpty()) {
                    pesquisaFiltrado(nome);
                } else {
                    listarVenda();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                DtoVenda produtoSelecionado = listavendaFiltradoDTO.get(position);
                venda = produtoSelecionado;

                registerForContextMenu(listView);
                return false;
            }
        });

        listarVenda();
    }

    //METODOS

    public void listarVenda() {
        connection = bd.con();
        if (connection == null) {
            Toast.makeText(VendaConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String query = "EXEC pVendaDetalhes";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            List<Map<String, String>> listaMapVenda = new ArrayList<>();

            while (rs.next()) {
                DtoVenda venda = new DtoVenda();
                venda.setId(rs.getInt("Codigo da Venda"));
                venda.setIdCliente(rs.getInt("Codigo do Cliente"));
                venda.setNmCliente(rs.getString("Cliente"));
                venda.setNmProduto(rs.getString("Produto"));
                venda.setQtProduto(rs.getInt("Quantidade"));
                venda.setDtVenda(rs.getDate("Data do Pedido"));
                venda.setVlUnitario(rs.getFloat("Valor Unitario"));
                venda.setVlTotal(rs.getFloat("Valor Total"));
                listaVendaDTO.add(venda);

                Map<String, String> vendaMap = new HashMap<>();
                vendaMap.put("venda_ID", String.valueOf(venda.getId()));
                vendaMap.put("venda_ID_Cliente", String.valueOf(venda.getIdCliente()));
                vendaMap.put("venda_Nome_Cliente", venda.getNmCliente());
                vendaMap.put("venda_Nome_Produto", venda.getNmProduto());
                vendaMap.put("venda_Quantidade", String.valueOf(venda.getQtProduto()));
                vendaMap.put("venda_Data_Pedido", String.valueOf(venda.getDtVenda()));
                vendaMap.put("venda_Valor_Unitario", String.valueOf(venda.getVlUnitario()));
                vendaMap.put("venda_Valor_Total", String.valueOf(venda.getVlTotal()));
                listaMapVenda.add(vendaMap);
            }

            listavendaFiltradoDTO.clear();
            listavendaFiltradoDTO.addAll(listaVendaDTO);

            String[] Fromw = {"venda_ID", "venda_Nome_Cliente", "venda_Nome_Produto", "venda_Quantidade", "venda_Data_Pedido",
                    "venda_Valor_Unitario", "venda_Valor_Total"};
            int[] Tow = {R.id.Venda_ID, R.id.Venda_Cliente, R.id.Venda_Produto, R.id.Venda_Quantidade, R.id.Venda_Data_Venda,R.id.Venda_Valor_Unitario_Produto, R.id.Venda_Valor_Total_Produto};

            ad = new SimpleAdapter(VendaConsulta.this, listaMapVenda, R.layout.list_view_layout_venda, Fromw, Tow);
            listView.setAdapter(ad);

        } catch (SQLException e) {
            Toast.makeText(VendaConsulta.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void pesquisaFiltrado(String nome) {
        connection = bd.con();
        if (connection == null) {
            Toast.makeText(VendaConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String query = "EXEC pVendaDetalhesFiltrado @Nome = '" + nome + "'";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            List<Map<String, String>> listaMapproduto = new ArrayList<>();

            listavendaFiltradoDTO.clear();

            while (rs.next()) {
                DtoVenda venda = new DtoVenda();
                venda.setId(rs.getInt("Codigo da Venda"));
                venda.setIdCliente(rs.getInt("Codigo do Cliente"));
                venda.setNmCliente(rs.getString("Cliente"));
                venda.setNmProduto(rs.getString("Produto"));
                venda.setQtProduto(rs.getInt("Quantidade"));
                venda.setDtVenda(rs.getDate("Data do Pedido"));
                venda.setVlUnitario(rs.getFloat("Valor Unitario"));
                venda.setVlTotal(rs.getFloat("Valor Total"));
                listavendaFiltradoDTO.add(venda);

                Map<String, String> vendaMap = new HashMap<>();
                vendaMap.put("venda_ID", String.valueOf(venda.getId()));
                vendaMap.put("venda_ID_Cliente", String.valueOf(venda.getIdCliente()));
                vendaMap.put("venda_Nome_Cliente", venda.getNmCliente());
                vendaMap.put("venda_Nome_Produto", venda.getNmProduto());
                vendaMap.put("venda_Quantidade", String.valueOf(venda.getQtProduto()));
                vendaMap.put("venda_Data_Pedido", String.valueOf(venda.getDtVenda()));
                vendaMap.put("venda_Valor_Unitario", String.valueOf(venda.getVlUnitario()));
                vendaMap.put("venda_Valor_Total", String.valueOf(venda.getVlTotal()));
                listaMapproduto.add(vendaMap);
            }

            String[] Fromw = {"venda_ID", "venda_Nome_Cliente", "venda_Nome_Produto", "venda_Quantidade", "venda_Data_Pedido",
                    "venda_Valor_Unitario", "venda_Valor_Total"};
            int[] Tow = {R.id.Venda_ID, R.id.Venda_Cliente, R.id.Venda_Produto, R.id.Venda_Quantidade, R.id.Venda_Data_Venda,R.id.Venda_Valor_Unitario_Produto, R.id.Venda_Valor_Total_Produto};

            ad = new SimpleAdapter(VendaConsulta.this, listaMapproduto, R.layout.list_view_layout_produto, Fromw, Tow);
            listView.setAdapter(ad);

        } catch (SQLException e) {
            Toast.makeText(VendaConsulta.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0,0,0, "Alterar");
        menu.add(0,1,1, "Deletar");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == 0){
            try {
                SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Intent altera = new Intent(VendaConsulta.this, VendaAltera.class);
                altera.putExtra("NomeDash", textNome.getText().toString());
                altera.putExtra("CargoDash", textCargo.getText().toString());

                altera.putExtra("ID", venda.getId());
                altera.putExtra("IdCliente", venda.getIdCliente());
                altera.putExtra("Cliente", venda.getNmCliente());
                altera.putExtra("Produto", venda.getNmProduto());
                altera.putExtra("Quantidade", venda.getQtProduto());
                String dtVendaFormatted = sqlDateFormat.format(venda.getDtVenda());
                altera.putExtra("Data", dtVendaFormatted);
//                altera.putExtra("ValorUnitario", venda.getVlUnitario());
//                altera.putExtra("ValorTotal", venda.getVlTotal());
                startActivity(altera);
            } catch (Exception e){
                e.printStackTrace();
            }


        } else if(item.getItemId() == 1){
            excluiDados(venda);
        }

        return super.onContextItemSelected(item);
    }

    public void excluiDados(DtoVenda venda){
        AlertDialog.Builder msg = new AlertDialog.Builder(VendaConsulta.this);
        msg.setMessage("Deseja excluir?");
        msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                connection = bd.con();
                if (connection == null) {
                    Toast.makeText(VendaConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
                    return;
                }

                try{
                    String query = "EXEC pDeletaVenda " +
                            "@cdVenda = " + venda.getId() + ";";
                    Statement st = connection.createStatement();
                    boolean rs = st.execute(query);

                    if(!rs){
                        Toast.makeText(VendaConsulta.this, "Excluido com sucesso", Toast.LENGTH_SHORT).show();
                        listarVenda();
                    } else {
                        Toast.makeText(VendaConsulta.this, "Erro ao excluir", Toast.LENGTH_SHORT).show();
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


            }
        });

        msg.setNegativeButton("Não", null);
        msg.show();
    }
}











































