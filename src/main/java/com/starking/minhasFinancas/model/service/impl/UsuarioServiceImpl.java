package com.starking.minhasFinancas.model.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.starking.minhasFinancas.exception.ErroAutenticacao;
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
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

		if (!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuario não encontrado");
		}
		if (usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida. ");
		}
		return usuario.get();
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
