package com.futstore.futstore.modelo;

public enum Genero {
	MASCULINO("Masculino"), FEMININO("Feminino"), NAO_INFORMAR("Não Informar");

	private String descricao;

	Genero(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}