package com.futstore.futstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.futstore.futstore.modelo.Usuario;
import com.futstore.futstore.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario salvar(Usuario usuario) {
        // Criptografa a senha antes de salvar no banco
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }
    
    public Usuario atualizar(Usuario usuario, Long id) {
        Usuario usuarioBanco = usuarioRepository.findById(id).orElseThrow(() -> 
            new IllegalArgumentException("Usuário inválido: " + id));
        
        usuarioBanco.setNome(usuario.getNome());
        usuarioBanco.setCpf(usuario.getCpf());
        usuarioBanco.setPapel(usuario.getPapel());
        
        // Se uma nova senha for fornecida, criptografá-la
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuarioBanco.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        
        return usuarioRepository.save(usuarioBanco);
    }
}