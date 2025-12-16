# 🚀 Projeto Steps V1 - Aplicação Web Spring Boot

## 📋 Descrição

Este projeto é uma **aplicação web completa** desenvolvida com **Spring Boot** e **Actuator** que apresenta uma interface moderna de boas-vindas. A aplicação foi refatorada de uma simples interface Java Swing para uma **arquitetura profissional web** com separação clara entre **frontend** e **backend**.

### ✨ **Funcionalidades Principais:**
- 🎯 **Interface web responsiva** com mensagem "Bem-vindo!"  
- 🔘 **Botão interativo** que se comunica com API REST
- 📊 **Monitoramento completo** com Spring Boot Actuator
- 🔄 **Verificação automática** de status da API
- 📱 **Design responsivo** para desktop e mobile
- ⚡ **Hot-reload** para desenvolvimento ágil
- 📥 **Importação CSV** para carregamento em massa de dados

### 🏗️ **Arquitetura Moderna:**
- **Backend**: Spring Boot 3.2.1 + Actuator + DevTools
- **Frontend**: HTML5 + CSS3 + JavaScript ES6+  
- **API**: REST endpoints com JSON
- **Monitoramento**: Actuator endpoints (/health, /metrics, /info)
- **Build**: Maven 3.x
- **Java**: 17+

## 📁 Estrutura de Diretórios

```
projeto-steps-V1/
├── 📂 backend/                          # Backend Spring Boot
│   ├── 📂 src/
│   │   └── 📂 main/
│   │       ├── 📂 java/
│   │       │   └── 📂 com/
│   │       │       └── 📂 projeto/
│   │       │           └── 📂 steps/
│   │       │               ├── 📄 StepsApplication.java      # Classe principal
│   │       │               └── 📂 controller/
│   │       │                   └── 📄 WelcomeController.java # Controller REST
│   │       └── 📂 resources/
│   │           ├── 📄 application.properties   # Configurações
│   │           └── 📂 static/                  # Arquivos web estáticos
│   │               ├── 📄 index.html           # Página principal
│   │               ├── 📄 styles.css           # Estilos CSS
│   │               └── 📄 script.js            # JavaScript
│   └── 📄 pom.xml                              # Configuração Maven
├── 📂 frontend/                         # Frontend independente
│   ├── 📄 index.html                    # Versão desenvolvimento
│   ├── 📄 styles.css                    # Estilos desenvolvimento  
│   └── 📄 script.js                     # JavaScript desenvolvimento
├── 📄 executar-spring-boot.bat          # Script execução rápida
├── 📄 build-e-executar.bat              # Script build completo
└── 📄 README.md                         # Esta documentação
```

## 🔧 Pré-requisitos

### **Obrigatório:**
- ☕ **Java 17+** ([Download OpenJDK](https://adoptium.net/))
- 📦 **Maven 3.6+** ([Download Maven](https://maven.apache.org/download.cgi))

### **Opcional (para desenvolvimento):**
- 🆔 **IDE**: IntelliJ IDEA, Eclipse ou VS Code
- 🌐 **Navegador moderno**: Chrome, Firefox, Edge

### **Verificação de Instalação:**
```bash
# Verificar Java
java --version

# Verificar Maven  
mvn --version
```

## 🚀 Como Executar

### **Método 1: Execução Rápida (Recomendado)**
```bash
# Execute o script que faz tudo automaticamente
executar-spring-boot.bat
```

### **Método 2: Build Completo com JAR**
```bash  
# Build, testes e execução do JAR final
build-e-executar.bat
```

### **Método 3: Comandos Maven Manuais**
```bash
# Navegar para diretório backend
cd backend

# Compilar aplicação
mvn clean compile

# Executar em modo desenvolvimento
mvn spring-boot:run

# OU: Criar JAR e executar
mvn clean package
java -jar target/steps-backend-1.0.0.jar
```

### **📍 URLs da Aplicação:**
Após iniciar, a aplicação estará disponível em:
- 🏠 **Aplicação principal**: http://localhost:8080
- 🔍 **Health Check**: http://localhost:8080/actuator/health  
- 📊 **Métricas**: http://localhost:8080/actuator/metrics
- 📋 **Informações**: http://localhost:8080/actuator/info
- 🎛️ **Todos endpoints**: http://localhost:8080/actuator

## � **Importação de Dados via CSV**

### **🚀 Nova Funcionalidade: Script de Importação em Lote**

O projeto agora inclui um script automatizado para importação de dados em massa:

#### **📋 Como Usar:**
```bash
# 1. Criar arquivo CSV com dados
notepad cadastro-mongo.csv

# 2. Executar script de importação
.\importar-csv-mongodb.bat
```

#### **🗂️ Formato do Arquivo CSV:**
```csv
Nome Completo,Data Nascimento  
João Silva Santos,15051985
Maria Oliveira Costa,23081990
Pedro Almeida Souza,08121978
```

**🎯 Características:**
- ✅ **Formato simples**: Nome completo + Data (ddmmaaaa)
- ✅ **Conversão automática** de datas para formato ISO
- ✅ **Relatório detalhado** com sucessos e erros
- ✅ **Validação prévia** da API antes da importação
- ✅ **Tratamento individual** de registros com erro

**📊 O script fornece:**
- Contagem total de registros processados
- Número de sucessos e erros
- IDs dos registros inseridos com sucesso
- Verificação pós-importação do banco de dados

## �📚 Explicação Detalhada dos Arquivos (Para Iniciantes)

### 🎯 **Backend (Spring Boot)**

#### `StepsApplication.java` - Classe Principal
```java
@SpringBootApplication  // Configura Spring Boot automaticamente
public class StepsApplication {
    public static void main(String[] args) {
        SpringApplication.run(StepsApplication.class, args);
    }
}
```
**O que faz:** É o "coração" da aplicação. Quando executada, inicia todo o servidor web Spring Boot.

#### `WelcomeController.java` - Controller REST
```java
@RestController  // Indica que esta classe responde requisições HTTP
public class WelcomeController {
    @GetMapping("/")          // Responde GET em http://localhost:8080/
    @PostMapping("/api/obrigado")  // Responde POST em /api/obrigado
}
```
**O que faz:** Define quais URLs a aplicação vai responder e o que fazer quando acessadas.

#### `application.properties` - Configurações
```properties
server.port=8080                    # Porta do servidor
management.endpoints.web.exposure.include=*  # Habilita Actuator
```
**O que faz:** Arquivo de configuração que define comportamentos da aplicação.

#### `pom.xml` - Configuração Maven
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
**O que faz:** Lista todas as bibliotecas (dependências) que a aplicação precisa.

### 🎨 **Frontend (Web)**

#### `index.html` - Estrutura da Página
```html
<button id="thankButton">Clique Aqui</button>
<div id="thankMessage">Obrigado!</div>
```
**O que faz:** Define a estrutura visual da página (botões, textos, layouts).

#### `styles.css` - Visual e Design
```css
.action-button {
    background: linear-gradient(45deg, #4682b4, #5a9fd4);
    border-radius: 25px;
}
```
**O que faz:** Define cores, tamanhos, animações e toda aparência visual.

#### `script.js` - Lógica e Interação
```javascript
fetch('/api/obrigado', { method: 'POST' })  // Chama API
    .then(response => response.json())       // Converte resposta
    .then(data => showMessage(data.message)) // Exibe resultado
```
**O que faz:** Faz a página "funcionar" - responde a cliques, comunica com API.

## ⚙️ Como Tudo Funciona Juntos

### 🔄 **Fluxo da Aplicação:**

1. **Usuário acessa** http://localhost:8080
2. **Spring Boot** serve o arquivo `index.html` 
3. **Navegador** carrega HTML, CSS e JavaScript
4. **JavaScript** verifica se API está funcionando
5. **Usuário clica** no botão "Clique Aqui"
6. **JavaScript** faz requisição POST para `/api/obrigado`
7. **WelcomeController** recebe requisição e processa
8. **Controller** retorna JSON `{"message": "Obrigado!"}`
9. **JavaScript** recebe resposta e exibe na tela
10. **Usuário vê** mensagem "Obrigado!" com animação

### 🏗️ **Arquitetura (Conceitos):**

- **Frontend**: Interface que usuário vê e interage
- **Backend**: Servidor que processa lógica e dados  
- **API REST**: "ponte" de comunicação entre frontend e backend
- **JSON**: Formato de dados trocados entre frontend/backend
- **HTTP**: Protocolo de comunicação web (GET, POST, etc.)
- **Actuator**: Ferramenta de monitoramento do Spring Boot

## 🔧 Desenvolvimento e Personalização

### **🎨 Modificar Aparência:**
- **Cores**: Editar `styles.css` - alterar valores de `color` e `background`
- **Textos**: Editar `index.html` - alterar conteúdo das tags
- **Animações**: Modificar `@keyframes` no CSS
- **Layout**: Ajustar propriedades `flexbox` e `grid`

### **⚙️ Modificar Funcionalidade:**
- **Endpoints**: Adicionar novos métodos em `WelcomeController.java`
- **Lógica**: Editar `script.js` para novas interações
- **Configurações**: Alterar `application.properties`
- **Dependências**: Modificar `pom.xml`

### **📊 Monitoramento Avançado:**
O **Spring Boot Actuator** fornece endpoints poderosos:
- `/actuator/health` - Status de saúde da aplicação
- `/actuator/metrics` - Métricas de performance
- `/actuator/env` - Variáveis de ambiente
- `/actuator/loggers` - Configuração de logs
- `/actuator/httptrace` - Histórico de requisições HTTP

## 🐛 Resolução de Problemas

### **❌ Erro "Port 8080 already in use":**
```bash
# Encontrar processo usando porta 8080
netstat -ano | findstr :8080

# Matar processo (substituir PID)
taskkill /PID <numero_pid> /F

# OU alterar porta no application.properties
server.port=8081
```

### **❌ Erro "Java not found":**
```bash
# Verificar Java instalado
java -version

# Configurar JAVA_HOME (Windows)
set JAVA_HOME=C:\Program Files\Java\jdk-17

# Adicionar ao PATH
set PATH=%JAVA_HOME%\bin;%PATH%
```

### **❌ Erro "Maven not found":**
```bash
# Baixar Maven: https://maven.apache.org/download.cgi
# Configurar MAVEN_HOME
set MAVEN_HOME=C:\apache-maven-3.9.6
set PATH=%MAVEN_HOME%\bin;%PATH%
```

### **🔍 Debug Mode:**
```bash
# Executar com logs detalhados
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug"

# Ver logs da aplicação
tail -f logs/spring-boot.log
```

---

## 🛠️ **Scripts Disponíveis**

### **📁 Scripts de Execução:**
- `executar-spring-boot.bat` - Execução rápida do projeto
- `build-e-executar.bat` - Build completo + execução do JAR

### **📥 Scripts de Importação:**
- `importar-csv-mongodb.bat` - Importação em massa via CSV
- `cadastro-mongo.csv` - Arquivo exemplo com 10 registros de teste

### **📋 Exemplo de Uso Completo:**
```bash
# 1. Executar o projeto
.\executar-spring-boot.bat

# 2. Aguardar inicialização completa
# 3. Em outro terminal, executar importação
.\importar-csv-mongodb.bat

# 4. Verificar resultados em http://localhost:8080
```

---

## 📞 **Suporte e Contato**

Para dúvidas ou sugestões sobre o projeto ou os novos scripts de importação:
- 🐛 **Issues**: Criar issue no repositório
- 💡 **Melhorias**: Pull requests são bem-vindos
- 📚 **Documentação**: Consultar `VERSAO-V131-DOCUMENTACAO.md` para detalhes técnicos

---

**🎉 Projeto Steps V1 - Agora com importação automatizada de dados! 🚀**

## 📈 Próximos Passos

### **🚀 Melhorias Possíveis:**
- 🗄️ **Banco de Dados**: Adicionar H2, PostgreSQL ou MySQL
- 🔐 **Segurança**: Implementar Spring Security
- 📧 **API Externa**: Integrar envio de emails
- 🧪 **Testes**: Adicionar testes unitários e integração  
- 🐳 **Docker**: Containerizar aplicação
- ☁️ **Deploy**: Publicar no Heroku, AWS ou Azure

### **📚 Tecnologias para Aprender:**
- **Spring Data JPA**: Para bancos de dados
- **Spring Security**: Para autenticação
- **Thymeleaf**: Para templates HTML
- **React/Vue**: Para frontend mais avançado
- **Docker**: Para containerização
- **Kubernetes**: Para orquestração

## 👨‍💻 Autor e Contribuição

**Projeto Steps V1** - Aplicação educativa Spring Boot  
📧 Email: desenvolvimento@projetosteps.com  
🌐 Versão: 1.0.0  
📅 Última atualização: Dezembro 2025

### **🤝 Como Contribuir:**
1. Fork este projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

---

**⭐ Se este projeto foi útil, considere dar uma estrela no GitHub!**