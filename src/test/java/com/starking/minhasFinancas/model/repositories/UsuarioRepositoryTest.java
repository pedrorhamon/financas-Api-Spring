package com.starking.minhasFinancas.model.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.starking.minhasFinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;

	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);

		boolean result = repository.existsByEmail("pedro@gmail.com");

		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoEmailCadastrado() {
		
		boolean result = repository.existsByEmail("pedro@gmail.com");
		
		Assertions.assertThat(result).isFalse();	
	}
	
	@Test
	public void devePersistirUmUsuarionaBase() {
		Usuario usuario = criarUsuario();
		Usuario usuarioSalvar = repository.save(usuario);
		
		Assertions.assertThat(usuarioSalvar.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUsuarioEmail() {
		Usuario usuario = criarUsuario();
		
		entityManager.persist(usuario);
		
		Optional<Usuario> result = repository.findByEmail("pedro@gmail.com");
		
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioEmail() {
		
		Optional<Usuario> result = repository.findByEmail("pedro@gmail.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	public static Usuario criarUsuario() {
		return Usuario 
				.builder()
				.nome("pedro")
				.email("pedro@gmail.com")
				.senha("123456")
				.build();
	}
}
