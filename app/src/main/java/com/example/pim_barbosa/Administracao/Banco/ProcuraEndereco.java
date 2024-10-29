package com.example.pim_barbosa.Administracao.Banco;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pim_barbosa.Administracao.Cliente.DtoCliente;
import com.example.pim_barbosa.Administracao.Fornecedor.DtoFornecedor;
import com.example.pim_barbosa.R;

import org.json.JSONObject;

public class ProcuraEndereco {

    public void pesquisaCEP(String cep, Object dto, Context context) {
        String url = "https://cep.awesomeapi.com.br/json/" + cep;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            String address = response.getString("address");
                            String state = response.getString("state");
                            String city = response.getString("city");
                            String district = response.getString("district");

                            if (dto instanceof DtoCliente) { //CASO O DTO SEJA DO CLIENTE
                                DtoCliente clienteDTO = (DtoCliente) dto;
                                clienteDTO.setCep(cep);
                                clienteDTO.setLogradouro(address);
                                clienteDTO.setUf(state);
                                clienteDTO.setMunicipio(city);
                                clienteDTO.setBairro(district);

                                TextView txtLogradouro = ((Activity) context).findViewById(R.id.cliente_alterar_endereco);
                                txtLogradouro.setText(address);
                                TextView txtMunicipio = ((Activity) context).findViewById(R.id.cliente_alterar_municipio);
                                txtMunicipio.setText(city);
                                TextView txtEstado = ((Activity) context).findViewById(R.id.cliente_alterar_estado);
                                txtEstado.setText(state);
                                TextView txtBairro = ((Activity) context).findViewById(R.id.cliente_alterar_bairro);
                                txtBairro.setText(district);
                            }
                            else if(dto instanceof DtoFornecedor){ // SE FOR DO FORNECEDOR
                                DtoFornecedor fornecedorDTO = (DtoFornecedor) dto;
                                fornecedorDTO.setCep(cep);
                                fornecedorDTO.setLogradouro(address);
                                fornecedorDTO.setUf(state);
                                fornecedorDTO.setMunicipio(city);
                                fornecedorDTO.setBairro(district);

                                TextView txtLogradouro = ((Activity) context).findViewById(R.id.fornecedor_alterar_endereco);
                                txtLogradouro.setText(address);
                                TextView txtMunicipio = ((Activity) context).findViewById(R.id.fornecedor_alterar_municipio);
                                txtMunicipio.setText(city);
                                TextView txtEstado = ((Activity) context).findViewById(R.id.fornecedor_alterar_estado);
                                txtEstado.setText(state);
                                TextView txtBairro = ((Activity) context).findViewById(R.id.fornecedor_alterar_bairro);
                                txtBairro.setText(district);

                            }

                        }catch (Exception e) {
                            Toast.makeText(context, "Erro ao consultar o CEP", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erro ao consultar o CEP", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        );

        Volley.newRequestQueue(context).add(request);
    }



}
