package com.starking.minhasFinancas.api.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.starking.minhasFinancas.api.dto.AtualizaStatusDto;
import com.starking.minhasFinancas.api.dto.LancamentoDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface LancamentoResource {

//	@ApiOperation(value = "Lista todos os livros")
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = "Retorna uma lista de dto de livro"),
//			@ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
//			@ApiResponse(code = 500, message = "Retornara uma mensagem amigável para o usuário"),
//	})
//	public ResponseEntity<List<?>> buscar(@RequestParam(value = "descricao", required = false) String descricao, 
//			@RequestParam(value = "mes", required = false) Integer mes,
//			@RequestParam(value = "ano", required = false) Integer ano, 
//			@RequestParam("usuario") Long idUsuario);
	
	@ApiOperation(value = "Registra um livro")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Retorna um dto de livro"),
	    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
	    @ApiResponse(code = 500, message = "Retornara uma mensagem amigável para o usuário"),
	})
	public ResponseEntity<?> salvar(@RequestBody LancamentoDto lancamentoDto);


	@ApiOperation(value = "Atualiza um livro")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Retorna um dto de livro"),
	    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
	    @ApiResponse(code = 500, message = "Retornara uma mensagem amigável para o usuário"),
	})
	public ResponseEntity<?>  atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDto lancamentoDto);

	@ApiOperation(value = "Deleta um livro")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Não possui retorno"),
	    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
	    @ApiResponse(code = 500, message = "Retornara uma mensagem amigável para o usuário"),
	})
	public ResponseEntity<?> deletar(@PathVariable("id") Long id);
	
	@ApiOperation(value = "Atualizar Status")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Não possui retorno"),
	    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
	    @ApiResponse(code = 500, message = "Retornara uma mensagem amigável para o usuário"),
	})
	public ResponseEntity<?>  atualizarStatus( @PathVariable("id") Long id , @RequestBody AtualizaStatusDto dto );
	
	@ApiOperation(value = "Buscar por Id")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Não possui retorno"),
	    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
	    @ApiResponse(code = 500, message = "Retornara uma mensagem amigável para o usuário"),
	})
	public ResponseEntity<?> buscarPorId(@PathVariable("id")Long id);
	
	
}


