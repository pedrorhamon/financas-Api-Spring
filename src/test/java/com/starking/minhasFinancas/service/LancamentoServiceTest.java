package com.starking.minhasFinancas.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
		
		Assertions.catchThrowableOfType(() -> service.salvar(lancamentoSalvar), RegraNegocioException.class);
		
		verify(repository, never()).save(lancamentoSalvar);
		
	}

}
