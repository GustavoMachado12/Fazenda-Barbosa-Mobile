package com.example.pim_barbosa.Administracao.Producao;

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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pim_barbosa.Administracao.Cliente.ClienteAltera;
import com.example.pim_barbosa.Administracao.Cliente.ClienteConsulta;
import com.example.pim_barbosa.Administracao.Cliente.DtoCliente;
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

public class ProducaoConsulta extends AppCompatActivity {

    Connection connection;
    ConSQL bd = new ConSQL();

    List<DtoProducao> listaProducaoDTO;
    List<DtoProducao> listaProducaoFiltradoDTO;
    DtoProducao producao = new DtoProducao();

    private TextView textNome, textCargo;
    private EditText txtBuscaPorNome;

    //ListGrid
    private ListView listView;
    SimpleAdapter ad;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producao_consulta);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        textNome = findViewById(R.id.textNomeDash);
        textCargo = findViewById(R.id.textCargoDash);

        textNome.setText(getIntent().getStringExtra("Nome"));
        textCargo.setText(getIntent().getStringExtra("Cargo"));

        txtBuscaPorNome = findViewById(R.id.editTextBusca);

        //LIST VIEW
        listView = findViewById(R.id.listaProducao);
        listaProducaoDTO = new ArrayList<>();
        listaProducaoFiltradoDTO = new ArrayList<>();

        listarProducao();

        txtBuscaPorNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                listarProducao();
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    listarProducao();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String nome = txtBuscaPorNome.getText().toString().trim();
                if (!nome.isEmpty()) {
                    pesquisaPorNome(nome);
                } else {
                    listarProducao();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                DtoProducao producaoSelecionado = listaProducaoFiltradoDTO.get(position);
                producao = producaoSelecionado;

                registerForContextMenu(listView);
                return false;
            }
        });

    }

    //METODOS
    public void listarProducao() {

        connection = bd.con();
        if (connection == null) {
            Toast.makeText(ProducaoConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String query = "EXEC pSelectProducao";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            List<Map<String, String>> listaMapproducao = new ArrayList<>();

            listaProducaoDTO.clear();

            while (rs.next()) {
                DtoProducao producao = new DtoProducao();
                producao.setId(rs.getInt("Codigo"));
                producao.setNmProduto(rs.getString("Produto"));
                producao.setDtPlantio(rs.getDate("Data de Plantio"));
                producao.setDtColheita(rs.getDate("Data de Colheita"));
                producao.setStatusColheita(rs.getString("Status"));
                producao.setVlColheita(rs.getFloat("Valor Lote"));
                listaProducaoDTO.add(producao);

                Map<String, String> producaoMap = new HashMap<>();
                producaoMap.put("producao_ID", String.valueOf(producao.getId()));
                producaoMap.put("producao_Produto", producao.getNmProduto());
                producaoMap.put("producao_dtPlantio", String.valueOf(producao.getDtPlantio()));
                producaoMap.put("producao_dtColheita", String.valueOf(producao.getDtColheita()));
                producaoMap.put("producao_Status", producao.getStatusColheita());
                producaoMap.put("producao_Valor", String.valueOf(producao.getVlColheita()));
                listaMapproducao.add(producaoMap);
            }

            listaProducaoFiltradoDTO.clear();
            listaProducaoFiltradoDTO.addAll(listaProducaoDTO);

            String[] Fromw = {"producao_ID", "producao_Produto", "producao_dtPlantio", "producao_dtColheita", "producao_Status", "producao_Valor"};
            int[] Tow = {R.id.Producao_ID, R.id.Producao_Nome_Produto, R.id.Producao_Dt_Plantio, R.id.Producao_Dt_Colheita, R.id.Producao_Status, R.id.Producao_Valor_Lote};

            ad = new SimpleAdapter(ProducaoConsulta.this, listaMapproducao, R.layout.list_view_layout_producao, Fromw, Tow);
            listView.setAdapter(ad);

        } catch (SQLException e) {
            Toast.makeText(ProducaoConsulta.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void pesquisaPorNome(String nome) {
        connection = bd.con();
        if (connection == null) {
            Toast.makeText(ProducaoConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String query = "EXEC pSelectProducaoFiltrado @nmProduto = '" + nome + "'";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            List<Map<String, String>> listaMapProducao = new ArrayList<>();

            listaProducaoFiltradoDTO.clear();

            while (rs.next()) {
                DtoProducao producao = new DtoProducao();
                producao.setId(rs.getInt("Codigo"));
                producao.setNmProduto(rs.getString("Produto"));
                producao.setDtPlantio(rs.getDate("Data de Plantio"));
                producao.setDtColheita(rs.getDate("Data de Colheita"));
                producao.setStatusColheita(rs.getString("Status"));
                producao.setVlColheita(rs.getFloat("Valor Lote"));
                listaProducaoDTO.add(producao);

                Map<String, String> producaoMap = new HashMap<>();
                producaoMap.put("producao_ID", String.valueOf(producao.getId()));
                producaoMap.put("producao_Produto", producao.getNmProduto());
                producaoMap.put("producao_dtPlantio", String.valueOf(producao.getDtPlantio()));
                producaoMap.put("producao_dtColheita", String.valueOf(producao.getDtColheita()));
                producaoMap.put("producao_Status", producao.getStatusColheita());
                producaoMap.put("producao_Valor", String.valueOf(producao.getVlColheita()));
                listaMapProducao.add(producaoMap);
            }

            String[] Fromw = {"producao_ID", "producao_Produto", "producao_dtPlantio", "producao_dtColheita", "producao_Status", "producao_Valor"};
            int[] Tow = {R.id.Producao_ID, R.id.Producao_Nome_Produto, R.id.Producao_Dt_Plantio, R.id.Producao_Dt_Colheita, R.id.Producao_Status, R.id.Producao_Valor_Lote};

            ad = new SimpleAdapter(ProducaoConsulta.this, listaMapProducao, R.layout.list_view_layout_producao, Fromw, Tow);
            listView.setAdapter(ad);

        } catch (SQLException e) {
            Toast.makeText(ProducaoConsulta.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(ProducaoConsulta.this, ProducaoAltera.class);
            intent.putExtra("NomeDash", textNome.getText().toString());
            intent.putExtra("CargoDash", textCargo.getText().toString());

            intent.putExtra("ID", producao.getId());
            intent.putExtra("Produto", producao.getNmProduto());
            SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dtPlantioFormatted = sqlDateFormat.format(producao.getDtPlantio());
            intent.putExtra("dtPlantio", dtPlantioFormatted);
            //intent.putExtra("dtPlantio", producao.getDtPlantio());
            String dtColheitaFormatted = sqlDateFormat.format(producao.getDtColheita());
            intent.putExtra("dtColheita", dtColheitaFormatted);
            //intent.putExtra("dtColheita", producao.getDtColheita());
            intent.putExtra("Status", producao.getStatusColheita());
            intent.putExtra("Valor", producao.getVlColheita());
            startActivity(intent);

        } else if(item.getItemId() == 1){
            excluiDados(producao);
        }

        return super.onContextItemSelected(item);
    }

    public void excluiDados(DtoProducao producao){
        AlertDialog.Builder msg = new AlertDialog.Builder(ProducaoConsulta.this);
        msg.setMessage("Deseja excluir?");
        msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                connection = bd.con();
                if (connection == null) {
                    Toast.makeText(ProducaoConsulta.this, "Erro ao conectar", Toast.LENGTH_SHORT).show();
                    return;
                }

                try{
                    String query = "DELETE FROM tb_Producao WHERE tb_Producao = " +
                            producao.getId();
                    Statement st = connection.createStatement();
                    boolean rs = st.execute(query);

                    if(!rs){
                        Toast.makeText(ProducaoConsulta.this, "Excluido com sucesso", Toast.LENGTH_SHORT).show();
                        listarProducao();
                    } else {
                        Toast.makeText(ProducaoConsulta.this, "Erro ao excluir", Toast.LENGTH_SHORT).show();
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
