package org.scandura.tgid.service;

import org.scandura.tgid.entity.Cliente;
import org.scandura.tgid.entity.Empresa;
import org.scandura.tgid.entity.Transacao;
import org.scandura.tgid.repository.ClienteRepository;
import org.scandura.tgid.repository.EmpresaRepository;
import org.scandura.tgid.repository.TransacaoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransacaoService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Transactional
    public void realizarTransacao(String cpf, String cnpj, BigDecimal valor, int tipoTransacao) {
        BigDecimal novoSaldoEmpresa;
        if (!validarCpf(cpf)) {
            throw new RuntimeException("CPF inválido: " + cpf);
        }

        if (!validarCnpj(cnpj)) {
            throw new RuntimeException("CNPJ inválido: " + cnpj);
        }

        Cliente cliente = clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com CPF: " + cpf));

        Empresa empresa = empresaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com CNPJ: " + cnpj));

        BigDecimal valorComTaxa = aplicarTaxa(valor, empresa, tipoTransacao);

        //DEPÓSITO
        if(tipoTransacao==1){
            novoSaldoEmpresa = empresa.getSaldo().add(valorComTaxa);
        //SAQUE
        }else if(tipoTransacao==2){
            if (empresa.getSaldo().compareTo(valorComTaxa) < 0) {
                throw new RuntimeException("Empresa não possui saldo suficiente para realizar a transação");
            }else{
                novoSaldoEmpresa = empresa.getSaldo().subtract(valorComTaxa);
            }
        }else{
            throw new RuntimeException("Tipo de transação inválido: " + tipoTransacao);
        }

        empresa.setSaldo(novoSaldoEmpresa);
        empresaRepository.save(empresa);

        Transacao transacao = new Transacao();
        transacao.setCliente(cliente);
        transacao.setEmpresa(empresa);
        transacao.setValor(valor);
        transacao.setDataHora(LocalDateTime.now());

        transacaoRepository.save(transacao);
    }

    private static boolean validarCpf(String cpf) {
        CPFValidator cpfValidator = new CPFValidator();
        try{ cpfValidator.assertValid(cpf);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    private boolean validarCnpj(String cnpj) {
        CNPJValidator cnpjValidator = new CNPJValidator();
        try{ cnpjValidator.assertValid(cnpj);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private BigDecimal aplicarTaxa(BigDecimal valor, Empresa empresa, int transacao) {
        BigDecimal taxa = valor.multiply(empresa.getTaxaAdministrativa())
                .divide(BigDecimal.valueOf(100));

        if(transacao==1){
            return valor.subtract(taxa);
        }else{
            return valor.add(taxa);
        }
    }
}