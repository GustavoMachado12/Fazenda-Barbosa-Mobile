package com.example.pim_barbosa.Administracao.Venda;

import java.util.Date;

public class DtoVenda {

    private int id;
    private int idCliente;
    private String nmCliente;
    private Date dtVenda;
    private String nmProduto;
    private float vlUnitario;
    private float vlTotal;
    private int qtProduto;

    public DtoVenda(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNmCliente() {
        return nmCliente;
    }

    public void setNmCliente(String nmCliente) {
        this.nmCliente = nmCliente;
    }

    public Date getDtVenda() {
        return dtVenda;
    }

    public void setDtVenda(Date dtVenda) {
        this.dtVenda = dtVenda;
    }

    public String getNmProduto() {
        return nmProduto;
    }

    public void setNmProduto(String nmProduto) {
        this.nmProduto = nmProduto;
    }

    public float getVlUnitario() {
        return vlUnitario;
    }

    public void setVlUnitario(float vlUnitario) {
        this.vlUnitario = vlUnitario;
    }

    public float getVlTotal() {
        return vlTotal;
    }

    public void setVlTotal(float vlTotal) {
        this.vlTotal = vlTotal;
    }

    public int getQtProduto() {
        return qtProduto;
    }

    public void setQtProduto(int qtProduto) {
        this.qtProduto = qtProduto;
    }
}
