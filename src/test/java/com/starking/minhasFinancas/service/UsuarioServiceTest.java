package com.starking.minhasFinancas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.starking.minhasFinancas.model.entity.Usuario;
import com.starking.minhasFinancas.model.repositories.UsuarioRepository;
import com.starking.minhasFinancas.model.service.UsuarioService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@Autowired
	UsuarioService service;
	
	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void deveValidarEmail() {
		repository.deleteAll();
		
		service.validarEmail("pedro@gmail.com");	
	}
	
	@Test
	public void deveLancarErro() {
		Usuario usuario = Usuario.builder().nome("pedro").email("pedro@gmail.com").build();
		repository.save(usuario);
		
		service.validarEmail("pedro@gmail.com");
	}
}
