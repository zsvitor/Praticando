package com.brasileiraostore.brasileiraostore.controle;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import com.brasileiraostore.brasileiraostore.modelos.UsuarioAdmin;
import com.brasileiraostore.brasileiraostore.repositorios.UsuarioAdminRepositorio;

@Controller
public class UsuarioAdminControle {
	
	@Autowired
	private UsuarioAdminRepositorio usuarioRepositorio;
	
	@GetMapping("cadastroUsuarioAdmin")
	public ModelAndView cadastrar(UsuarioAdmin usuarioadmin) {
		ModelAndView mv = new ModelAndView("administrativo/administradores/cadastro");
		mv.addObject("usuarios_admin", usuarioadmin);
		return mv;
	}
	
	@GetMapping("/listarUsuarioAdmin")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("administrativo/administradores/lista");
		mv.addObject("listaUsuarios", usuarioRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/editarUsuarioAdmin/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<UsuarioAdmin> usuarioadmin = usuarioRepositorio.findById(id);
		return cadastrar(usuarioadmin.get());
	}
	
	@PostMapping("/salvarUsuarioAdmin")
	public ModelAndView salvar(UsuarioAdmin usuarioadmin, BindingResult result) {
		if(result.hasErrors()) {
			return cadastrar(usuarioadmin);
		}
		usuarioRepositorio.saveAndFlush(usuarioadmin);
		return cadastrar(new UsuarioAdmin());
	}
}
