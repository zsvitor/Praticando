package com.futstore.futstore.controller;

import com.futstore.futstore.modelo.Produto;
import com.futstore.futstore.repository.ProdutoRepository;
import com.futstore.futstore.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/novo")
    public String adicionarProduto(Model model) {
        model.addAttribute("produto", new Produto());
        return "/auth/administrador/admin-criar-produto";
    }

    @PostMapping("/salvar")
    public String salvarProduto(@Valid Produto produto, BindingResult result, 
                              @RequestParam(value = "file", required = false) MultipartFile imagem, 
                              RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            return "/auth/administrador/admin-criar-produto";
        }
        
        // Primeiro salva o produto para obter o ID
        Produto produtoSalvo = produtoService.salvar(produto);
        
        // Se uma imagem foi enviada, salva a imagem
        if (imagem != null && !imagem.isEmpty()) {
            produtoService.salvarImagem(produtoSalvo.getId(), imagem);
        }
        
        return "redirect:/produto/administrador/listar";
    }

    @GetMapping("/administrador/listar")
    public String listarProdutosAdmin(@RequestParam(required = false, defaultValue = "0") int page, 
                                     @RequestParam(required = false) String filtroNome, Model model) {
        Page<Produto> produtosPage = produtoService.listarProdutos(page, filtroNome); 
        model.addAttribute("produtos", produtosPage.getContent());
        model.addAttribute("paginaAtual", page);
        model.addAttribute("totalPaginas", produtosPage.getTotalPages());
        model.addAttribute("filtroNome", filtroNome);    
        return "/auth/administrador/admin-listar-produto";
    }

    @GetMapping("/administrador/trocar-status/{id}")
    public String toggleProdutoStatus(@PathVariable("id") Long id, RedirectAttributes attributes) {
    	produtoService.toggleStatus(id);
        return "redirect:/produto/administrador/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarProduto(@PathVariable("id") Long id, Model model) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        if (!produtoOptional.isPresent()) {
            throw new IllegalArgumentException("Produto inválido: " + id);
        }        
        model.addAttribute("produto", produtoOptional.get());
        return "/auth/administrador/admin-alterar-produto";
    }

    @PostMapping("/editar/{id}")
    public String editarProduto(@PathVariable("id") Long id, @Valid Produto produto, 
                              BindingResult result, 
                              @RequestParam(value = "file", required = false) MultipartFile imagem,
                              Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            produto.setId(id);
            return "/auth/administrador/admin-alterar-produto";
        }   
        
        produtoService.atualizar(produto, id);
        
        // Se uma nova imagem foi enviada, atualiza a imagem
        if (imagem != null && !imagem.isEmpty()) {
            produtoService.salvarImagem(id, imagem);
        }
        
        return "redirect:/produto/administrador/listar";
    }
    
    @PostMapping("/upload-imagem/{id}")
    public String uploadImagem(@PathVariable("id") Long id, 
                             @RequestParam("file") MultipartFile imagem,
                             RedirectAttributes attributes) {
        
        if (imagem.isEmpty()) {
            attributes.addFlashAttribute("erro", "Selecione uma imagem para upload");
            return "redirect:/produto/editar/" + id;
        }
        
        try {
            produtoService.salvarImagem(id, imagem);
            attributes.addFlashAttribute("mensagem", "Imagem carregada com sucesso");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao carregar imagem: " + e.getMessage());
        }
        
        return "redirect:/produto/editar/" + id;
    }
    
    @GetMapping("/estoquista/listar")
    public String listarProdutosEstoq(@RequestParam(required = false, defaultValue = "0") int page, 
                                     @RequestParam(required = false) String filtroNome, Model model) {
        Page<Produto> produtosPage = produtoService.listarProdutos(page, filtroNome); 
        model.addAttribute("produtos", produtosPage.getContent());
        model.addAttribute("paginaAtual", page);
        model.addAttribute("totalPaginas", produtosPage.getTotalPages());
        model.addAttribute("filtroNome", filtroNome);    
        return "/auth/estoquista/estoq-listar-produto";
    }
    
    @GetMapping("/estoquista/editar-estoque/{id}")
    public String editarEstoque(@PathVariable("id") Long id, Model model) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        if (!produtoOptional.isPresent()) {
            throw new IllegalArgumentException("Produto inválido: " + id);
        }        
        model.addAttribute("produto", produtoOptional.get());
        return "/auth/estoquista/estoq-alterar-produto";
    }

    @PostMapping("/estoquista/atualizar-estoque/{id}")
    public String atualizarEstoque(@PathVariable("id") Long id, @Valid Produto produto, 
                                  BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            produto.setId(id);
            return "/auth/estoquista/estoq-alterar-produto";
        }
        Optional<Produto> produtoAtual = produtoRepository.findById(id);
        if (produtoAtual.isPresent()) {
            Produto produtoExistente = produtoAtual.get();
            produtoExistente.setQuantidadeEstoque(produto.getQuantidadeEstoque());
            produtoRepository.save(produtoExistente);
        }      
        return "redirect:/produto/estoquista/listar";
    }
}