package com.example.pim_barbosa.Administracao.API;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IConverteDados {

    @GET("posts")
    static Call<List> getPosts() {
        return null;
    }

}
