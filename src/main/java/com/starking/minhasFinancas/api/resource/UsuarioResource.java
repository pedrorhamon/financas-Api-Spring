package com.starking.minhasFinancas.api.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.starking.minhasFinancas.api.dto.UsuarioDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface UsuarioResource {
	
	@ApiOperation(value = "Autenticar")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Retorna um dto de livro"),
	    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
	    @ApiResponse(code = 500, message = "Retornara uma mensagem amigável para o usuário"),
	})
	public ResponseEntity<?> autenticar(@RequestBody UsuarioDto dto);
	
	@ApiOperation(value = "Registra um livro")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Retorna um dto de livro"),
	    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
	    @ApiResponse(code = 500, message = "Retornara uma mensagem amigável para o usuário"),
	})
	public ResponseEntity<Object> salvar(@RequestBody UsuarioDto dto);

	
	@ApiOperation(value = "Busca um livro por id")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Retorna um dto de livro"),
	    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
	    @ApiResponse(code = 500, message = "Retornara uma mensagem amigável para o usuário"),
	})
	public ResponseEntity obterSaldo(@PathVariable("id") Long id);
}



