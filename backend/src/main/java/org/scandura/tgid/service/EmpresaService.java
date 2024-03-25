package org.scandura.tgid.service;

import org.scandura.tgid.entity.Empresa;
import org.scandura.tgid.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Empresa> listarEmpresas() {
        return empresaRepository.findAll();
    }

    public Empresa cadastrarEmpresa(Empresa empresa) {
        if (empresaRepository.findByCnpj(empresa.getCnpj()).isPresent()) {
            throw new RuntimeException("Empresa já cadastrada com o CNPJ: " + empresa.getCnpj());
        }

        return empresaRepository.save(empresa);
    }

    public Optional<Empresa> getEmpresaById(Long id) {
        return empresaRepository.findById(id);
    }

    public Empresa atualizarEmpresa(Long id, Empresa empresa) {
        Optional<Empresa> empresaExistente = empresaRepository.findById(id);

        if (empresaExistente.isPresent()) {
            Empresa empresaAtualizada = empresaExistente.get();
            if (empresa.getNome() != null) {
                empresaAtualizada.setNome(empresa.getNome());
            }
            if (empresa.getCnpj() != null) {
                empresaAtualizada.setCnpj(empresa.getCnpj());
            }
            if (empresa.getSaldo() != null) {
                empresaAtualizada.setSaldo(empresa.getSaldo());
            }
            if (empresa.getTaxaAdministrativa() != null) {
                empresaAtualizada.setTaxaAdministrativa(empresa.getTaxaAdministrativa());
            }
            return empresaRepository.save(empresaAtualizada);
        } else {
            throw new RuntimeException("Empresa não encontrada com o ID: " + id);
        }
    }

    public void deletarEmpresa(Long id) {
        Optional<Empresa> empresaExistente = empresaRepository.findById(id);

        if (empresaExistente.isPresent()) {
            empresaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Empresa não encontrada com o ID: " + id);
        }
    }
}
