package com.projeto.steps.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CONTROLLER REST API V1.2 - VERSÃO EXPANDIDA
 * 
 * Nova versão com funcionalidades REST avançadas:
 * - Contadores de uso
 * - Histórico de ações
 * - Estatísticas em tempo real
 * - Gerenciamento de dados
 * - Endpoints CRUD completos
 * 
 * @author Steps Development Team
 * @version 1.2.0
 * @since 2025-12-16
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v2")
public class WelcomeControllerV12 {

    // Contadores e estatísticas em memória
    private final AtomicInteger clickCount = new AtomicInteger(0);
    private final AtomicInteger viewCount = new AtomicInteger(0);
    private final List<Map<String, String>> activityLog = new ArrayList<>();
    private final LocalDateTime startTime = LocalDateTime.now();
    private String customMessage = "Bem-vindo ao Sistema Steps V1.2!";

    /**
     * GET /api/v2/welcome
     * Endpoint de boas-vindas com estatísticas
     */
    @GetMapping("/welcome")
    public ResponseEntity<Map<String, Object>> getWelcome() {
        int views = viewCount.incrementAndGet();
        logActivity("welcome_view", "Visualização da mensagem de boas-vindas");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", customMessage);
        response.put("version", "1.2.0");
        response.put("viewNumber", views);
        response.put("timestamp", getCurrentTimestamp());
        response.put("uptime", getUptime());
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v2/thanks
     * Versão melhorada do endpoint de agradecimento
     */
    @PostMapping("/thanks")
    public ResponseEntity<Map<String, Object>> postThanks(@RequestBody(required = false) Map<String, Object> body) {
        int clicks = clickCount.incrementAndGet();
        
        // Extrai informações opcionais do body
        String userName = body != null ? (String) body.get("userName") : "Usuário";
        String source = body != null ? (String) body.get("source") : "web";
        
        logActivity("button_click", "Clique no botão de agradecimento por " + userName);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Obrigado, " + userName + "!");
        response.put("success", true);
        response.put("clickNumber", clicks);
        response.put("source", source);
        response.put("timestamp", getCurrentTimestamp());
        response.put("totalClicks", clicks);
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v2/stats
     * Estatísticas completas da aplicação
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Estatísticas básicas
        stats.put("totalViews", viewCount.get());
        stats.put("totalClicks", clickCount.get());
        stats.put("clickToViewRatio", calculateClickRatio());
        stats.put("uptime", getUptime());
        stats.put("startTime", startTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        
        // Atividade recente (últimas 10 ações)
        List<Map<String, String>> recentActivity = activityLog.size() > 10 ? 
            activityLog.subList(activityLog.size() - 10, activityLog.size()) : 
            new ArrayList<>(activityLog);
        stats.put("recentActivity", recentActivity);
        
        // Métricas avançadas
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("averageClicksPerHour", calculateClicksPerHour());
        metrics.put("totalActivities", activityLog.size());
        metrics.put("systemHealth", "excellent");
        stats.put("metrics", metrics);
        
        return ResponseEntity.ok(stats);
    }

    /**
     * GET /api/v2/activity
     * Histórico completo de atividades
     */
    @GetMapping("/activity")
    public ResponseEntity<Map<String, Object>> getActivity(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        logActivity("activity_view", "Consulta ao histórico de atividades");
        
        Map<String, Object> response = new HashMap<>();
        
        // Paginação simples
        int start = page * size;
        int end = Math.min(start + size, activityLog.size());
        List<Map<String, String>> paginatedActivity = 
            start < activityLog.size() ? activityLog.subList(start, end) : new ArrayList<>();
        
        response.put("activities", paginatedActivity);
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalActivities", activityLog.size());
        response.put("totalPages", (int) Math.ceil((double) activityLog.size() / size));
        
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/v2/message
     * Atualiza a mensagem personalizada
     */
    @PutMapping("/message")
    public ResponseEntity<Map<String, Object>> updateMessage(@RequestBody Map<String, String> request) {
        String newMessage = request.get("message");
        
        if (newMessage == null || newMessage.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Mensagem não pode estar vazia");
            error.put("timestamp", getCurrentTimestamp());
            return ResponseEntity.badRequest().body(error);
        }
        
        String oldMessage = this.customMessage;
        this.customMessage = newMessage.trim();
        
        logActivity("message_update", "Mensagem alterada de '" + oldMessage + "' para '" + newMessage + "'");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Mensagem atualizada com sucesso!");
        response.put("oldMessage", oldMessage);
        response.put("newMessage", this.customMessage);
        response.put("timestamp", getCurrentTimestamp());
        
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/v2/reset
     * Reset completo dos contadores e logs
     */
    @DeleteMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetAll() {
        int oldViews = viewCount.getAndSet(0);
        int oldClicks = clickCount.getAndSet(0);
        int oldActivities = activityLog.size();
        
        activityLog.clear();
        logActivity("system_reset", "Sistema resetado completamente");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Sistema resetado com sucesso!");
        response.put("resetStats", Map.of(
            "previousViews", oldViews,
            "previousClicks", oldClicks,
            "previousActivities", oldActivities
        ));
        response.put("timestamp", getCurrentTimestamp());
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v2/health
     * Health check avançado
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        
        // Status geral
        health.put("status", "UP");
        health.put("version", "1.2.0");
        health.put("timestamp", getCurrentTimestamp());
        
        // Componentes
        Map<String, String> components = new HashMap<>();
        components.put("api", "UP");
        components.put("statistics", "UP");
        components.put("logging", "UP");
        components.put("memory", "UP");
        health.put("components", components);
        
        // Métricas de performance
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> performance = new HashMap<>();
        performance.put("memoryUsed", (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024 + " MB");
        performance.put("memoryFree", runtime.freeMemory() / 1024 / 1024 + " MB");
        performance.put("processors", runtime.availableProcessors());
        health.put("performance", performance);
        
        return ResponseEntity.ok(health);
    }

    /**
     * GET /api/v2/info
     * Informações detalhadas da API
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> info = new HashMap<>();
        
        // Informações da API
        info.put("name", "Steps REST API");
        info.put("version", "1.2.0");
        info.put("description", "API REST avançada com estatísticas e gerenciamento de dados");
        info.put("startTime", startTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        info.put("uptime", getUptime());
        
        // Endpoints disponíveis
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("GET /api/v2/welcome", "Mensagem de boas-vindas");
        endpoints.put("POST /api/v2/thanks", "Agradecimento com dados opcionais");
        endpoints.put("GET /api/v2/stats", "Estatísticas completas");
        endpoints.put("GET /api/v2/activity", "Histórico de atividades");
        endpoints.put("PUT /api/v2/message", "Atualizar mensagem personalizada");
        endpoints.put("DELETE /api/v2/reset", "Reset completo do sistema");
        endpoints.put("GET /api/v2/health", "Health check avançado");
        endpoints.put("GET /api/v2/info", "Informações da API");
        info.put("endpoints", endpoints);
        
        // Características
        List<String> features = List.of(
            "Contadores em tempo real",
            "Histórico de atividades",
            "Paginação de resultados", 
            "Mensagens personalizáveis",
            "Estatísticas avançadas",
            "Health check detalhado",
            "Reset de dados",
            "API RESTful completa"
        );
        info.put("features", features);
        
        return ResponseEntity.ok(info);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Registra uma atividade no log
     */
    private void logActivity(String action, String description) {
        Map<String, String> activity = new HashMap<>();
        activity.put("action", action);
        activity.put("description", description);
        activity.put("timestamp", getCurrentTimestamp());
        activity.put("id", String.valueOf(activityLog.size() + 1));
        
        activityLog.add(activity);
    }

    /**
     * Retorna timestamp atual formatado
     */
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    /**
     * Calcula tempo de atividade
     */
    private String getUptime() {
        LocalDateTime now = LocalDateTime.now();
        java.time.Duration duration = java.time.Duration.between(startTime, now);
        
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        
        if (days > 0) {
            return String.format("%dd %02d:%02d:%02d", days, hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

    /**
     * Calcula ratio de clicks por views
     */
    private double calculateClickRatio() {
        int views = viewCount.get();
        int clicks = clickCount.get();
        return views > 0 ? Math.round((double) clicks / views * 100.0) / 100.0 : 0.0;
    }

    /**
     * Calcula cliques por hora
     */
    private double calculateClicksPerHour() {
        LocalDateTime now = LocalDateTime.now();
        java.time.Duration duration = java.time.Duration.between(startTime, now);
        double hours = duration.toMinutes() / 60.0;
        return hours > 0 ? Math.round(clickCount.get() / hours * 100.0) / 100.0 : 0.0;
    }
}