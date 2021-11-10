package com.starking.minhasFinancas.api.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
		return null;	
	}
	
	private Lancamento converter(LancamentoDto lancamentoDto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(lancamentoDto.getId());
		lancamento.setDescricao(lancamentoDto.getDescricao());
		lancamento.setAno(lancamentoDto.getAno());
		lancamento.setMes(lancamentoDto.getMes());
		lancamento.setValor(lancamentoDto.getValor());
		
		Usuario usuario = usuarioService.obterPorId(lancamentoDto.getUsuario())
		.orElseThrow( () -> new RegraNegocioException("Usuario não encontrado para o Id informado. "));
		
		lancamento.setUsuario(usuario);
		lancamento.setTipo(TipoLancamento.valueOf(lancamentoDto.getTipo()));
		lancamento.setStatus(StatusLancamento.valueOf(lancamentoDto.getStatus()));
		
		return lancamento;
	}
}
