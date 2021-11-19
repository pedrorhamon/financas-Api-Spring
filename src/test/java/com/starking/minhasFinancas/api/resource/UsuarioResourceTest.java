package com.starking.minhasFinancas.api.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starking.minhasFinancas.api.dto.UsuarioDto;
import com.starking.minhasFinancas.exception.ErroAutenticacao;
import com.starking.minhasFinancas.exception.RegraNegocioException;
import com.starking.minhasFinancas.model.entity.Usuario;
import com.starking.minhasFinancas.model.service.LancamentoService;
import com.starking.minhasFinancas.model.service.UsuarioService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class)
@AutoConfigureMockMvc
public class UsuarioResourceTest {

	static final String API = "/api/usuarios";
	static final MediaType JSON = MediaType.APPLICATION_JSON;

	@Autowired
	MockMvc mvc;

	@MockBean
	UsuarioService usuarioService;

	@MockBean
	LancamentoService lancamentoService;

	@Test
	public void deveAutenticarUsuario() throws Exception {
		String email = "usuario@email.com";
		String senha = "123";

		UsuarioDto dto = UsuarioDto.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
		Mockito.when(usuarioService.autenticar(email, senha)).thenReturn(usuario);
		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API.concat("/autenticar"))
				.accept(JSON)
				.contentType(JSON)
				.content(json);

			mvc
				.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
				.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
				.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
	}
	
	@Test
	public void deveRetornarBadRequestErroDeAutenticacao() throws Exception {
		String email = "usuario@email.com";
		String senha = "123";

		UsuarioDto dto = UsuarioDto.builder().email(email).senha(senha).build();
		Mockito.when(usuarioService.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);

		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API.concat("/autenticar"))
				.accept(JSON)
				.contentType(JSON)
				.content(json);

		mvc
		.perform(request)
		.andExpect(MockMvcResultMatchers.status()
				.isBadRequest());
		;
	}
	
	@Test
	public void deveCriarUsuario() throws Exception {
		String email = "usuario@email.com";
		String senha = "123";

		UsuarioDto dto = UsuarioDto.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
		
		Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);
		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API)
				.accept(JSON)
				.contentType(JSON)
				.content(json);

			mvc
				.perform(request).andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
				.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
				.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
	}
	
	@Test
	public void deveRetornarBadRequest() throws Exception {
		String email = "usuario@email.com";
		String senha = "123";

		UsuarioDto dto = UsuarioDto.builder().email(email).senha(senha).build();
		
		
		Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API)
				.accept(JSON)
				.contentType(JSON)
				.content(json);

			mvc
				.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

}
