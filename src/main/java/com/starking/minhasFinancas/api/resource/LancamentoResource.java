package com.starking.minhasFinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starking.minhasFinancas.api.dto.LancamentoDto;
import com.starking.minhasFinancas.exception.RegraNegocioException;
import com.starking.minhasFinancas.model.entity.Lancamento;
import com.starking.minhasFinancas.model.entity.Usuario;
import com.starking.minhasFinancas.model.enums.StatusLancamento;
import com.starking.minhasFinancas.model.enums.TipoLancamento;
import com.starking.minhasFinancas.model.service.LancamentoService;
import com.starking.minhasFinancas.model.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {

	private LancamentoService lancamentoService;

	private UsuarioService usuarioService;

	public LancamentoResource(LancamentoService lancamentoService) {
		this.lancamentoService = lancamentoService;
	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDto lancamentoDto) {
		try {
			Lancamento entidade = converter(lancamentoDto);
			entidade = lancamentoService.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDto lancamentoDto) {
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
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
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
		lancamento.setTipo(TipoLancamento.valueOf(lancamentoDto.getTipo()));
		lancamento.setStatus(StatusLancamento.valueOf(lancamentoDto.getStatus()));

		return lancamento;
	}
}
