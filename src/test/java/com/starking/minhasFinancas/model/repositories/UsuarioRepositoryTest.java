package com.starking.minhasFinancas.model.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		Usuario usuario = Usuario.builder()
				.nome("pedro")
				.email("pedro@gmail.com").build();
		repository.save(usuario);

		boolean result = repository.existsByEmail("pedro@gmail.com");

		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoEmailCadastrado() {
		repository.deleteAll();
		
		boolean result = repository.existsByEmail("pedro@gmail.com");
		
		Assertions.assertThat(result).isFalse();	
	}
}
