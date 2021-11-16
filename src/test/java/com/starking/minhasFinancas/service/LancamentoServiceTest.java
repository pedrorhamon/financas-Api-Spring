package com.starking.minhasFinancas.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.starking.minhasFinancas.exception.RegraNegocioException;
import com.starking.minhasFinancas.model.entity.Lancamento;
import com.starking.minhasFinancas.model.enums.StatusLancamento;
import com.starking.minhasFinancas.model.repositories.LancamentoRepository;
import com.starking.minhasFinancas.model.repositories.LancamentoRepositoryTest;
import com.starking.minhasFinancas.model.service.impl.LancamentoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {
	
	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		
		Lancamento lancamemtoSalvar = LancamentoRepositoryTest.criarLancamento();
		doNothing().when(service).validar(lancamemtoSalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		when(repository.save(lancamemtoSalvar)).thenReturn(lancamentoSalvo);
		
		Lancamento lancamento =  service.salvar(lancamemtoSalvar);
		
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoErroValidacao() {
		Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento();
		doThrow(RegraNegocioException.class).when(service).validar(lancamentoSalvar);
		
		catchThrowableOfType(() -> service.salvar(lancamentoSalvar), RegraNegocioException.class);
		
		verify(repository, never()).save(lancamentoSalvar);	
	}
	
	
	@Test
	public void deveAtualizarLancamento() {
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		doNothing().when(service).validar(lancamentoSalvo);
		
		when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		service.atualizar(lancamentoSalvo);
		verify(repository, times(1)).save(lancamentoSalvo);
		
	}

	@Test
	public void deveLancaErroAtualizar() {
		Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento();
		
		catchThrowableOfType(() -> service.atualizar(lancamentoSalvar), NullPointerException.class);
		
		verify(repository, never()).save(lancamentoSalvar);	
	}
	
	@Test
	public void deveDeletarLancamento() {
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		
		service.deletar(lancamentoSalvo);
		
		verify(repository).delete(lancamentoSalvo);
	}
	
	@Test
	public void naoDeveDeletarLancamentoSalvo() {
		Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento();

		catchThrowableOfType(() -> service.deletar(lancamentoSalvar), NullPointerException.class);

		verify(repository, never()).save(lancamentoSalvar);
	}
}
