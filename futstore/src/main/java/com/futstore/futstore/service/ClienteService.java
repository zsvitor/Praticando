package com.futstore.futstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.futstore.futstore.modelo.Cliente;
import com.futstore.futstore.modelo.Endereco;
import com.futstore.futstore.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Cliente salvar(Cliente cliente) {
		cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
		if (cliente.getEnderecosEntrega().isEmpty() && cliente.getEnderecoFaturamento() != null) {
			Endereco enderecoEntrega = new Endereco();
			enderecoEntrega.setCep(cliente.getEnderecoFaturamento().getCep());
			enderecoEntrega.setLogradouro(cliente.getEnderecoFaturamento().getLogradouro());
			enderecoEntrega.setNumero(cliente.getEnderecoFaturamento().getNumero());
			enderecoEntrega.setComplemento(cliente.getEnderecoFaturamento().getComplemento());
			enderecoEntrega.setBairro(cliente.getEnderecoFaturamento().getBairro());
			enderecoEntrega.setCidade(cliente.getEnderecoFaturamento().getCidade());
			enderecoEntrega.setUf(cliente.getEnderecoFaturamento().getUf());
			enderecoEntrega.setNome("Endereço Principal");
			enderecoEntrega.setPrincipal(true);
			cliente.addEnderecoEntrega(enderecoEntrega);
		}
		return clienteRepository.save(cliente);
	}

	public boolean existeCpf(String cpf) {
		return clienteRepository.existsByCpf(cpf);
	}

	public boolean existeGmail(String gmail) {
		return clienteRepository.existsByGmail(gmail);
	}

}