package com.futstore.futstore.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.futstore.futstore.modelo.Cliente;
import com.futstore.futstore.modelo.Endereco;
import com.futstore.futstore.modelo.Genero;
import com.futstore.futstore.service.ClienteService;
import java.util.Arrays;
import java.util.Map;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping("/cadastro")
	public String exibirFormularioCadastro(Model model) {
		model.addAttribute("cliente", new Cliente());
		model.addAttribute("generos", Arrays.asList(Genero.values()));
		model.addAttribute("endereco", new Endereco());
		return "cliente/cadastro";
	}

	@PostMapping("/cadastro")
	public String cadastrarCliente(@Valid Cliente cliente, BindingResult result, @Valid Endereco endereco,
			BindingResult enderecoResult,
			@RequestParam(value = "usarMesmoEndereco", required = false) boolean usarMesmoEndereco, Model model,
			RedirectAttributes attributes) {
		if (clienteService.existeCpf(cliente.getCpf())) {
			result.rejectValue("cpf", "cpf.existente", "CPF já cadastrado.");
		}
		if (clienteService.existeGmail(cliente.getGmail())) {
			result.rejectValue("gmail", "gmail.existente", "Gmail já cadastrado.");
		}
		if (result.hasErrors() || enderecoResult.hasErrors()) {
			model.addAttribute("generos", Arrays.asList(Genero.values()));
			return "cliente/cadastro";
		}
		cliente.setEnderecoFaturamento(endereco);
		clienteService.salvar(cliente);
		attributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso! Faça login para continuar.");
		return "redirect:/cliente/login";
	}

	@GetMapping("/buscar-cep/{cep}")
	@ResponseBody
	public Map<String, Object> buscarCep(@PathVariable String cep) {
		RestTemplate restTemplate = new RestTemplate();
		cep = cep.replaceAll("[^0-9]", "");
		return restTemplate.getForObject("https://viacep.com.br/ws/" + cep + "/json/", Map.class);
	}

	@GetMapping("/login")
	public String exibirFormularioLogin() {
		return "cliente/login";
	}

	@PostMapping("/login")
	public String realizarLogin(@RequestParam String gmail, @RequestParam String senha, HttpSession session,
			RedirectAttributes attributes) {
		attributes.addFlashAttribute("erro", "Email ou senha inválidos.");
		return "redirect:/cliente/login";
	}

}