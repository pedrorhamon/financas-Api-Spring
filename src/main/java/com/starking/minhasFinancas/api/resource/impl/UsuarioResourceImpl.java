package com.starking.minhasFinancas.api.resource;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starking.minhasFinancas.api.dto.UsuarioDto;
import com.starking.minhasFinancas.exception.ErroAutenticacao;
import com.starking.minhasFinancas.exception.RegraNegocioException;
import com.starking.minhasFinancas.model.entity.Usuario;
import com.starking.minhasFinancas.model.service.LancamentoService;
import com.starking.minhasFinancas.model.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {
	
	private UsuarioService usuarioService;
	
	private LancamentoService lancamentoService;
	
	@Autowired
	public UsuarioResource(UsuarioService usuarioService, LancamentoService lancamentoService) {
		super();
		this.usuarioService = usuarioService;
		this.lancamentoService = lancamentoService;
	}

	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDto dto) {
		try {
			Usuario usuarioAutenticado = usuarioService.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		} catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity<Object> salvar(@RequestBody UsuarioDto dto) {
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha()).build();	
		try {
			Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
			return new ResponseEntity<Object>(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo(@PathVariable("id") Long id) {
		Optional<Usuario> usuario = usuarioService.obterPorId(id);

		if (!usuario.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
		return ResponseEntity.ok(saldo);
	}
}
