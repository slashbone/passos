package com.projeto.steps.controller;

import com.projeto.steps.model.Usuario;
import com.projeto.steps.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controlador REST para operações CRUD completas de usuários - Versão 1.3.1
 * 
 * Endpoints disponíveis:
 * - GET / : Informações da API
 * - POST /usuarios : Criar usuário
 * - GET /usuarios : Listar todos os usuários
 * - GET /usuarios/{id} : Buscar usuário por ID
 * - GET /usuarios/buscar?nome={nome} : Buscar usuários por nome
 * - PUT /usuarios/{id} : Atualizar usuário
 * - DELETE /usuarios/{id} : Excluir usuário
 * - GET /estatisticas : Estatísticas do sistema
 * 
 * @author Projeto Steps
 * @version 1.3.1
 * @since 2024
 */
@RestController
@RequestMapping("/api/v1.31")
@CrossOrigin(origins = "*")
public class WelcomeControllerV131 {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Endpoint para servir a página demo V1.3.1
     */
    @GetMapping("/demo-v131")
    public String paginaDemoV131() {
        return "forward:/demo-v131.html";
    }

    /**
     * Endpoint raiz - Informações da API
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> informacoesAPI() {
        Map<String, Object> info = new HashMap<>();
        info.put("titulo", "API Steps - CRUD Completo MongoDB");
        info.put("versao", "1.3.1");
        info.put("descricao", "Sistema completo de gerenciamento de usuários com MongoDB");
        info.put("banco_de_dados", "MongoDB");
        info.put("colecao", "dados_usuario");
        info.put("status", "ativo");
        info.put("endpoints", Arrays.asList(
            "GET / - Informações da API",
            "POST /usuarios - Criar usuário",
            "GET /usuarios - Listar todos os usuários",
            "GET /usuarios/{id} - Buscar usuário por ID",
            "GET /usuarios/buscar?nome={nome} - Buscar usuários por nome",
            "PUT /usuarios/{id} - Atualizar usuário",
            "DELETE /usuarios/{id} - Excluir usuário",
            "GET /estatisticas - Estatísticas do sistema"
        ));
        info.put("timestamp", new Date());
        
        return ResponseEntity.ok(info);
    }

    /**
     * Endpoint para estatísticas do sistema
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        try {
            long totalUsuarios = usuarioRepository.count();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("total_usuarios", totalUsuarios);
            stats.put("banco_conectado", true);
            stats.put("ultima_atualizacao", new Date());
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro ao obter estatísticas");
            erro.put("detalhes", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }

    /**
     * Criar novo usuário
     */
    @PostMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> criarUsuario(@RequestBody Map<String, String> dadosUsuario) {
        try {
            // Validação dos campos obrigatórios
            String nome = dadosUsuario.get("nome");
            String dataNascimentoStr = dadosUsuario.get("dataNascimento");
            
            if (nome == null || nome.trim().isEmpty()) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "O campo 'nome' é obrigatório");
                return ResponseEntity.badRequest().body(erro);
            }
            
            if (dataNascimentoStr == null || dataNascimentoStr.trim().isEmpty()) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "O campo 'dataNascimento' é obrigatório");
                return ResponseEntity.badRequest().body(erro);
            }
            
            // Validação da data
            LocalDate dataNascimento;
            try {
                dataNascimento = LocalDate.parse(dataNascimentoStr);
                
                // Verifica se a data não é futura
                if (dataNascimento.isAfter(LocalDate.now())) {
                    Map<String, Object> erro = new HashMap<>();
                    erro.put("erro", "A data de nascimento não pode ser uma data futura");
                    return ResponseEntity.badRequest().body(erro);
                }
                
                // Verifica se a data não é muito antiga (mais de 150 anos)
                if (dataNascimento.isBefore(LocalDate.now().minusYears(150))) {
                    Map<String, Object> erro = new HashMap<>();
                    erro.put("erro", "A data de nascimento não pode ser anterior a " + 
                           LocalDate.now().minusYears(150).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    return ResponseEntity.badRequest().body(erro);
                }
                
            } catch (Exception e) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "Formato de data inválido. Use o formato YYYY-MM-DD");
                return ResponseEntity.badRequest().body(erro);
            }
            
            // Validação do nome
            if (nome.trim().length() < 3) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "O nome deve ter pelo menos 3 caracteres");
                return ResponseEntity.badRequest().body(erro);
            }
            
            // Criar o usuário
            Usuario novoUsuario = new Usuario();
            novoUsuario.setNome(nome.trim());
            novoUsuario.setDataNascimento(dataNascimento);
            
            Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
            
            // Resposta de sucesso
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Usuário cadastrado com sucesso!");
            resposta.put("usuario", usuarioSalvo);
            resposta.put("timestamp", new Date());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro interno do servidor ao criar usuário");
            erro.put("detalhes", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }

    /**
     * Listar todos os usuários
     */
    @GetMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("usuarios", usuarios);
            resposta.put("total", usuarios.size());
            resposta.put("timestamp", new Date());
            
            return ResponseEntity.ok(resposta);
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro ao buscar usuários");
            erro.put("detalhes", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }

    /**
     * Buscar usuário por ID
     */
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Map<String, Object>> buscarUsuarioPorId(@PathVariable String id) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
            
            if (usuarioOpt.isPresent()) {
                Map<String, Object> resposta = new HashMap<>();
                resposta.put("usuario", usuarioOpt.get());
                resposta.put("encontrado", true);
                resposta.put("timestamp", new Date());
                
                return ResponseEntity.ok(resposta);
            } else {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "Usuário não encontrado");
                erro.put("id_pesquisado", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
            }
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro ao buscar usuário");
            erro.put("detalhes", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }

    /**
     * Buscar usuários por nome (busca parcial)
     */
    @GetMapping("/usuarios/buscar")
    public ResponseEntity<Map<String, Object>> buscarUsuariosPorNome(@RequestParam String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "Parâmetro 'nome' é obrigatório");
                return ResponseEntity.badRequest().body(erro);
            }
            
            List<Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCase(nome.trim());
            
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("usuarios", usuarios);
            resposta.put("total_encontrados", usuarios.size());
            resposta.put("termo_pesquisado", nome.trim());
            resposta.put("timestamp", new Date());
            
            return ResponseEntity.ok(resposta);
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro ao buscar usuários por nome");
            erro.put("detalhes", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }

    /**
     * Atualizar usuário existente
     */
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Map<String, Object>> atualizarUsuario(@PathVariable String id, @RequestBody Map<String, String> dadosUsuario) {
        try {
            // Verificar se o usuário existe
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
            
            if (!usuarioOpt.isPresent()) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "Usuário não encontrado para atualização");
                erro.put("id_pesquisado", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
            }
            
            Usuario usuarioExistente = usuarioOpt.get();
            
            // Validação dos campos obrigatórios
            String nome = dadosUsuario.get("nome");
            String dataNascimentoStr = dadosUsuario.get("dataNascimento");
            
            if (nome == null || nome.trim().isEmpty()) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "O campo 'nome' é obrigatório");
                return ResponseEntity.badRequest().body(erro);
            }
            
            if (dataNascimentoStr == null || dataNascimentoStr.trim().isEmpty()) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "O campo 'dataNascimento' é obrigatório");
                return ResponseEntity.badRequest().body(erro);
            }
            
            // Validação da data
            LocalDate dataNascimento;
            try {
                dataNascimento = LocalDate.parse(dataNascimentoStr);
                
                // Verifica se a data não é futura
                if (dataNascimento.isAfter(LocalDate.now())) {
                    Map<String, Object> erro = new HashMap<>();
                    erro.put("erro", "A data de nascimento não pode ser uma data futura");
                    return ResponseEntity.badRequest().body(erro);
                }
                
                // Verifica se a data não é muito antiga (mais de 150 anos)
                if (dataNascimento.isBefore(LocalDate.now().minusYears(150))) {
                    Map<String, Object> erro = new HashMap<>();
                    erro.put("erro", "A data de nascimento não pode ser anterior a " + 
                           LocalDate.now().minusYears(150).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    return ResponseEntity.badRequest().body(erro);
                }
                
            } catch (Exception e) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "Formato de data inválido. Use o formato YYYY-MM-DD");
                return ResponseEntity.badRequest().body(erro);
            }
            
            // Validação do nome
            if (nome.trim().length() < 3) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "O nome deve ter pelo menos 3 caracteres");
                return ResponseEntity.badRequest().body(erro);
            }
            
            // Atualizar os dados
            usuarioExistente.setNome(nome.trim());
            usuarioExistente.setDataNascimento(dataNascimento);
            
            Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
            
            // Resposta de sucesso
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Usuário atualizado com sucesso!");
            resposta.put("usuario", usuarioAtualizado);
            resposta.put("timestamp", new Date());
            
            return ResponseEntity.ok(resposta);
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro interno do servidor ao atualizar usuário");
            erro.put("detalhes", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }

    /**
     * Excluir usuário
     */
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Map<String, Object>> excluirUsuario(@PathVariable String id) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                usuarioRepository.delete(usuario);
                
                Map<String, Object> resposta = new HashMap<>();
                resposta.put("mensagem", "Usuário excluído com sucesso!");
                resposta.put("usuario_excluido", usuario);
                resposta.put("timestamp", new Date());
                
                return ResponseEntity.ok(resposta);
            } else {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "Usuário não encontrado para exclusão");
                erro.put("id_pesquisado", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
            }
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro ao excluir usuário");
            erro.put("detalhes", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }
}