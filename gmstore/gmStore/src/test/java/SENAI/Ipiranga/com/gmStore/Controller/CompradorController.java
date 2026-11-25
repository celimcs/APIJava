package SENAI.Ipiranga.com.gmStore.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import SENAI.Ipiranga.com.gmStore.Model.Comprador;
import SENAI.Ipiranga.com.gmStore.Model.UsuarioLogin;
import SENAI.Ipiranga.com.gmStore.Repository.CompradorRepository;
import SENAI.Ipiranga.com.gmStore.Service.CompradorService;

@RestController
@RequestMapping("/compradores")
public class CompradorController {

    private final CompradorService compradorService;
    private final CompradorRepository compradorRepository;

    public CompradorController(CompradorService compradorService, CompradorRepository compradorRepository) {
        this.compradorService = compradorService;
        this.compradorRepository = compradorRepository;
    }

    // 1. GET: listar todos os compradores
    @GetMapping
    public ResponseEntity<List<Comprador>> getAll() {
        return ResponseEntity.ok(compradorRepository.findAll());
    }

    // 2. GET: buscar comprador por CPF
    @GetMapping("/{cpf}")
    public ResponseEntity<Comprador> getByCpf(@PathVariable long cpf) {
        return compradorRepository.findById(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. GET: terceira busca (ex.: buscar por nome)
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Comprador>> getByNome(@PathVariable String nome) {
        return ResponseEntity.ok(compradorRepository.findByNomeContainingIgnoreCase(nome));
    }

    // 4. POST: cadastrar comprador (também pode criar /logar se precisar)
    @PostMapping("/cadastrar")
    public ResponseEntity<Comprador> post(@RequestBody Comprador comprador) {
        Comprador salvo = compradorService.cadastrarComprador(comprador);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PostMapping("/logar")
    public ResponseEntity<UsuarioLogin> autenticar(@RequestBody Optional<UsuarioLogin> usuarioLogin) {
        return compradorService.logar(usuarioLogin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // 5. DELETE: remover comprador por CPF
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable long cpf) {
        if (!compradorRepository.existsById(cpf)) {
            return ResponseEntity.notFound().build();
        }
        compradorRepository.deleteById(cpf);
        return ResponseEntity.noContent().build();
    }
}