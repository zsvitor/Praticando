package com.futstore.futstore.controller;

import com.futstore.futstore.modelo.Carrinho;
import com.futstore.futstore.modelo.Cliente;
import com.futstore.futstore.modelo.Endereco;
import com.futstore.futstore.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping("/finalizar")
	public String finalizarCompra(HttpSession session, Model model) {
		Cliente clienteLogado = (Cliente) session.getAttribute("clienteLogado");
		if (clienteLogado == null) {
			session.setAttribute("redirectUrl", "/checkout/finalizar");
			return "redirect:/cliente/login";
		}
		Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
		if (carrinho == null || carrinho.isEmpty()) {
			model.addAttribute("erro", "Seu carrinho está vazio");
			return "redirect:/carrinho";
		}
		clienteLogado = clienteService.buscarPorId(clienteLogado.getId());
		List<Endereco> enderecosEntrega = clienteLogado.getEnderecosEntrega();
		if (enderecosEntrega == null || enderecosEntrega.isEmpty()) {
			model.addAttribute("erro", "Você precisa cadastrar um endereço de entrega");
			return "redirect:/cliente/perfil#enderecosEntrega";
		}
		model.addAttribute("cliente", clienteLogado);
		model.addAttribute("carrinho", carrinho);
		Endereco enderecoPrincipal = enderecosEntrega.stream().filter(Endereco::isPrincipal).findFirst()
				.orElse(enderecosEntrega.get(0));
		model.addAttribute("enderecoPrincipal", enderecoPrincipal);
		return "cliente/finalizar-compra";
	}

}