package com.starking.minhasFinancas.model.entity.service.impl;

import com.starking.minhasFinancas.model.entity.Usuario;
import com.starking.minhasFinancas.model.entity.repositories.UsuarioRepository;
import com.starking.minhasFinancas.model.entity.service.UsuarioService;

public class UsuarioServiceImpl implements UsuarioService{
	
	private UsuarioRepository repository;
	
	

	public UsuarioServiceImpl(UsuarioRepository repository) {
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validarEmail(String email) {
		// TODO Auto-generated method stub
		
	}

}
