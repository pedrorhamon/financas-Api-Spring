package com.starking.minhasFinancas.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.minhasFinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	boolean existsByEmail(String email);
}
