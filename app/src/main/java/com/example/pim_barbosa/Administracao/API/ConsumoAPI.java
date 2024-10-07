package com.example.pim_barbosa.Administracao.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class ConsumoAPI {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://cep.awesomeapi.com.br/json/")
            .build();
    Call<List> call = IConverteDados.getPosts();
}
