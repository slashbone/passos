package com.projeto.steps.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CONTROLLER REST PARA GERENCIAR AS REQUISIÇÕES DA APLICAÇÃO
 * 
 * EXPLICAÇÃO DETALHADA PARA INICIANTES:
 * 
 * Um Controller no Spring Boot é uma classe que recebe e processa requisições HTTP.
 * Ele é responsável por definir quais URLs (endpoints) a aplicação vai responder
 * e o que fazer quando cada URL for acessada.
 * 
 * ANOTAÇÕES IMPORTANTES:
 * 
 * @RestController: 
 * - Combinação de @Controller + @ResponseBody
 * - Indica que esta classe vai responder requisições REST
 * - Retorna dados diretamente (JSON) em vez de páginas HTML
 * - Ideal para APIs que fornecem dados para frontend
 * 
 * @CrossOrigin:
 * - Permite que o frontend (rodando em porta diferente) acesse este backend
 * - Resolve problemas de CORS (Cross-Origin Resource Sharing)
 * - Essencial quando frontend e backend rodam em portas/domínios diferentes
 * 
 * @GetMapping e @PostMapping:
 * - Definem qual método HTTP será usado para acessar cada endpoint
 * - GET: Para buscar/obter dados (não modifica nada no servidor)
 * - POST: Para enviar/criar dados (pode modificar estado do servidor)
 */
@RestController
@CrossOrigin(origins = "*") // Permite acesso de qualquer origem (frontend)
public class WelcomeController {

    /**
     * ENDPOINT PRINCIPAL - PÁGINA DE BOAS-VINDAS
     * 
     * Este endpoint responde à URL raiz da aplicação (http://localhost:8080/)
     * 
     * @GetMapping("/"):
     * - Mapeia requisições GET para a URL raiz "/"
     * - Quando alguém acessa http://localhost:8080/, este método é executado
     * 
     * FUNCIONAMENTO:
     * 1. Usuario acessa http://localhost:8080/ no navegador
     * 2. Spring Boot chama este método automaticamente
     * 3. Método retorna um Map com dados
     * 4. Spring converte automaticamente para JSON
     * 5. JSON é enviado de volta para o navegador
     * 
     * RETORNO:
     * - Map<String, Object>: Estrutura chave-valor que vira JSON
     * - Contém mensagem de boas-vindas e informações úteis
     * - Será convertido automaticamente para JSON pelo Spring
     */
    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        
        // Dados básicos da aplicação
        response.put("message", "Bem-vindo!");
        response.put("description", "API Spring Boot - Projeto Steps V1");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        response.put("version", "1.0.0");
        
        // Endpoints disponíveis para o frontend
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("home", "GET /");
        endpoints.put("obrigado", "POST /api/obrigado");
        endpoints.put("health", "GET /actuator/health");
        endpoints.put("info", "GET /actuator/info");
        response.put("endpoints", endpoints);
        
        // Status da aplicação
        response.put("status", "online");
        response.put("server", "Spring Boot com Actuator");
        
        return response;
    }

    /**
     * ENDPOINT DA API - RESPOSTA "OBRIGADO"
     * 
     * Este endpoint simula a funcionalidade do botão da interface original.
     * Quando o frontend faz uma requisição POST, retorna "Obrigado!"
     * 
     * @PostMapping("/api/obrigado"):
     * - Mapeia requisições POST para "/api/obrigado"
     * - POST é usado porque representa uma "ação" (clique do botão)
     * - URL com "/api/" segue convenções REST para APIs
     * 
     * FUNCIONAMENTO:
     * 1. Frontend faz requisição POST para http://localhost:8080/api/obrigado
     * 2. Este método é executado automaticamente
     * 3. Cria resposta com mensagem de agradecimento
     * 4. Spring converte para JSON e envia para o frontend
     * 5. Frontend recebe JSON e exibe a mensagem
     * 
     * ResponseEntity<Map<String, Object>>:
     * - ResponseEntity: Permite controlar detalhes da resposta HTTP
     * - Podemos definir status code, headers, etc.
     * - Map será convertido para JSON automaticamente
     */
    @PostMapping("/api/obrigado")
    public ResponseEntity<Map<String, Object>> obrigado() {
        Map<String, Object> response = new HashMap<>();
        
        // Dados da resposta
        response.put("message", "Obrigado!");
        response.put("success", true);
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        response.put("action", "button_clicked");
        
        // Retorna com status HTTP 200 (OK)
        return ResponseEntity.ok(response);
    }

    /**
     * ENDPOINT DE INFORMAÇÕES DA APLICAÇÃO
     * 
     * Fornece informações técnicas sobre a aplicação.
     * Útil para debugging e monitoramento.
     * 
     * @GetMapping("/api/info"):
     * - Endpoint adicional para informações detalhadas
     * - Complementa os endpoints do Actuator
     * - Fornece dados específicos da nossa aplicação
     */
    @GetMapping("/api/info")
    public Map<String, Object> info() {
        Map<String, Object> info = new HashMap<>();
        
        // Informações da aplicação
        info.put("name", "Steps Backend");
        info.put("description", "Backend Spring Boot para interface web com mensagem de boas-vindas");
        info.put("version", "1.0.0");
        info.put("java_version", System.getProperty("java.version"));
        info.put("spring_boot_version", "3.2.1");
        
        // Informações do sistema
        Map<String, Object> system = new HashMap<>();
        system.put("os", System.getProperty("os.name"));
        system.put("architecture", System.getProperty("os.arch"));
        system.put("available_processors", Runtime.getRuntime().availableProcessors());
        info.put("system", system);
        
        // Recursos da aplicação
        Map<String, String> features = new HashMap<>();
        features.put("web", "Spring Boot Web Starter");
        features.put("actuator", "Spring Boot Actuator para monitoramento");
        features.put("devtools", "Desenvolvimento com hot-reload");
        info.put("features", features);
        
        return info;
    }

    /**
     * ENDPOINT DE TESTE DE SAÚDE PERSONALIZADO
     * 
     * Além do Actuator health, temos nosso próprio endpoint de saúde
     * que verifica aspectos específicos da nossa aplicação.
     * 
     * DIFERENÇA DO ACTUATOR HEALTH:
     * - Actuator health: Genérico, verifica Spring Boot
     * - Este endpoint: Específico, verifica nossa lógica de negócio
     */
    @GetMapping("/api/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            // Simula verificações de saúde da aplicação
            health.put("status", "UP");
            health.put("message", "Aplicação funcionando perfeitamente!");
            health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            
            // Verifica componentes da aplicação
            Map<String, String> components = new HashMap<>();
            components.put("web_server", "UP");
            components.put("rest_api", "UP");
            components.put("actuator", "UP");
            health.put("components", components);
            
            return ResponseEntity.ok(health);
            
        } catch (Exception e) {
            // Em caso de erro, retorna status de problema
            health.put("status", "DOWN");
            health.put("message", "Erro na aplicação: " + e.getMessage());
            health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(health);
        }
    }
}