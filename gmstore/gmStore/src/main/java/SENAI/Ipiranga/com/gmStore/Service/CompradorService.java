package SENAI.Ipiranga.com.gmStore.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import SENAI.Ipiranga.com.gmStore.Model.Comprador;
import SENAI.Ipiranga.com.gmStore.Model.UsuarioLogin;
import SENAI.Ipiranga.com.gmStore.Repository.CompradorRepository;

@Service
public class CompradorService {
	
private final CompradorRepository repository;
private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder ();
	
public CompradorService(CompradorRepository repository) {
		this.repository = repository;
	}

public Comprador cadastrarComprador( Comprador comprador) {
		
	if (comprador==null)
		{
		throw new IllegalArgumentException("Os dados não foram informados");
		}
		
	if(comprador.getSenha() ==null || comprador.getSenha().isBlank()) {
			throw new IllegalArgumentException("A senha deve ser informada");
		}
		String senhaEncoder = encoder.encode(comprador.getSenha());
		comprador.setSenha(senhaEncoder);
		return repository.save(comprador);
}
		
public Optional<UsuarioLogin> logar(Optional<UsuarioLogin> user){
			
			if(user == null || user.isEmpty() ) {
				return Optional.empty();
			}
			UsuarioLogin usuarioLogin = user.get();
			if(usuarioLogin.getUsuario() == null||
					usuarioLogin.getUsuario().isBlank()||
					usuarioLogin.getSenha()==null||
					usuarioLogin.getSenha().isBlank()) {
				return Optional.empty();
			}
			
			Optional<Comprador> compradorEncontrado = repository.findByEmail(usuarioLogin.getUsuario());
			
			if(compradorEncontrado.isEmpty()) {
				return Optional.empty();
				
			}
			Comprador compradorBanco = compradorEncontrado.get();
			if(!encoder.matches(usuarioLogin.getSenha(), compradorBanco.getSenha())) {
				return Optional.empty();
			}
				String auth = usuarioLogin.getUsuario()+":"+ usuarioLogin.getSenha();
				String encodedAuth = Base64.getEncoder()
						.encodeToString(auth.getBytes(StandardCharsets.UTF_8));
				String authHeader = "Basic "+ encodedAuth;
				usuarioLogin.setToken(authHeader);
				usuarioLogin.setId(compradorBanco.getCpf());
				usuarioLogin.setNome(compradorBanco.getNome());
				usuarioLogin.setFoto(compradorBanco.getTelefone());
				usuarioLogin.setTipo(compradorBanco.getEmail());
				
				
				return Optional.of(usuarioLogin);
				
			}
		}
	
	


