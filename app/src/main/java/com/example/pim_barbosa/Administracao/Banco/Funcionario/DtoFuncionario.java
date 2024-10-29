package com.example.pim_barbosa.Administracao.Banco.Funcionario;

import java.io.Serializable;

public class DtoFuncionario implements Serializable {
    private String nome;
    private String cargo;
    private int nvl;

    public DtoFuncionario(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getNvl() {
        return nvl;
    }

    public void setNvl(int nvl) {
        this.nvl = nvl;
    }
}
