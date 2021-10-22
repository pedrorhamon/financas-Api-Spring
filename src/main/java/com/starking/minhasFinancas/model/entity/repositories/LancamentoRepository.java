package com.starking.minhasFinancas.model.entity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.minhasFinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
