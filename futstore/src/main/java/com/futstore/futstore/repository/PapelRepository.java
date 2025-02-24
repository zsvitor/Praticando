package com.futstore.futstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futstore.futstore.modelo.Papel;

public interface PapelRepository extends JpaRepository<Papel, Long>{
	Papel findByPapel(String papel);
}
