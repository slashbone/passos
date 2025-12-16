package com.projeto.steps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CLASSE PRINCIPAL DA APLICAÇÃO SPRING BOOT
 * 
 * Esta é a classe mais importante da nossa aplicação. Ela serve como ponto de entrada
 * e configuração inicial de toda a aplicação Spring Boot.
 * 
 * EXPLICAÇÃO DETALHADA PARA INICIANTES:
 * 
 * @SpringBootApplication - Esta anotação é na verdade uma combinação de 3 anotações:
 *   1. @Configuration: Indica que esta classe pode definir beans (objetos gerenciados pelo Spring)
 *   2. @EnableAutoConfiguration: Permite que o Spring configure automaticamente a aplicação
 *      baseado nas dependências presentes no classpath (ex: se temos spring-web, configura servidor web)
 *   3. @ComponentScan: Diz ao Spring para procurar por componentes (controllers, services, etc.)
 *      a partir deste pacote e subpacotes
 * 
 * FUNCIONAMENTO:
 * - Quando executamos esta classe, o Spring Boot:
 *   1. Inicia um servidor web embarcado (Tomcat)
 *   2. Configura automaticamente componentes necessários
 *   3. Escaneia por controllers e outros componentes
 *   4. Disponibiliza a aplicação na porta 8080 por padrão
 * 
 * ESTRUTURA DO PACOTE:
 * - Esta classe deve estar no pacote raiz (com.projeto.steps)
 * - Todos os controllers devem estar em subpacotes (com.projeto.steps.controller)
 * - O Spring encontrará automaticamente todos os componentes nos subpacotes
 */
@SpringBootApplication
public class StepsApplication {

    /**
     * MÉTODO MAIN - PONTO DE ENTRADA DA APLICAÇÃO
     * 
     * Este é o método que será executado quando iniciarmos nossa aplicação.
     * É o mesmo conceito do main() de qualquer aplicação Java.
     * 
     * @param args - Argumentos da linha de comando (raramente usados em Spring Boot)
     * 
     * EXPLICAÇÃO DO SpringApplication.run():
     * - SpringApplication: Classe responsável por inicializar a aplicação Spring Boot
     * - .run(): Método que efetivamente inicia a aplicação
     * - StepsApplication.class: Referência para esta classe (ponto de partida do scan)
     * - args: Passa os argumentos da linha de comando para o Spring
     * 
     * O QUE ACONTECE QUANDO EXECUTAMOS:
     * 1. Spring Boot lê as configurações do application.properties
     * 2. Inicia o servidor web embarcado (Tomcat na porta 8080)
     * 3. Carrega todos os controllers e componentes encontrados
     * 4. Configura o Actuator com endpoints de monitoramento
     * 5. Aplicação fica disponível para receber requisições HTTP
     */
    public static void main(String[] args) {
        // Inicia a aplicação Spring Boot
        SpringApplication.run(StepsApplication.class, args);
        
        // Mensagens informativas para o desenvolvedor
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 APLICAÇÃO SPRING BOOT INICIADA COM SUCESSO!");
        System.out.println("=".repeat(60));
        System.out.println("📍 Aplicação principal: http://localhost:8080");
        System.out.println("📍 Página de boas-vindas: http://localhost:8080/");
        System.out.println("📍 API de agradecimento: http://localhost:8080/api/obrigado");
        System.out.println("=".repeat(60));
        System.out.println("📊 ENDPOINTS DO ACTUATOR (Monitoramento):");
        System.out.println("   • Health Check: http://localhost:8080/actuator/health");
        System.out.println("   • Informações: http://localhost:8080/actuator/info");
        System.out.println("   • Métricas: http://localhost:8080/actuator/metrics");
        System.out.println("   • Todos endpoints: http://localhost:8080/actuator");
        System.out.println("=".repeat(60));
        System.out.println("💡 Para parar a aplicação: Ctrl+C");
        System.out.println("💡 Para recompilar: mvn clean package");
        System.out.println("💡 Para executar: mvn spring-boot:run");
        System.out.println("=".repeat(60) + "\n");
    }
}