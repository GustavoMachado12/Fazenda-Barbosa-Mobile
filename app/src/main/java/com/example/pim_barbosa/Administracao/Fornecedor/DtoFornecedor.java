package com.example.pim_barbosa.Administracao.Fornecedor;

public class DtoFornecedor {

    private int id;
    private String nome;
    private String documento;
    private String telefone;
    private String email;
    private String endereco;

    //ENDEREÇO
    private String cep;
    private String logradouro;
    private String municipio;
    private String uf;
    private String complemento;
    private String bairro;

    public DtoFornecedor() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getComplemento() {
        return complemento;
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
