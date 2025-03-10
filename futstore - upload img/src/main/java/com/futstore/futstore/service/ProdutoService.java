package com.futstore.futstore.service;

import com.futstore.futstore.modelo.Produto;
import com.futstore.futstore.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Page<Produto> listarProdutos(int pagina, String filtroNome) {
        PageRequest pageRequest = PageRequest.of(pagina, 10);      
        if (filtroNome != null && !filtroNome.isEmpty()) {
            return produtoRepository.findByNomeContainingIgnoreCaseOrderByIdDesc(filtroNome, pageRequest);
        }        
        return produtoRepository.findAllByOrderByIdDesc(pageRequest);
    }

    public Produto atualizar(Produto produto, Long id) {
        Produto produtoBanco = produtoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto inválido: " + id));       
        produtoBanco.setNome(produto.getNome());
        produtoBanco.setDescricao(produto.getDescricao());
        produtoBanco.setPreco(produto.getPreco());
        produtoBanco.setQuantidadeEstoque(produto.getQuantidadeEstoque());
        produtoBanco.setAvaliacao(produto.getAvaliacao());
        
        // Se houver uma nova imagem, atualiza o caminho
        if (produto.getImagem() != null && !produto.getImagem().isEmpty()) {
            produtoBanco.setImagem(produto.getImagem());
        }
        
        return produtoRepository.save(produtoBanco);
    }

    public void toggleStatus(Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto inválido: " + id));        
        produto.setAtivo(!produto.isAtivo());
        produtoRepository.save(produto);
    }
    
    public void salvarImagem(Long id, MultipartFile imagem) {
        if (!imagem.isEmpty()) {
            Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto inválido: " + id));
            
            String nomeImagem = imagem.getOriginalFilename();
            String caminhoRelativo = "uploads/" + nomeImagem;
            produto.setImagem(caminhoRelativo);
            produtoRepository.save(produto);

            try {
                // Caminho absoluto para salvar as imagens
                String caminhoAbsoluto = "src/main/resources/static/uploads";
                File diretorio = new File(caminhoAbsoluto);
                if (!diretorio.exists()) {
                    diretorio.mkdirs();
                }

                File arquivoDestino = new File(diretorio.getAbsolutePath() + File.separator + nomeImagem);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(arquivoDestino));
                
                stream.write(imagem.getBytes());
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage());
            }
        }
    }
}