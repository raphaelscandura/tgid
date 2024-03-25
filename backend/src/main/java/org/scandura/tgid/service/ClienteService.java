package org.scandura.tgid.service;

import org.scandura.tgid.entity.Cliente;
import org.scandura.tgid.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> getClienteById(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente cadastrarCliente(Cliente cliente) {
        if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new RuntimeException("Cliente já cadastrado com o CPF: " + cliente.getCpf());
        }

        return clienteRepository.save(cliente);
    }

    public Cliente atualizarCliente(Long id, Cliente cliente) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        if (clienteExistente.isPresent()) {
            Cliente clienteAtualizado = clienteExistente.get();
            if (cliente.getNome() != null) {
                clienteAtualizado.setNome(cliente.getNome());
            }
            if (cliente.getCpf() != null) {
                clienteAtualizado.setCpf(cliente.getCpf());
            }

            return clienteRepository.save(clienteAtualizado);
        } else {
            throw new RuntimeException("Cliente não encontrado com o ID: " + id);
        }
    }

    public void deletarCliente(Long id) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        if (clienteExistente.isPresent()) {
            clienteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cliente não encontrado com o ID: " + id);
        }
    }
}