package com.starking.minhasFinancas.model.service.impl;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.starking.minhasFinancas.exception.RegraNegocioException;
import com.starking.minhasFinancas.model.entity.Usuario;
import com.starking.minhasFinancas.model.repositories.UsuarioRepository;
import com.starking.minhasFinancas.model.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository usuarioRepository;

	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return usuarioRepository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = usuarioRepository.existsByEmail(email);
		if (existe) {
			throw new RegraNegocioException("Já existe um usuario cadastrado com este email.");
		}
	}

}
