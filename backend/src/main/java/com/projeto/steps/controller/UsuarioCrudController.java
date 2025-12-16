package com.projeto.steps.controller;

import com.projeto.steps.entity.Usuario;
import com.projeto.steps.repository.UsuarioJpaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 🎮 Controller REST para CRUD de Usuários - V1.4
 * 
 * API completa para gerenciamento de usuários com SQL Server
 * 
 * 🎯 Endpoints Disponíveis:
 * 
 * 📋 CONSULTAS:
 * GET  /api/v1.4/usuarios           - Listar todos
 * GET  /api/v1.4/usuarios/{id}      - Buscar por ID
 * GET  /api/v1.4/usuarios/nome/{nome} - Buscar por nome
 * GET  /api/v1.4/usuarios/aniversario - Aniversariantes hoje
 * 
 * ✏️ OPERAÇÕES:
 * POST   /api/v1.4/usuarios         - Criar usuário
 * PUT    /api/v1.4/usuarios/{id}    - Atualizar usuário
 * DELETE /api/v1.4/usuarios/{id}    - Excluir usuário
 * 
 * 📊 ESTATÍSTICAS:
 * GET  /api/v1.4/estatisticas       - Estatísticas gerais
 * GET  /api/v1.4/info               - Informações da API
 * 
 * @author Projeto Steps V1.4
 * @version 1.4.0
 */
@RestController
@RequestMapping("/api/v1.4")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500", "http://localhost:5500"})
public class UsuarioCrudController {

    @Autowired
    private UsuarioJpaRepository usuarioRepository;

    // ========================================
    // 📋 ENDPOINTS DE CONSULTA
    // ========================================

    /**
     * 📋 Listar todos os usuários
     * 
     * GET /api/v1.4/usuarios
     */
    @GetMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> listarTodos() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            long total = usuarioRepository.count();
            
            Map<String, Object> response = new HashMap<>();
            response.put("usuarios", usuarios);
            response.put("total", total);
            response.put("status", "success");
            response.put("message", "Usuários listados com sucesso");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Erro ao listar usuários: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 👤 Buscar usuário por ID
     * 
     * GET /api/v1.4/usuarios/{id}
     */
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(id);
            Map<String, Object> response = new HashMap<>();
            
            if (usuario.isPresent()) {
                response.put("usuario", usuario.get());
                response.put("status", "success");
                response.put("message", "Usuário encontrado");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "not_found");
                response.put("message", "Usuário não encontrado com ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Erro ao buscar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 🔍 Buscar usuários por nome (parcial)
     * 
     * GET /api/v1.4/usuarios/nome/{nome}
     */
    @GetMapping("/usuarios/nome/{nome}")
    public ResponseEntity<Map<String, Object>> buscarPorNome(@PathVariable String nome) {
        try {
            List<Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCase(nome);
            
            Map<String, Object> response = new HashMap<>();
            response.put("usuarios", usuarios);
            response.put("total", usuarios.size());
            response.put("termo_busca", nome);
            response.put("status", "success");
            response.put("message", "Busca realizada com sucesso");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Erro na busca: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 🎂 Aniversariantes de hoje
     * 
     * GET /api/v1.4/usuarios/aniversario
     */
    @GetMapping("/usuarios/aniversario")
    public ResponseEntity<Map<String, Object>> aniversariantesHoje() {
        try {
            List<Usuario> aniversariantes = usuarioRepository.findAniversariantesHoje();
            
            Map<String, Object> response = new HashMap<>();
            response.put("aniversariantes", aniversariantes);
            response.put("total", aniversariantes.size());
            response.put("data", LocalDate.now());
            response.put("status", "success");
            response.put("message", "Aniversariantes de hoje listados");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Erro ao buscar aniversariantes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ========================================
    // ✏️ ENDPOINTS DE OPERAÇÃO
    // ========================================

    /**
     * ➕ Criar novo usuário
     * 
     * POST /api/v1.4/usuarios
     */
    @PostMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> criarUsuario(@Valid @RequestBody Usuario usuario, 
                                                           BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        
        // Validar entrada
        if (bindingResult.hasErrors()) {
            response.put("status", "validation_error");
            response.put("message", "Dados inválidos");
            response.put("errors", bindingResult.getAllErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        try {
            // Verificar se já existe usuário com mesmo nome
            Optional<Usuario> usuarioExistente = usuarioRepository.findByNomeIgnoreCase(usuario.getNome());
            if (usuarioExistente.isPresent()) {
                response.put("status", "duplicate");
                response.put("message", "Já existe um usuário com este nome");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            Usuario novoUsuario = usuarioRepository.save(usuario);
            
            response.put("usuario", novoUsuario);
            response.put("status", "success");
            response.put("message", "Usuário criado com sucesso");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erro ao criar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * ✏️ Atualizar usuário existente
     * 
     * PUT /api/v1.4/usuarios/{id}
     */
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Map<String, Object>> atualizarUsuario(@PathVariable Long id, 
                                                               @Valid @RequestBody Usuario usuarioAtualizado,
                                                               BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        
        // Validar entrada
        if (bindingResult.hasErrors()) {
            response.put("status", "validation_error");
            response.put("message", "Dados inválidos");
            response.put("errors", bindingResult.getAllErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        try {
            Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
            
            if (!usuarioExistente.isPresent()) {
                response.put("status", "not_found");
                response.put("message", "Usuário não encontrado com ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            Usuario usuario = usuarioExistente.get();
            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setDataNascimento(usuarioAtualizado.getDataNascimento());
            
            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            
            response.put("usuario", usuarioSalvo);
            response.put("status", "success");
            response.put("message", "Usuário atualizado com sucesso");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erro ao atualizar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 🗑️ Excluir usuário
     * 
     * DELETE /api/v1.4/usuarios/{id}
     */
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Map<String, Object>> excluirUsuario(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(id);
            
            if (!usuario.isPresent()) {
                response.put("status", "not_found");
                response.put("message", "Usuário não encontrado com ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            usuarioRepository.deleteById(id);
            
            response.put("status", "success");
            response.put("message", "Usuário excluído com sucesso");
            response.put("usuario_excluido", usuario.get());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erro ao excluir usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ========================================
    // 📊 ENDPOINTS DE ESTATÍSTICAS
    // ========================================

    /**
     * 📊 Estatísticas gerais do sistema
     * 
     * GET /api/v1.4/estatisticas
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        try {
            Object[] stats = usuarioRepository.getEstatisticasGerais();
            List<Usuario> usuariosHoje = usuarioRepository.findUsuariosCriadosHoje();
            List<Usuario> aniversariantes = usuarioRepository.findAniversariantesHoje();
            
            Map<String, Object> response = new HashMap<>();
            response.put("total_usuarios", stats[0]);
            response.put("usuarios_cadastrados_hoje", stats[1]);
            response.put("idade_media", stats[2]);
            response.put("aniversariantes_hoje", aniversariantes.size());
            response.put("usuarios_recentes", usuariosHoje);
            response.put("data_consulta", LocalDate.now());
            response.put("status", "success");
            response.put("message", "Estatísticas geradas com sucesso");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Erro ao gerar estatísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * ℹ️ Informações da API
     * 
     * GET /api/v1.4/info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> informacoesApi() {
        Map<String, Object> info = new HashMap<>();
        info.put("nome", "Steps API V1.4");
        info.put("versao", "1.4.0");
        info.put("descricao", "API CRUD completa com SQL Server no Podman");
        info.put("database", "SQL Server 2022");
        info.put("container", "Podman");
        info.put("funcionalidades", List.of(
            "CRUD completo de usuários",
            "Validações automáticas",
            "Busca por nome",
            "Aniversariantes",
            "Estatísticas detalhadas",
            "Interface frontend separada"
        ));
        info.put("endpoints_disponiveis", Map.of(
            "usuarios", "/api/v1.4/usuarios",
            "estatisticas", "/api/v1.4/estatisticas",
            "info", "/api/v1.4/info"
        ));
        info.put("status", "success");
        
        return ResponseEntity.ok(info);
    }
}