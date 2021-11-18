package com.starking.minhasFinancas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.starking.minhasFinancas.exception.RegraNegocioException;
import com.starking.minhasFinancas.model.entity.Lancamento;
import com.starking.minhasFinancas.model.entity.Usuario;
import com.starking.minhasFinancas.model.enums.StatusLancamento;
import com.starking.minhasFinancas.model.enums.TipoLancamento;
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
	
	@Test
	public void deveFiltrarLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> lista = Arrays.asList(lancamento);
		when( repository.findAll(any(Example.class))).thenReturn(lista);
		
		List<Lancamento> resultado = service.buscar(lancamento);
		
		Assertions
			.assertThat(resultado)
			.isNotEmpty()
			.hasSize(1)
			.contains(lancamento);
	}
	
	@Test
	public void deveAtualizarStatusLancamento() {
		Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvar.setId(1l);
		lancamentoSalvar.setStatus(StatusLancamento.PENDENTE);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		doReturn(lancamentoSalvar).when(service).atualizar(lancamentoSalvar);
		
		service.atualizarStatus(lancamentoSalvar, novoStatus);
		
		Assertions.assertThat(lancamentoSalvar.getStatus()).isEqualTo(novoStatus);
		verify(service).atualizar(lancamentoSalvar);

	}
	
	@Test
	public void deveObterLancamentoId() {
		Long id = 1L;
		
		Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvar.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		Assertions.assertThat(resultado.isPresent()).isFalse();	
	}
	
	@Test
	public void deveLancarErrosValidacao() {
		Lancamento lancamento = new Lancamento();

		Throwable erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");

		lancamento.setDescricao("");

		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");

		lancamento.setDescricao("Salario");

		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

		lancamento.setAno(0);

		erro = catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

		lancamento.setAno(13);

		erro = catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

		lancamento.setMes(1);

		erro = catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");

		lancamento.setAno(202);

		erro = catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");

		lancamento.setAno(2020);

		erro = catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");

		lancamento.setUsuario(new Usuario());

		erro = catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");

		lancamento.getUsuario().setId(1l);

		erro = catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");

		lancamento.setValor(BigDecimal.ZERO);

		erro = catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");

		lancamento.setValor(BigDecimal.valueOf(1));

		erro = catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um tipo de Lançamento.");
	}
	
	@Test
	public void deveObterSaldoPorUsuario() {
		Long idUsuario = 1l;
		
		when( repository
				.obterSaldoPorTipoLancamentoEUsuarioEStatus(idUsuario, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO)) 
				.thenReturn(BigDecimal.valueOf(100));
		
		when( repository
				.obterSaldoPorTipoLancamentoEUsuarioEStatus(idUsuario, TipoLancamento.DESPESA, StatusLancamento.EFETIVADO)) 
				.thenReturn(BigDecimal.valueOf(50));

		BigDecimal saldo = service.obterSaldoPorUsuario(idUsuario);

		assertThat(saldo).isEqualTo(BigDecimal.valueOf(50));		
	}	
}
