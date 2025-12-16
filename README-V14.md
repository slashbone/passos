# 🚀 Steps V1.4 - SQL Server Edition

## 📋 Visão Geral da Release 1.4

**Steps V1.4** é uma **evolução completa** do projeto original, agora com:
- 🗄️ **Microsoft SQL Server** rodando no **Podman** para testes
- 🎮 **Interface frontend completamente separada** 
- 🔧 **API REST completa** com CRUD (Criar, Ler, Atualizar, Deletar)
- 📊 **Sistema de estatísticas avançado**
- 🎯 **Experiência de usuário moderna e intuitiva**

---

## 🎯 **PARA USUÁRIOS INICIANTES: O QUE É CADA TECNOLOGIA**

### **🐳 O que é Podman?**
- **Podman** é como o Docker - uma ferramenta para rodar aplicações em "containers"
- Um **container** é como uma "caixinha isolada" que contém tudo que um programa precisa
- No nosso caso, usamos para rodar o **Microsoft SQL Server** sem precisar instalar ele direto no Windows

### **🗄️ O que é SQL Server?**
- **SQL Server** é um banco de dados da Microsoft
- É onde ficam guardados todos os dados dos usuários (nome, data de nascimento, etc.)
- É como um "arquivo Excel gigante" que nosso programa usa para salvar informações

### **🌐 O que é Frontend/Backend?**
- **Backend** = A parte "invisível" (API, banco de dados, lógica)
- **Frontend** = A parte que o usuário vê (telas, botões, formulários)
- **API** = "Ponte" que conecta o frontend com o backend

---

## 🛠️ **GUIA DE INSTALAÇÃO PASSO-A-PASSO**

### **📋 Pré-requisitos**

Você precisa ter instalado:

1. **Java JDK 21** ou superior
   - 📥 Download: https://adoptium.net/
   - ✅ Como verificar: Abra o cmd e digite `java --version`

2. **Maven 3.6+**
   - 📥 Download: https://maven.apache.org/download.cgi
   - ✅ Como verificar: No cmd digite `mvn --version`

3. **Podman Desktop**
   - 📥 Download: https://podman.io/getting-started/installation
   - ✅ Como verificar: No cmd digite `podman --version`

4. **Git** (opcional)
   - 📥 Download: https://git-scm.com/download/win

### **🚀 Instalação Rápida**

1. **📁 Baixe o projeto**
   ```bash
   # Se tiver Git
   git clone <url-do-repositorio>
   cd projeto-steps-V1
   
   # Mude para a branch V1.4
   git checkout release/1.4
   ```

2. **🗄️ Configure o SQL Server**
   ```bash
   # Execute o script de setup automático
   .\setup-sqlserver-podman.bat
   ```
   
   **O que esse script faz:**
   - Baixa a imagem do SQL Server 2022
   - Cria um container chamado "sqlserver-steps"
   - Configura usuário "sa" com senha "StepsPassword123!"
   - Cria banco de dados "steps_db"
   - Cria tabela "usuarios" com dados de teste
   - Testa se tudo está funcionando

3. **▶️ Execute a aplicação**
   ```bash
   # Execute o script principal
   .\executar-steps-v14.bat
   ```
   
   **O que esse script faz:**
   - Verifica se o SQL Server está rodando
   - Compila o projeto Spring Boot
   - Inicia a API na porta 8080
   - Abre automaticamente o frontend no navegador

---

## 🎮 **COMO USAR A APLICAÇÃO**

### **🌐 Acessando a Interface**

1. **Abrir o Frontend**
   - Execute: `.\abrir-frontend-v14.bat`
   - Ou abra diretamente: `frontend-v14\index.html`

2. **URLs importantes**
   - 🏠 **Interface**: `frontend-v14\index.html`
   - 🔧 **API Info**: http://localhost:8080/api/v1.4/info
   - 👥 **API Usuários**: http://localhost:8080/api/v1.4/usuarios
   - 💓 **Health Check**: http://localhost:8080/actuator/health

### **👥 Gerenciando Usuários**

#### **➕ Cadastrar Novo Usuário**
1. Preencha o **Nome Completo**
2. Selecione a **Data de Nascimento**
3. Clique em **"Cadastrar Usuário"**

#### **🔍 Buscar Usuários**
- **Por nome**: Digite parte do nome e clique em 🔍
- **Listar todos**: Clique em "Listar Todos"
- **Aniversariantes**: Clique em "Aniversariantes" 🎂

#### **✏️ Editar Usuário**
1. Clique no botão "Editar" ao lado do usuário
2. Modifique os dados no modal que abrir
3. Clique em "Salvar Alterações"

#### **🗑️ Excluir Usuário**
1. Clique no botão "Excluir" (vermelho)
2. Confirme a exclusão no alerta
3. O usuário será removido permanentemente

### **📊 Estatísticas Automáticas**

O sistema mostra automaticamente:
- **Total de usuários** cadastrados
- **Cadastros de hoje**
- **Aniversariantes de hoje** 🎂
- **Idade média** dos usuários

---

## 🔧 **COMANDOS ÚTEIS PARA GERENCIAR O SISTEMA**

### **🐳 Comandos do Podman (SQL Server)**

```bash
# Ver containers rodando
podman ps

# Parar SQL Server
podman stop sqlserver-steps

# Iniciar SQL Server
podman start sqlserver-steps

# Ver logs do SQL Server
podman logs sqlserver-steps

# Conectar no SQL Server (terminal)
podman exec -it sqlserver-steps bash

# Executar consulta SQL direta
podman exec sqlserver-steps /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "StepsPassword123!" -Q "SELECT * FROM steps_db.dbo.usuarios"
```

### **🚀 Comandos do Spring Boot**

```bash
# Entrar no diretório backend
cd backend

# Compilar projeto
mvn clean compile

# Executar com perfil SQL Server
mvn spring-boot:run -Dspring-boot.run.profiles=sqlserver

# Executar testes
mvn test

# Gerar JAR
mvn clean package
```

### **🌐 Comandos para Frontend**

```bash
# Abrir frontend
.\abrir-frontend-v14.bat

# Servir com Python (alternativa)
cd frontend-v14
python -m http.server 8000

# Servir com Node.js (alternativa)
cd frontend-v14
npx serve .
```

---

## 🏗️ **ARQUITETURA TÉCNICA DETALHADA**

### **📁 Estrutura de Diretórios**

```
projeto-steps-V1/
├── 📂 backend/                           # API Spring Boot
│   ├── 📂 src/main/java/com/projeto/steps/
│   │   ├── 📄 StepsApplication.java      # Classe principal
│   │   ├── 📂 entity/
│   │   │   └── 📄 Usuario.java           # Entidade JPA
│   │   ├── 📂 repository/
│   │   │   └── 📄 UsuarioJpaRepository.java # Repositório JPA
│   │   └── 📂 controller/
│   │       └── 📄 UsuarioCrudController.java # API REST
│   ├── 📂 src/main/resources/
│   │   ├── 📄 application.properties     # Config principal
│   │   └── 📄 application-sqlserver.properties # Config SQL Server
│   └── 📄 pom.xml                        # Dependências Maven
├── 📂 frontend-v14/                      # Interface separada
│   ├── 📄 index.html                     # Página principal
│   ├── 📄 styles.css                     # Estilos CSS
│   └── 📄 script.js                      # Lógica JavaScript
├── 📄 setup-sqlserver-podman.bat         # Setup automático SQL Server
├── 📄 executar-steps-v14.bat             # Executar aplicação completa
├── 📄 abrir-frontend-v14.bat             # Abrir só o frontend
└── 📄 README-V14.md                      # Esta documentação
```

### **🔄 Fluxo de Dados**

1. **Frontend** (HTML/CSS/JS) → 
2. **API REST** (Spring Boot) → 
3. **JPA/Hibernate** (ORM) → 
4. **SQL Server** (Podman Container)

### **🎯 Endpoints da API V1.4**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/v1.4/usuarios` | Lista todos os usuários |
| `GET` | `/api/v1.4/usuarios/{id}` | Busca usuário por ID |
| `GET` | `/api/v1.4/usuarios/nome/{nome}` | Busca por nome (parcial) |
| `GET` | `/api/v1.4/usuarios/aniversario` | Aniversariantes de hoje |
| `POST` | `/api/v1.4/usuarios` | Cria novo usuário |
| `PUT` | `/api/v1.4/usuarios/{id}` | Atualiza usuário |
| `DELETE` | `/api/v1.4/usuarios/{id}` | Exclui usuário |
| `GET` | `/api/v1.4/estatisticas` | Estatísticas do sistema |
| `GET` | `/api/v1.4/info` | Informações da API |

---

## 🐛 **SOLUCIONANDO PROBLEMAS COMUNS**

### **❌ "Podman não encontrado"**
**Problema**: Script não consegue executar comandos Podman
**Solução**: 
1. Instale o Podman Desktop: https://podman.io/getting-started/installation
2. Reinicie o computador após a instalação
3. Abra um novo terminal e teste: `podman --version`

### **❌ "Porta 8080 já está em uso"**
**Problema**: Outra aplicação está usando a porta 8080
**Solução**: 
1. Descubra o que está usando: `netstat -ano | findstr :8080`
2. Mate o processo: `taskkill /PID [numero_do_processo] /F`
3. Ou mude a porta no `application-sqlserver.properties`

### **❌ "SQL Server não está respondendo"**
**Problema**: Container SQL Server não inicializa
**Soluções**:
1. Verifique logs: `podman logs sqlserver-steps`
2. Remova e recrie: `podman rm sqlserver-steps` depois `.\setup-sqlserver-podman.bat`
3. Verifique se tem espaço em disco suficiente (mín. 2GB)

### **❌ "Erro de compilação Maven"**
**Problema**: Maven não consegue baixar dependências
**Soluções**:
1. Verifique internet
2. Limpe cache: `mvn clean`
3. Force download: `mvn clean compile -U`

### **❌ "Frontend não conecta com API"**
**Problema**: Erro de CORS ou API não responde
**Soluções**:
1. Verifique se API está rodando: http://localhost:8080/actuator/health
2. Verifique console do navegador (F12)
3. Confirme que `spring.profiles.active=sqlserver` está correto

### **❌ "Dados não aparecem no frontend"**
**Problema**: Interface carrega mas não mostra usuários
**Soluções**:
1. Verifique se SQL Server tem dados: `podman exec sqlserver-steps /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "StepsPassword123!" -Q "SELECT COUNT(*) FROM steps_db.dbo.usuarios"`
2. Verifique logs da API no terminal
3. Teste API diretamente: http://localhost:8080/api/v1.4/usuarios

---

## 🎯 **TESTANDO SE TUDO FUNCIONA**

### **✅ Checklist de Verificação**

1. **Podman funcionando**
   ```bash
   podman --version
   # Deve mostrar: podman version x.x.x
   ```

2. **SQL Server rodando**
   ```bash
   podman ps
   # Deve mostrar container "sqlserver-steps" com status "Up"
   ```

3. **API respondendo**
   - Abra: http://localhost:8080/actuator/health
   - Deve mostrar: `{"status":"UP"}`

4. **Banco de dados com dados**
   ```bash
   podman exec sqlserver-steps /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "StepsPassword123!" -Q "SELECT COUNT(*) FROM steps_db.dbo.usuarios"
   # Deve mostrar número > 0
   ```

5. **Frontend conectando**
   - Abra: `frontend-v14\index.html`
   - Deve mostrar estatísticas e lista de usuários

### **🧪 Teste Completo CRUD**

1. **Criar usuário**
   - Preencha nome: "Teste Usuário"
   - Data: Uma data no passado
   - Clique "Cadastrar"
   - Deve aparecer mensagem de sucesso

2. **Listar usuários**
   - Clique "Listar Todos"
   - Deve aparecer "Teste Usuário" na lista

3. **Buscar usuário**
   - Digite "Teste" na busca
   - Clique no botão de busca
   - Deve encontrar "Teste Usuário"

4. **Editar usuário**
   - Clique "Editar" no usuário
   - Mude o nome para "Teste Editado"
   - Salve
   - Deve aparecer com novo nome

5. **Excluir usuário**
   - Clique "Excluir" no usuário
   - Confirme a exclusão
   - Usuário deve desaparecer da lista

---

## 🎨 **PERSONALIZAÇÃO E EXTENSÕES**

### **🎯 Como Adicionar Novos Campos**

**Exemplo: Adicionar campo "Email"**

1. **Atualizar Entity** (`Usuario.java`):
   ```java
   @Column(name = "email")
   private String email;
   
   // Adicionar getter/setter
   ```

2. **Atualizar Frontend** (`index.html`):
   ```html
   <div class="form-group">
       <label for="email">Email</label>
       <input type="email" id="email" name="email">
   </div>
   ```

3. **Atualizar JavaScript** (`script.js`):
   ```javascript
   // Adicionar no objeto userData
   email: formData.get('email')
   ```

### **🎨 Mudando Cores e Estilo**

Edite `styles.css` e modifique as variáveis CSS:
```css
:root {
    --primary-color: #2563eb;  /* Cor principal */
    --success-color: #059669;  /* Cor de sucesso */
    --danger-color: #dc2626;   /* Cor de erro */
}
```

### **📊 Adicionando Novas Estatísticas**

1. **No Controller** (`UsuarioCrudController.java`):
   ```java
   // Adicionar nova consulta no repository
   // Incluir no endpoint /estatisticas
   ```

2. **No Frontend** (`script.js`):
   ```javascript
   // Atualizar função displayStatistics()
   // Adicionar novo card de estatística
   ```

---

## 📞 **SUPORTE E PRÓXIMOS PASSOS**

### **🆘 Onde Buscar Ajuda**

1. **Documentação oficial**:
   - Spring Boot: https://spring.io/projects/spring-boot
   - Podman: https://docs.podman.io/
   - SQL Server: https://docs.microsoft.com/sql/

2. **Comunidades**:
   - Stack Overflow (português)
   - GitHub Issues do projeto
   - Discord/Slack da comunidade Java

### **🚀 Ideias para Evolução**

1. **Funcionalidades**:
   - 🔐 Sistema de login/autenticação
   - 📁 Upload de arquivos/fotos
   - 📊 Relatórios em PDF
   - 📧 Envio de emails
   - 🔔 Notificações push

2. **Tecnologias**:
   - 🌐 Deploy na nuvem (Azure/AWS)
   - 🐳 Docker Compose
   - 🔄 CI/CD com GitHub Actions
   - 📱 App mobile (React Native/Flutter)

3. **Melhorias**:
   - 🧪 Testes automatizados
   - 📈 Monitoramento/métricas
   - 🔒 Segurança avançada
   - ⚡ Cache Redis
   - 📦 Microserviços

---

## 🎉 **CONCLUSÃO**

**Parabéns!** 🎊 Você agora tem:

- ✅ Um sistema CRUD completo e funcional
- ✅ Interface moderna e responsiva
- ✅ Banco SQL Server profissional
- ✅ API REST documentada
- ✅ Ambiente containerizado
- ✅ Scripts de automação
- ✅ Documentação completa

**Steps V1.4** é uma base sólida para projetos maiores e mais complexos. Use este conhecimento para construir aplicações ainda mais incríveis! 🚀

---

**💡 Lembre-se**: Programação é uma jornada de aprendizado constante. Continue praticando, experimentando e construindo coisas novas!

**🎯 Próximo desafio**: Que tal adicionar autenticação de usuários ou deploy na nuvem? 😉