package org.scandura.tgid.dto;

import java.math.BigDecimal;

public class TransacaoRequest {
    private String cpfCliente;
    private String cnpjEmpresa;
    private BigDecimal valor;
    private int tipoTransacao;

    public TransacaoRequest() {
    }

    public TransacaoRequest(String cpfCliente, String cnpjEmpresa, BigDecimal valor) {
        this.cpfCliente = cpfCliente;
        this.cnpjEmpresa = cnpjEmpresa;
        this.valor = valor;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public String getCnpjEmpresa() {
        return cnpjEmpresa;
    }

    public void setCnpjEmpresa(String cnpjEmpresa) {
        this.cnpjEmpresa = cnpjEmpresa;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public int getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(int tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }
}