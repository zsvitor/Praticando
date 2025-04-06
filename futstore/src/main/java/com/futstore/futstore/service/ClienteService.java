package com.futstore.futstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.futstore.futstore.modelo.Cliente;
import com.futstore.futstore.modelo.Endereco;
import com.futstore.futstore.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public Cliente salvar(Cliente cliente) {
		cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
		if (cliente.getEnderecosEntrega().isEmpty()) {
			Endereco enderecoEntrega = new Endereco();
			copiarEndereco(cliente.getEnderecoFaturamento(), enderecoEntrega);
			enderecoEntrega.setDescricao("Principal");
			enderecoEntrega.setPrincipal(true);
			cliente.addEnderecoEntrega(enderecoEntrega);
		}
		return clienteRepository.save(cliente);
	}

	private void copiarEndereco(Endereco origem, Endereco destino) {
		destino.setCep(origem.getCep());
		destino.setLogradouro(origem.getLogradouro());
		destino.setNumero(origem.getNumero());
		destino.setComplemento(origem.getComplemento());
		destino.setBairro(origem.getBairro());
		destino.setCidade(origem.getCidade());
		destino.setUf(origem.getUf());
	}

	public boolean autenticar(String gmail, String senha) {
		return clienteRepository.findByGmail(gmail)
				.map(cliente -> cliente.isAtivo() && passwordEncoder.matches(senha, cliente.getSenha())).orElse(false);
	}

	public Cliente buscarPorGmail(String gmail) {
		return clienteRepository.findByGmail(gmail).orElse(null);
	}

}