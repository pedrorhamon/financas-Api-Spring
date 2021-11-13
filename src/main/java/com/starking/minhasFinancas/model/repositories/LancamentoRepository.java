package com.starking.minhasFinancas.model.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.starking.minhasFinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	@Query(value = "select sum(1.valor) "
			+ "from Lancamento 1 join 1.usuario u "
			+ "where u.id = :idUsuario and 1.tipo =:tipo group by u")
	BigDecimal obterSaldoPorTipoLancamentoUsuario(@Param("idUsuario") Long idUsuario, @Param("tipo") String tipo);

}
