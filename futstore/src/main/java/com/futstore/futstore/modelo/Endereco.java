package com.futstore.futstore.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Endereco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O CEP deve ser informado")
	@Pattern(regexp = "^\\d{5}-\\d{3}$|^\\d{8}$", message = "Formato de CEP inválido")
	private String cep;

	@NotBlank(message = "O logradouro deve ser informado")
	private String logradouro;

	@NotNull(message = "O número deve ser informado")
	private String numero;

	private String complemento;

	@NotBlank(message = "O bairro deve ser informado")
	private String bairro;

	@NotBlank(message = "A cidade deve ser informada")
	private String cidade;

	@NotBlank(message = "A UF deve ser informada")
	@Size(min = 2, max = 2, message = "UF deve ter 2 caracteres")
	private String uf;

	private String nome;

	private boolean principal = false;

	// Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
	
}