package com.example.pim_barbosa.Administracao.Producao;

import java.util.Date;

public class DtoProducao {

    private int id;
    private String nmProduto;
    private String tipoCategoria;
    private int qtProduto;
    private Date dtPlantio;
    private Date dtColheita;
    private String StatusColheita;
    private float vlColheita;

    public DtoProducao() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNmProduto() {
        return nmProduto;
    }

    public void setNmProduto(String nmProduto) {
        this.nmProduto = nmProduto;
    }

    public String getTipoCategoria() {
        return tipoCategoria;
    }

    public void setTipoCategoria(String tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

    public int getQtProduto() {
        return qtProduto;
    }

    public void setQtProduto(int qtProduto) {
        this.qtProduto = qtProduto;
    }

    public Date getDtPlantio() {
        return dtPlantio;
    }

    public void setDtPlantio(Date dtPlantio) {
        this.dtPlantio = dtPlantio;
    }

    public Date getDtColheita() {
        return dtColheita;
    }

    public void setDtColheita(Date dtColheita) {
        this.dtColheita = dtColheita;
    }

    public String getStatusColheita() {
        return StatusColheita;
    }

    public void setStatusColheita(String statusColheita) {
        StatusColheita = statusColheita;
    }

    public float getVlColheita() {
        return vlColheita;
    }

    public void setVlColheita(float vlColheita) {
        this.vlColheita = vlColheita;
    }
}
