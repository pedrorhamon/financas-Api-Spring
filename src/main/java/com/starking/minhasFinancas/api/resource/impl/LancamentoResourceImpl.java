package com.starking.minhasFinancas.api.resource.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.starking.minhasFinancas.api.dto.AtualizaStatusDto;
import com.starking.minhasFinancas.api.dto.LancamentoDto;
import com.starking.minhasFinancas.api.resource.LancamentoResource;
import com.starking.minhasFinancas.exception.RegraNegocioException;
import com.starking.minhasFinancas.model.entity.Lancamento;
import com.starking.minhasFinancas.model.entity.Usuario;
import com.starking.minhasFinancas.model.enums.StatusLancamento;
import com.starking.minhasFinancas.model.enums.TipoLancamento;
import com.starking.minhasFinancas.model.service.LancamentoService;
import com.starking.minhasFinancas.model.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoResourceImpl implements LancamentoResource{

	private final LancamentoService lancamentoService;

	private final UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity buscar(
			@RequestParam(value = "descricao", required = false) String descricao, 
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano, 
			@RequestParam("usuario") Long idUsuario) {
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possivel realizar consulta. Usuario não encontrado para o Id informado. ");
		}else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarPorId(@PathVariable("id")Long id){
		return lancamentoService.obterPorId(id)
				.map(lancamento -> new ResponseEntity(converter(lancamento), HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody 
			LancamentoDto lancamentoDto) {
		try {
			Lancamento entidade = converter(lancamentoDto);
			entidade = lancamentoService.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity<?>  atualizar(@PathVariable("id") Long id,
			@RequestBody LancamentoDto lancamentoDto) {
		return lancamentoService.obterPorId(id).map(entity -> {
			try {
				Lancamento lancamento = converter(lancamentoDto);
				lancamento.setId(entity.getId());
				lancamentoService.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);

			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}
	
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity<?>  atualizarStatus( @PathVariable("id") Long id , @RequestBody AtualizaStatusDto dto ) {
		return lancamentoService.obterPorId(id).map( entity -> {
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
			
			if(statusSelecionado == null) {
				return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um status válido.");
			}
			
			try {
				entity.setStatus(statusSelecionado);
				lancamentoService.atualizar(entity);
				return ResponseEntity.ok(entity);
			}catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet( () ->
		new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?>  deletar(@PathVariable("id") Long id) {
		return lancamentoService.obterPorId(id).map(entidade -> {
			lancamentoService.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> 
			new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}

	private Lancamento converter(LancamentoDto lancamentoDto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(lancamentoDto.getId());
		lancamento.setDescricao(lancamentoDto.getDescricao());
		lancamento.setAno(lancamentoDto.getAno());
		lancamento.setMes(lancamentoDto.getMes());
		lancamento.setValor(lancamentoDto.getValor());

		Usuario usuario = usuarioService.obterPorId(lancamentoDto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuario não encontrado para o Id informado. "));
		lancamento.setUsuario(usuario);
		
		if (lancamentoDto.getTipo() != null) {
			lancamento.setTipo(TipoLancamento.valueOf(lancamentoDto.getTipo()));
		}
		if (lancamentoDto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(lancamentoDto.getStatus()));
		}
		return lancamento;
	}
	
	private LancamentoDto converter(Lancamento lancamento) {
		return LancamentoDto.builder()
				.id(lancamento.getId())
				.descricao(lancamento.getDescricao())
				.valor(lancamento.getValor())
				.mes(lancamento.getMes())
				.ano(lancamento.getAno())
				.status(lancamento.getStatus().name())
				.tipo(lancamento.getTipo().name())
				.usuario(lancamento.getUsuario().getId())
				.build();
	}
}
