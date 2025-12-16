package com.projeto.steps.controller;

import com.projeto.steps.model.Usuario;
import com.projeto.steps.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CONTROLLER V1.3 - INTEGRAÇÃO COM MONGODB
 * 
 * EXPLICAÇÃO PARA INICIANTES:
 * Este controller é responsável por receber requisições HTTP do frontend,
 * processar os dados enviados e salvá-los no banco de dados MongoDB.
 * 
 * O QUE É UM CONTROLLER?
 * O Controller é como um "recepcionista" da sua aplicação:
 * 1. Recebe pedidos (requisições HTTP) do navegador/frontend
 * 2. Processa esses pedidos (valida dados, chama o banco de dados, etc.)
 * 3. Envia uma resposta de volta
 * 
 * FLUXO COMPLETO:
 * Frontend (navegador) → Controller → Repository → MongoDB
 * MongoDB → Repository → Controller → Frontend (navegador)
 * 
 * ANOTAÇÕES IMPORTANTES:
 * @RestController    : Marca esta classe como um controller REST (API)
 * @RequestMapping    : Define o caminho base para todos os endpoints desta classe
 * @CrossOrigin       : Permite que frontend em outra porta acesse esta API
 */
@RestController
@RequestMapping("/api/v1.3")
@CrossOrigin(origins = "*")
public class WelcomeControllerV13 {
    
    /**
     * INJEÇÃO DE DEPENDÊNCIA DO REPOSITORY
     * 
     * @Autowired diz ao Spring:
     * "Por favor, crie automaticamente um UsuarioRepository e injete aqui"
     * 
     * É como pedir ao Spring: "Eu preciso de um Repository para trabalhar,
     * você pode me fornecer um?"
     */
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    /**
     * ENDPOINT: PÁGINA INICIAL / INFORMAÇÕES DA API
     * 
     * URL: GET http://localhost:8080/api/v1.3/
     * 
     * COMO FUNCIONA:
     * Este endpoint retorna informações básicas sobre a API V1.3.
     * Não recebe parâmetros, apenas retorna um JSON informativo.
     * 
     * @return Map com informações da API
     */
    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("versao", "1.3");
        response.put("titulo", "API Steps - Integração MongoDB");
        response.put("descricao", "API REST com MongoDB para cadastro de usuários");
        response.put("banco_de_dados", "MongoDB");
        response.put("colecao", "dados_usuario");
        response.put("endpoints", Map.of(
            "POST /usuarios", "Cadastra um novo usuário",
            "GET /usuarios", "Lista todos os usuários",
            "GET /usuarios/{id}", "Busca usuário por ID",
            "PUT /usuarios/{id}", "Atualiza um usuário",
            "GET /usuarios/buscar?nome=", "Busca usuários por nome",
            "DELETE /usuarios/{id}", "Deleta um usuário"
        ));
        return response;
    }
    
    /**
     * ENDPOINT: CADASTRAR NOVO USUÁRIO
     * 
     * URL: POST http://localhost:8080/api/v1.3/usuarios
     * 
     * CORPO DA REQUISIÇÃO (JSON):
     * {
     *   "nome": "João Silva",
     *   "dataNascimento": "1990-05-15"
     * }
     * 
     * COMO FUNCIONA:
     * 1. O frontend envia dados do usuário em formato JSON
     * 2. @RequestBody converte o JSON em objeto Usuario
     * 3. O Repository salva o usuário no MongoDB
     * 4. Retorna o usuário salvo com o ID gerado pelo MongoDB
     * 
     * CÓDIGOS DE RESPOSTA HTTP:
     * - 201 CREATED: Usuário criado com sucesso
     * - 400 BAD REQUEST: Dados inválidos
     * - 500 INTERNAL SERVER ERROR: Erro no servidor
     * 
     * @param usuario Dados do usuário enviados pelo frontend
     * @return ResponseEntity com usuário salvo e status HTTP
     */
    @PostMapping("/usuarios")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        try {
            // VALIDAÇÃO BÁSICA
            if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Nome é obrigatório");
                erro.put("campo", "nome");
                return ResponseEntity.badRequest().body(erro);
            }
            
            if (usuario.getDataNascimento() == null) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Data de nascimento é obrigatória");
                erro.put("campo", "dataNascimento");
                return ResponseEntity.badRequest().body(erro);
            }
            
            // Verifica se a data não é futura
            if (usuario.getDataNascimento().isAfter(LocalDate.now())) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Data de nascimento não pode ser no futuro");
                erro.put("campo", "dataNascimento");
                return ResponseEntity.badRequest().body(erro);
            }
            
            // SALVA NO MONGODB
            // O método save() do Repository faz toda a mágica:
            // 1. Conecta com o MongoDB
            // 2. Acessa o banco "passos"
            // 3. Acessa a coleção "dados_usuario"
            // 4. Insere o documento
            // 5. Retorna o documento com ID gerado
            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            
            // CRIA RESPOSTA DE SUCESSO
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Usuário cadastrado com sucesso!");
            resposta.put("usuario", usuarioSalvo);
            resposta.put("timestamp", java.time.LocalDateTime.now());
            
            // Retorna status 201 (CREATED) com o usuário salvo
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
            
        } catch (Exception e) {
            // TRATAMENTO DE ERRO
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao cadastrar usuário: " + e.getMessage());
            erro.put("tipo", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }
    
    /**
     * ENDPOINT: LISTAR TODOS OS USUÁRIOS
     * 
     * URL: GET http://localhost:8080/api/v1.3/usuarios
     * 
     * COMO FUNCIONA:
     * Busca todos os usuários cadastrados no MongoDB e retorna em formato JSON.
     * 
     * EXEMPLO DE RESPOSTA:
     * [
     *   {
     *     "id": "65a1b2c3d4e5f6789012345",
     *     "nome": "João Silva",
     *     "dataNascimento": "1990-05-15"
     *   },
     *   {
     *     "id": "65a1b2c3d4e5f6789012346",
     *     "nome": "Maria Santos",
     *     "dataNascimento": "1985-08-20"
     *   }
     * ]
     * 
     * @return Lista de todos os usuários
     */
    @GetMapping("/usuarios")
    public ResponseEntity<?> listarUsuarios() {
        try {
            // findAll() busca TODOS os documentos da coleção "dados_usuario"
            List<Usuario> usuarios = usuarioRepository.findAll();
            
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("total", usuarios.size());
            resposta.put("usuarios", usuarios);
            resposta.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(resposta);
            
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao listar usuários: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }
    
    /**
     * ENDPOINT: BUSCAR USUÁRIO POR ID
     * 
     * URL: GET http://localhost:8080/api/v1.3/usuarios/{id}
     * Exemplo: GET http://localhost:8080/api/v1.3/usuarios/65a1b2c3d4e5f6789012345
     * 
     * COMO FUNCIONA:
     * O {id} na URL é um parâmetro variável.
     * @PathVariable captura o valor da URL e passa para o método.
     * 
     * @param id ID do usuário no MongoDB
     * @return Usuário encontrado ou erro 404
     */
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable String id) {
        try {
            // findById retorna um Optional (pode ter ou não o usuário)
            return usuarioRepository.findById(id)
                .map(usuario -> {
                    Map<String, Object> resposta = new HashMap<>();
                    resposta.put("usuario", usuario);
                    resposta.put("timestamp", java.time.LocalDateTime.now());
                    return ResponseEntity.ok((Object) resposta);
                })
                .orElseGet(() -> {
                    Map<String, Object> erro = new HashMap<>();
                    erro.put("erro", "Usuário não encontrado");
                    erro.put("id", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body((Object) erro);
                });
                
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao buscar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }
    
    /**
     * ENDPOINT: BUSCAR USUÁRIOS POR NOME
     * 
     * URL: GET http://localhost:8080/api/v1.3/usuarios/buscar?nome=Silva
     * 
     * COMO FUNCIONA:
     * @RequestParam captura parâmetros da query string (depois do ?)
     * Busca usuários cujo nome contém o texto especificado.
     * 
     * @param nome Nome ou parte do nome a buscar
     * @return Lista de usuários encontrados
     */
    @GetMapping("/usuarios/buscar")
    public ResponseEntity<?> buscarUsuariosPorNome(@RequestParam String nome) {
        try {
            // findByNomeContaining busca nomes que contêm o texto
            List<Usuario> usuarios = usuarioRepository.findByNomeContaining(nome);
            
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("termo_busca", nome);
            resposta.put("total_encontrado", usuarios.size());
            resposta.put("usuarios", usuarios);
            resposta.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(resposta);
            
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao buscar usuários: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }
    
    /**
     * ENDPOINT: DELETAR USUÁRIO
     * 
     * URL: DELETE http://localhost:8080/api/v1.3/usuarios/{id}
     * Exemplo: DELETE http://localhost:8080/api/v1.3/usuarios/65a1b2c3d4e5f6789012345
     * 
     * COMO FUNCIONA:
     * Remove o usuário do MongoDB baseado no ID fornecido.
     * 
     * @param id ID do usuário a ser deletado
     * @return Mensagem de sucesso ou erro
     */
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable String id) {
        try {
            // Verifica se o usuário existe
            if (!usuarioRepository.existsById(id)) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Usuário não encontrado");
                erro.put("id", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
            }
            
            // Deleta o usuário
            usuarioRepository.deleteById(id);
            
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Usuário deletado com sucesso");
            resposta.put("id", id);
            resposta.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(resposta);
            
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao deletar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }
    
    /**
     * ENDPOINT: ATUALIZAR USUÁRIO
     * 
     * URL: PUT http://localhost:8080/api/v1.3/usuarios/{id}
     * Exemplo: PUT http://localhost:8080/api/v1.3/usuarios/65a1b2c3d4e5f6789012345
     * 
     * CORPO DA REQUISIÇÃO (JSON):
     * {
     *   "nome": "João Silva Santos",
     *   "dataNascimento": "1990-05-15"
     * }
     * 
     * COMO FUNCIONA:
     * 1. Verifica se o usuário existe no banco
     * 2. Valida os novos dados
     * 3. Atualiza o usuário no MongoDB
     * 4. Retorna o usuário atualizado
     * 
     * @param id ID do usuário a ser atualizado
     * @param usuario Novos dados do usuário
     * @return ResponseEntity com usuário atualizado e status HTTP
     */
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable String id, @RequestBody Usuario usuario) {
        try {
            // Verificar se o usuário existe
            if (!usuarioRepository.existsById(id)) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Usuário não encontrado");
                erro.put("id", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
            }
            
            // VALIDAÇÃO BÁSICA DOS NOVOS DADOS
            if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Nome é obrigatório");
                erro.put("campo", "nome");
                return ResponseEntity.badRequest().body(erro);
            }
            
            if (usuario.getDataNascimento() == null) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Data de nascimento é obrigatória");
                erro.put("campo", "dataNascimento");
                return ResponseEntity.badRequest().body(erro);
            }
            
            // Verifica se a data não é futura
            if (usuario.getDataNascimento().isAfter(LocalDate.now())) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Data de nascimento não pode ser no futuro");
                erro.put("campo", "dataNascimento");
                return ResponseEntity.badRequest().body(erro);
            }
            
            // Define o ID no objeto para manter o mesmo ID
            usuario.setId(id);
            
            // ATUALIZA NO MONGODB
            // O método save() do Repository atualiza se o ID já existe
            Usuario usuarioAtualizado = usuarioRepository.save(usuario);
            
            // CRIA RESPOSTA DE SUCESSO
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Usuário atualizado com sucesso!");
            resposta.put("usuario", usuarioAtualizado);
            resposta.put("timestamp", java.time.LocalDateTime.now());
            
            // Retorna status 200 (OK) com o usuário atualizado
            return ResponseEntity.ok(resposta);
            
        } catch (Exception e) {
            // TRATAMENTO DE ERRO
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao atualizar usuário: " + e.getMessage());
            erro.put("tipo", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }
    
    /**
     * ENDPOINT: ESTATÍSTICAS
     * 
     * URL: GET http://localhost:8080/api/v1.3/estatisticas
     * 
     * COMO FUNCIONA:
     * Retorna estatísticas gerais sobre os dados armazenados.
     * 
     * @return Estatísticas do banco de dados
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<?> obterEstatisticas() {
        try {
            long totalUsuarios = usuarioRepository.count();
            
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("total_usuarios", totalUsuarios);
            resposta.put("banco_dados", "passos");
            resposta.put("colecao", "dados_usuario");
            resposta.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(resposta);
            
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao obter estatísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }
}
