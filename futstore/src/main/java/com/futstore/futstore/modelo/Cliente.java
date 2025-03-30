package com.futstore.futstore.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ]{3,}\\s+[A-Za-zÀ-ÖØ-öø-ÿ]{3,}(\\s+[A-Za-zÀ-ÖØ-öø-ÿ]{1,})*$", message = "O nome deve ter pelo menos duas palavras com mínimo de 3 letras cada")
	private String nomeCompleto;

	@CPF(message = "CPF inválido")
	@Column(unique = true)
	private String cpf;

	@NotNull(message = "A data de nascimento deve ser informada")
	@Past(message = "A data de nascimento deve ser no passado")
	private LocalDate dataNascimento;

	@NotNull(message = "O gênero deve ser informado")
	@Enumerated(EnumType.STRING)
	private Genero genero;

	@Email(message = "Email inválido")
	@Column(unique = true)
	private String gmail;

	@NotEmpty(message = "A senha deve ser informada")
	@Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
	private String senha;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "endereco_faturamento_id")
	private Endereco enderecoFaturamento;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "cliente_id")
	private List<Endereco> enderecosEntrega = new ArrayList<>();

	private boolean ativo = true;

	// Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public String getGmail() {
		return gmail;
	}

	public void setGmail(String gmail) {
		this.gmail = gmail;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Endereco getEnderecoFaturamento() {
		return enderecoFaturamento;
	}

	public void setEnderecoFaturamento(Endereco enderecoFaturamento) {
		this.enderecoFaturamento = enderecoFaturamento;
	}

	public List<Endereco> getEnderecosEntrega() {
		return enderecosEntrega;
	}

	public void setEnderecosEntrega(List<Endereco> enderecosEntrega) {
		this.enderecosEntrega = enderecosEntrega;
	}

	public void addEnderecoEntrega(Endereco endereco) {
		this.enderecosEntrega.add(endereco);
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Endereco getEndereco() {
		return this.enderecoFaturamento;
	}

	public void setEndereco(Endereco endereco) {
		this.enderecoFaturamento = endereco;
	}

}