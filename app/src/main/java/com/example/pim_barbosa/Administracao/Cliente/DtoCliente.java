package com.example.pim_barbosa.Administracao.Cliente;

public class DtoCliente {

    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String documento;
    private String endereco;

    //ENDEREÇO
    private String cep;
    private String logradouro;
    private String municipio;
    private String uf;
    private String complemento;
    private String bairro;


    public DtoCliente(int id, String nome, String email, String telefone, String documento, String endereco, String cep, String logradouro, String municipio, String uf, String complemento) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.documento = documento;
        this.endereco = endereco;
        this.cep = cep;
        this.logradouro = logradouro;
        this.municipio = municipio;
        this.uf = uf;
        this.complemento = complemento;
    }

    public DtoCliente() {

    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCep() {
        return cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getUf() {
        return uf;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }


    //ENDERECO
    public String getEnderecoCompleto() {
        return (cep != null ? cep + ", " : "") +
                (logradouro != null ? logradouro + ", " : "") +
                (bairro != null ? bairro + ", " : "") +
                (municipio != null ? municipio + ", " : "") +
                (uf != null ? uf + ", " : "") +
                (complemento != null ? complemento : "").trim();
    }
}
