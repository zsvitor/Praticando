package com.futstore.futstore.controller;

import java.util.Arrays;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.futstore.futstore.modelo.Cliente;
import com.futstore.futstore.modelo.Endereco;
import com.futstore.futstore.modelo.Genero;
import com.futstore.futstore.repository.ClienteRepository;
import com.futstore.futstore.service.ClienteService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ClienteRepository clienteRepository;

	@GetMapping("/cadastro")
	public String mostrarFormularioCadastro(Model model) {
		Cliente cliente = new Cliente();
		cliente.setEnderecoFaturamento(new Endereco());
		model.addAttribute("cliente", cliente);
		model.addAttribute("generos", Arrays.asList(Genero.values()));
		return "cliente/cadastro";
	}

	@PostMapping("/cadastro")
	public String cadastrarCliente(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model,
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "copiarEnderecoFaturamento", required = false) boolean copiarEnderecoFaturamento,
			@RequestParam(value = "confirmarSenha", required = true) String confirmarSenha) {
		if (clienteRepository.existsByCpf(cliente.getCpf())) {
			result.rejectValue("cpf", "cpf.existente", "CPF já cadastrado.");
		}
		if (clienteRepository.existsByGmail(cliente.getGmail())) {
			result.rejectValue("gmail", "gmail.existente", "Email já cadastrado.");
		}
		if (!cliente.getSenha().equals(confirmarSenha)) {
			result.reject("senha.naoConfere", "As senhas não correspondem.");
		}
		if (result.hasErrors()) {
			model.addAttribute("generos", Arrays.asList(Genero.values()));
			return "cliente/cadastro";
		}
		if (copiarEnderecoFaturamento) {
			Endereco enderecoEntrega = new Endereco();
			Endereco enderecoFaturamento = cliente.getEnderecoFaturamento();
			enderecoEntrega.setCep(enderecoFaturamento.getCep());
			enderecoEntrega.setLogradouro(enderecoFaturamento.getLogradouro());
			enderecoEntrega.setNumero(enderecoFaturamento.getNumero());
			enderecoEntrega.setComplemento(enderecoFaturamento.getComplemento());
			enderecoEntrega.setBairro(enderecoFaturamento.getBairro());
			enderecoEntrega.setCidade(enderecoFaturamento.getCidade());
			enderecoEntrega.setUf(enderecoFaturamento.getUf());
			enderecoEntrega.setDescricao("Principal");
			enderecoEntrega.setPrincipal(true);
			cliente.addEnderecoEntrega(enderecoEntrega);
		}
		clienteService.salvar(cliente);
		redirectAttributes.addFlashAttribute("mensagemSucesso",
				"Cadastro realizado com sucesso! Faça login para continuar.");
		return "redirect:/cliente/login";
	}

	@GetMapping("/login")
	public String mostrarLoginForm(@RequestParam(required = false) String error, Model model, HttpSession session) {
		if (session.getAttribute("clienteLogado") != null) {
			return "redirect:/";
		}
		if (error != null) {
			model.addAttribute("loginError", "Email ou senha inválidos");
		}
		return "cliente/login";
	}

	@PostMapping("/autenticar")
	public String autenticarCliente(@RequestParam String gmail, @RequestParam String senha, HttpSession session,
			RedirectAttributes redirectAttributes) {
		if (clienteService.autenticar(gmail, senha)) {
			Cliente cliente = clienteService.buscarPorGmail(gmail);
			session.setAttribute("clienteLogado", cliente);
			String redirectUrl = (String) session.getAttribute("redirectUrl");
			if (redirectUrl != null) {
				session.removeAttribute("redirectUrl");
				return "redirect:" + redirectUrl;
			}
			return "redirect:/";
		} else {
			redirectAttributes.addFlashAttribute("loginError", "Email ou senha inválidos");
			return "redirect:/cliente/login?error=true";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
		session.removeAttribute("clienteLogado");
		redirectAttributes.addFlashAttribute("mensagemSucesso", "Logout realizado com sucesso!");
		return "redirect:/";
	}

}