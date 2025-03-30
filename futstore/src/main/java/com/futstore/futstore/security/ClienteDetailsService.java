package com.futstore.futstore.security;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.futstore.futstore.modelo.Cliente;
import com.futstore.futstore.repository.ClienteRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class ClienteDetailsService implements UserDetailsService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public UserDetails loadUserByUsername(String gmail) throws UsernameNotFoundException {
		Cliente cliente = clienteRepository.findByGmail(gmail)
				.orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado com o email: " + gmail));
		return new org.springframework.security.core.userdetails.User(cliente.getGmail(), cliente.getSenha(),
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE")));
	}

}