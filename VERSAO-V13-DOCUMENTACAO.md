# 📚 DOCUMENTAÇÃO VERSÃO 1.3 - INTEGRAÇÃO MONGODB

## 🎯 VISÃO GERAL

A Versão 1.3 do Projeto Steps adiciona integração completa com **MongoDB**, um banco de dados NoSQL que permite armazenar dados de usuários (nome e data de nascimento) de forma persistente.

### ✨ Novidades da V1.3:
- ✅ Cadastro de usuários com nome e data de nascimento
- ✅ Armazenamento persistente em MongoDB
- ✅ API REST completa (CRUD)
- ✅ Frontend interativo para cadastro e visualização
- ✅ Container MongoDB com Podman
- ✅ Validações de dados

---

## 📖 O QUE É MONGODB? (EXPLICAÇÃO PARA INICIANTES)

### Conceitos Básicos

**MongoDB** é um banco de dados NoSQL (Not Only SQL), diferente dos bancos de dados tradicionais como MySQL ou PostgreSQL.

#### Principais Diferenças:

| Banco SQL (ex: MySQL) | MongoDB (NoSQL) |
|------------------------|-----------------|
| Armazena dados em **tabelas** | Armazena dados em **coleções** |
| Cada linha é um **registro** | Cada documento é um **objeto JSON** |
| Estrutura rígida (schema fixo) | Estrutura flexível (schema dinâmico) |
| Usa SQL para consultas | Usa consultas em formato JSON |

### Estrutura do MongoDB

```
MongoDB Server (Instância do MongoDB)
└── Banco de Dados: "passos"
    └── Coleção: "dados_usuario"
        ├── Documento 1: { "_id": "...", "nome": "João Silva", "dataNascimento": "1990-05-15" }
        ├── Documento 2: { "_id": "...", "nome": "Maria Santos", "dataNascimento": "1985-08-20" }
        └── Documento 3: { "_id": "...", "nome": "Pedro Costa", "dataNascimento": "1992-12-10" }
```

### Por que usar MongoDB?

1. **Flexibilidade**: Não precisa definir estrutura antes de inserir dados
2. **Velocidade**: Muito rápido para leitura e escrita
3. **Escalabilidade**: Fácil de escalar horizontalmente
4. **JSON nativo**: Trabalha naturalmente com JavaScript/JSON
5. **Sem JOINs complexos**: Dados relacionados podem ser armazenados juntos

---

## 🐳 INSTALAÇÃO DO MONGODB COM PODMAN

### O que é Podman?

**Podman** é uma ferramenta para executar containers (como o Docker, mas sem daemon). Containers são como "caixas isoladas" que contêm tudo que um software precisa para rodar.

### Passo 1: Verificar se Podman está instalado

Abra o PowerShell e execute:

```powershell
podman --version
```

**Resultado esperado:**
```
podman version 4.x.x
```

**Se não estiver instalado:**
- Windows: https://podman.io/getting-started/installation#windows
- Execute o instalador e reinicie o computador

### Passo 2: Criar e executar container MongoDB

Execute o seguinte comando no PowerShell:

```powershell
podman run -d `
  --name mongodb-steps `
  -p 27017:27017 `
  -e MONGO_INITDB_DATABASE=passos `
  -v mongodb-data:/data/db `
  mongo:latest
```

#### 📝 Explicação de cada parte do comando:

| Parâmetro | O que faz |
|-----------|-----------|
| `podman run` | Comando para criar e executar um container |
| `-d` | **Detached mode** - Executa em segundo plano (não trava o terminal) |
| `--name mongodb-steps` | Dá um nome ao container (mongodb-steps) para fácil identificação |
| `-p 27017:27017` | **Port mapping** - Mapeia porta 27017 do container para porta 27017 do seu computador |
| `-e MONGO_INITDB_DATABASE=passos` | **Environment variable** - Cria automaticamente o banco "passos" |
| `-v mongodb-data:/data/db` | **Volume** - Cria armazenamento persistente para os dados (não perde dados ao reiniciar) |
| `mongo:latest` | Imagem do MongoDB a ser baixada e executada (versão mais recente) |

### Passo 3: Verificar se MongoDB está rodando

```powershell
podman ps
```

**Resultado esperado:**
```
CONTAINER ID  IMAGE                            COMMAND     CREATED        STATUS        PORTS                     NAMES
abc123def456  docker.io/library/mongo:latest   mongod      2 minutes ago  Up 2 minutes  0.0.0.0:27017->27017/tcp  mongodb-steps
```

✅ **Se você ver uma linha com "mongodb-steps" e STATUS "Up", está funcionando!**

### Passo 4: Testar conexão com MongoDB

```powershell
podman exec -it mongodb-steps mongosh
```

Isso abre o shell do MongoDB. Você verá algo como:

```
Current Mongosh Log ID: ...
Connecting to: mongodb://127.0.0.1:27017/?directConnection=true
Using MongoDB: 7.x.x

test>
```

#### Comandos úteis no MongoDB Shell:

```javascript
// Listar todos os bancos de dados
show dbs

// Usar o banco "passos"
use passos

// Listar coleções do banco atual
show collections

// Ver documentos na coleção "dados_usuario"
db.dados_usuario.find()

// Contar quantos documentos existem
db.dados_usuario.countDocuments()

// Sair do MongoDB Shell
exit
```

### Passo 5: Comandos úteis do Podman

```powershell
# Ver logs do MongoDB
podman logs mongodb-steps

# Ver logs em tempo real (seguir)
podman logs -f mongodb-steps

# Parar o MongoDB
podman stop mongodb-steps

# Iniciar o MongoDB (se estiver parado)
podman start mongodb-steps

# Reiniciar o MongoDB
podman restart mongodb-steps

# Remover o container (ATENÇÃO: remove os dados se não usar volume)
podman rm -f mongodb-steps

# Ver uso de recursos do container
podman stats mongodb-steps
```

---

## 🏗️ ESTRUTURA DO PROJETO V1.3

### Novos arquivos criados:

```
backend/
├── pom.xml                                    [ATUALIZADO] - Adicionada dependência MongoDB
├── src/main/
│   ├── java/com/projeto/steps/
│   │   ├── model/
│   │   │   └── Usuario.java                  [NOVO] - Modelo de dados do usuário
│   │   ├── repository/
│   │   │   └── UsuarioRepository.java        [NOVO] - Interface para acesso ao MongoDB
│   │   └── controller/
│   │       └── WelcomeControllerV13.java     [NOVO] - Controller com endpoints CRUD
│   └── resources/
│       ├── application.properties            [ATUALIZADO] - Configurações do MongoDB
│       └── static/
│           └── demo-v13.html                 [NOVO] - Interface web para cadastro
```

---

## 🔧 CONFIGURAÇÃO DO PROJETO

### 1. Dependência Maven (pom.xml)

Adicionada a dependência Spring Data MongoDB:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

### 2. Configurações (application.properties)

Configurações do MongoDB adicionadas:

```properties
# URI de conexão com MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/passos

# Nome do banco de dados
spring.data.mongodb.database=passos

# Auto-criação de índices
spring.data.mongodb.auto-index-creation=true

# Repositórios MongoDB habilitados
spring.data.mongodb.repositories.enabled=true
```

#### Explicação das configurações:

- **URI**: Endereço completo do MongoDB (protocolo://host:porta/banco)
- **Database**: Nome do banco de dados (criado automaticamente se não existir)
- **Auto-index-creation**: Cria índices automaticamente nas entidades
- **Repositories**: Habilita os repositórios Spring Data MongoDB

---

## 💻 COMPONENTES DO BACKEND

### 1. Model: Usuario.java

**Localização:** `backend/src/main/java/com/projeto/steps/model/Usuario.java`

**Responsabilidade:** Define a estrutura dos dados do usuário.

#### Estrutura da classe:

```java
@Document(collection = "dados_usuario")
public class Usuario {
    @Id
    private String id;              // ID único gerado pelo MongoDB
    private String nome;            // Nome completo do usuário
    private LocalDate dataNascimento; // Data de nascimento
    
    // Construtores, getters e setters...
}
```

#### Anotações importantes:

- `@Document(collection = "dados_usuario")`: Marca a classe como um documento MongoDB
- `@Id`: Indica o campo identificador único (gerado automaticamente)

#### Exemplo de documento no MongoDB:

```json
{
  "_id": "65a1b2c3d4e5f6789012345",
  "nome": "João Silva",
  "dataNascimento": "1990-05-15"
}
```

### 2. Repository: UsuarioRepository.java

**Localização:** `backend/src/main/java/com/projeto/steps/repository/UsuarioRepository.java`

**Responsabilidade:** Interface para operações no banco de dados.

#### Métodos automáticos (fornecidos pelo Spring):

```java
// Salvar ou atualizar usuário
Usuario save(Usuario usuario)

// Buscar usuário por ID
Optional<Usuario> findById(String id)

// Buscar todos os usuários
List<Usuario> findAll()

// Deletar usuário por ID
void deleteById(String id)

// Contar usuários
long count()
```

#### Métodos customizados criados:

```java
// Buscar por nome exato
List<Usuario> findByNome(String nome)

// Buscar por nome contendo texto
List<Usuario> findByNomeContaining(String nome)

// Verificar se existe
boolean existsByNome(String nome)

// Contar por nome
long countByNome(String nome)
```

**Como funciona a mágica?**
- O Spring lê o nome do método (ex: `findByNome`)
- Identifica o campo (`Nome`)
- Cria automaticamente a query MongoDB correspondente
- Você não precisa escrever código de acesso ao banco!

### 3. Controller: WelcomeControllerV13.java

**Localização:** `backend/src/main/java/com/projeto/steps/controller/WelcomeControllerV13.java`

**Responsabilidade:** Recebe requisições HTTP e processa dados.

#### Endpoints disponíveis:

| Método HTTP | Endpoint | Descrição |
|-------------|----------|-----------|
| GET | `/api/v1.3/` | Informações da API |
| POST | `/api/v1.3/usuarios` | Cadastra novo usuário |
| GET | `/api/v1.3/usuarios` | Lista todos os usuários |
| GET | `/api/v1.3/usuarios/{id}` | Busca usuário por ID |
| GET | `/api/v1.3/usuarios/buscar?nome=` | Busca por nome |
| DELETE | `/api/v1.3/usuarios/{id}` | Deleta usuário |
| GET | `/api/v1.3/estatisticas` | Estatísticas gerais |

---

## 🌐 API REST - GUIA COMPLETO

### Endpoint 1: Cadastrar Usuário

**Método:** POST  
**URL:** `http://localhost:8080/api/v1.3/usuarios`  
**Content-Type:** `application/json`

#### Requisição:

```json
{
  "nome": "João Silva",
  "dataNascimento": "1990-05-15"
}
```

#### Resposta de Sucesso (201 Created):

```json
{
  "mensagem": "Usuário cadastrado com sucesso!",
  "usuario": {
    "id": "65a1b2c3d4e5f6789012345",
    "nome": "João Silva",
    "dataNascimento": "1990-05-15"
  },
  "timestamp": "2024-12-16T14:30:00"
}
```

#### Resposta de Erro (400 Bad Request):

```json
{
  "erro": "Nome é obrigatório",
  "campo": "nome"
}
```

#### Testando com PowerShell:

```powershell
$body = @{
    nome = "João Silva"
    dataNascimento = "1990-05-15"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/v1.3/usuarios" `
  -Method POST `
  -Body $body `
  -ContentType "application/json"
```

#### Testando com curl:

```bash
curl -X POST http://localhost:8080/api/v1.3/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva","dataNascimento":"1990-05-15"}'
```

### Endpoint 2: Listar Todos os Usuários

**Método:** GET  
**URL:** `http://localhost:8080/api/v1.3/usuarios`

#### Resposta:

```json
{
  "total": 2,
  "usuarios": [
    {
      "id": "65a1b2c3d4e5f6789012345",
      "nome": "João Silva",
      "dataNascimento": "1990-05-15"
    },
    {
      "id": "65a1b2c3d4e5f6789012346",
      "nome": "Maria Santos",
      "dataNascimento": "1985-08-20"
    }
  ],
  "timestamp": "2024-12-16T14:35:00"
}
```

#### Testando com PowerShell:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1.3/usuarios"
```

### Endpoint 3: Buscar Usuário por ID

**Método:** GET  
**URL:** `http://localhost:8080/api/v1.3/usuarios/{id}`

#### Exemplo:

```
GET http://localhost:8080/api/v1.3/usuarios/65a1b2c3d4e5f6789012345
```

#### Resposta:

```json
{
  "usuario": {
    "id": "65a1b2c3d4e5f6789012345",
    "nome": "João Silva",
    "dataNascimento": "1990-05-15"
  },
  "timestamp": "2024-12-16T14:40:00"
}
```

### Endpoint 4: Buscar Usuários por Nome

**Método:** GET  
**URL:** `http://localhost:8080/api/v1.3/usuarios/buscar?nome={texto}`

#### Exemplo:

```
GET http://localhost:8080/api/v1.3/usuarios/buscar?nome=Silva
```

#### Resposta:

```json
{
  "termo_busca": "Silva",
  "total_encontrado": 1,
  "usuarios": [
    {
      "id": "65a1b2c3d4e5f6789012345",
      "nome": "João Silva",
      "dataNascimento": "1990-05-15"
    }
  ],
  "timestamp": "2024-12-16T14:45:00"
}
```

### Endpoint 5: Deletar Usuário

**Método:** DELETE  
**URL:** `http://localhost:8080/api/v1.3/usuarios/{id}`

#### Exemplo:

```
DELETE http://localhost:8080/api/v1.3/usuarios/65a1b2c3d4e5f6789012345
```

#### Resposta de Sucesso:

```json
{
  "mensagem": "Usuário deletado com sucesso",
  "id": "65a1b2c3d4e5f6789012345",
  "timestamp": "2024-12-16T14:50:00"
}
```

#### Testando com PowerShell:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1.3/usuarios/65a1b2c3d4e5f6789012345" `
  -Method DELETE
```

### Endpoint 6: Estatísticas

**Método:** GET  
**URL:** `http://localhost:8080/api/v1.3/estatisticas`

#### Resposta:

```json
{
  "total_usuarios": 5,
  "banco_dados": "passos",
  "colecao": "dados_usuario",
  "timestamp": "2024-12-16T14:55:00"
}
```

---

## 🖥️ FRONTEND - INTERFACE WEB

### Acessando a Interface

1. Certifique-se que o backend está rodando
2. Abra o navegador e acesse:

```
http://localhost:8080/demo-v13.html
```

### Funcionalidades da Interface:

#### 1. Formulário de Cadastro
- Campo "Nome Completo" (obrigatório, mínimo 3 caracteres)
- Campo "Data de Nascimento" (obrigatório, não aceita datas futuras)
- Botão "Cadastrar no MongoDB"
- Mensagens de sucesso/erro
- Limpeza automática do formulário após cadastro

#### 2. Lista de Usuários
- Exibe todos os usuários cadastrados
- Atualização em tempo real
- Informações exibidas:
  - Nome completo
  - Data de nascimento formatada
  - ID do MongoDB
- Botão para deletar cada usuário
- Atualização automática a cada 30 segundos

#### 3. Estatísticas
- Total de usuários cadastrados
- Nome do último usuário cadastrado
- Atualização em tempo real

### Validações no Frontend:

1. **Nome:**
   - Não pode ser vazio
   - Mínimo de 3 caracteres

2. **Data de Nascimento:**
   - Não pode ser vazia
   - Não pode ser data futura (máximo = hoje)

---

## 🚀 COMO EXECUTAR O PROJETO V1.3

### Pré-requisitos

✅ Java 21 instalado  
✅ Maven instalado  
✅ Podman instalado  
✅ MongoDB container rodando (veja seção "Instalação do MongoDB")

### Passo a Passo Completo

#### 1. Iniciar MongoDB (se não estiver rodando)

```powershell
# Verificar se está rodando
podman ps

# Se não estiver na lista, inicie:
podman start mongodb-steps

# Ou crie um novo container:
podman run -d --name mongodb-steps -p 27017:27017 -e MONGO_INITDB_DATABASE=passos -v mongodb-data:/data/db mongo:latest
```

#### 2. Compilar o projeto

Navegue até a pasta do backend:

```powershell
cd "C:\Users\NiltonBasilio\Downloads\projeto-steps-V1\backend"
```

Compile o projeto:

```powershell
mvn clean package
```

**Resultado esperado:**
```
[INFO] BUILD SUCCESS
[INFO] Total time:  XX.XXX s
```

#### 3. Executar o backend

```powershell
java -jar target/steps-backend-1.3.0.jar
```

**Ou use o Maven:**

```powershell
mvn spring-boot:run
```

**Logs esperados:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.1)

...
INFO  c.p.steps.StepsApplication - Started StepsApplication in X.XXX seconds
```

#### 4. Verificar se está funcionando

Abra o navegador e teste:

```
http://localhost:8080/api/v1.3/
```

Deve retornar informações da API em JSON.

#### 5. Acessar a Interface Web

```
http://localhost:8080/demo-v13.html
```

---

## 🧪 TESTANDO O SISTEMA COMPLETO

### Teste 1: Cadastrar Usuário pela Interface

1. Acesse `http://localhost:8080/demo-v13.html`
2. Preencha o formulário:
   - Nome: "João Silva"
   - Data de Nascimento: "15/05/1990"
3. Clique em "Cadastrar no MongoDB"
4. Deve aparecer mensagem de sucesso
5. O usuário deve aparecer na lista à direita

### Teste 2: Verificar no MongoDB

Abra o MongoDB Shell:

```powershell
podman exec -it mongodb-steps mongosh
```

Execute os comandos:

```javascript
use passos
db.dados_usuario.find().pretty()
```

**Resultado esperado:**
```json
{
  "_id": ObjectId("65a1b2c3d4e5f6789012345"),
  "nome": "João Silva",
  "dataNascimento": ISODate("1990-05-15T00:00:00.000Z"),
  "_class": "com.projeto.steps.model.Usuario"
}
```

### Teste 3: Testar API com PowerShell

```powershell
# Cadastrar usuário
$body = @{
    nome = "Maria Santos"
    dataNascimento = "1985-08-20"
} | ConvertTo-Json

$resultado = Invoke-RestMethod -Uri "http://localhost:8080/api/v1.3/usuarios" `
  -Method POST `
  -Body $body `
  -ContentType "application/json"

Write-Host "ID do usuário criado: $($resultado.usuario.id)"

# Listar todos os usuários
$usuarios = Invoke-RestMethod -Uri "http://localhost:8080/api/v1.3/usuarios"
$usuarios.usuarios | Format-Table nome, dataNascimento, id

# Buscar por nome
$resultado = Invoke-RestMethod -Uri "http://localhost:8080/api/v1.3/usuarios/buscar?nome=Silva"
$resultado.usuarios
```

### Teste 4: Validações

Tente cadastrar dados inválidos:

1. **Nome vazio:** Deve retornar erro "Nome é obrigatório"
2. **Data futura:** Deve retornar erro "Data de nascimento não pode ser no futuro"
3. **Campos vazios:** Deve retornar erro de validação

---

## 🔍 CONSULTAS ÚTEIS NO MONGODB

### Conectar ao MongoDB Shell

```powershell
podman exec -it mongodb-steps mongosh
```

### Comandos Básicos

```javascript
// Usar o banco "passos"
use passos

// Ver todas as coleções
show collections

// Contar documentos
db.dados_usuario.countDocuments()

// Listar todos os usuários (formatado)
db.dados_usuario.find().pretty()

// Buscar por nome específico
db.dados_usuario.find({ "nome": "João Silva" })

// Buscar por nome contendo texto (regex)
db.dados_usuario.find({ "nome": /Silva/i })

// Buscar usuários nascidos depois de uma data
db.dados_usuario.find({ 
  "dataNascimento": { $gt: new Date("1990-01-01") } 
})

// Ordenar por nome (alfabética)
db.dados_usuario.find().sort({ "nome": 1 })

// Limitar resultados (primeiros 5)
db.dados_usuario.find().limit(5)

// Contar usuários com nome específico
db.dados_usuario.countDocuments({ "nome": "João Silva" })

// Deletar usuário por ID
db.dados_usuario.deleteOne({ "_id": ObjectId("65a1b2c3...") })

// Deletar todos os usuários (CUIDADO!)
db.dados_usuario.deleteMany({})

// Criar índice no campo nome (melhor performance)
db.dados_usuario.createIndex({ "nome": 1 })

// Ver índices existentes
db.dados_usuario.getIndexes()

// Estatísticas da coleção
db.dados_usuario.stats()
```

### Consultas Avançadas

```javascript
// Buscar usuários nascidos entre 1980 e 1990
db.dados_usuario.find({
  "dataNascimento": {
    $gte: new Date("1980-01-01"),
    $lte: new Date("1990-12-31")
  }
})

// Buscar e projetar apenas alguns campos
db.dados_usuario.find(
  {},
  { "nome": 1, "dataNascimento": 1, "_id": 0 }
)

// Agregação: contar usuários por ano de nascimento
db.dados_usuario.aggregate([
  {
    $group: {
      _id: { $year: "$dataNascimento" },
      count: { $sum: 1 }
    }
  },
  { $sort: { "_id": 1 } }
])

// Atualizar um documento
db.dados_usuario.updateOne(
  { "_id": ObjectId("65a1b2c3...") },
  { $set: { "nome": "João Silva Santos" } }
)
```

---

## 📊 FLUXO COMPLETO DE DADOS

### Cadastro de Usuário - Passo a Passo

```
1. Frontend (demo-v13.html)
   ↓
   Usuário preenche formulário e clica em "Cadastrar"
   ↓
2. JavaScript
   ↓
   Cria objeto JSON: { nome, dataNascimento }
   ↓
   Envia POST para: http://localhost:8080/api/v1.3/usuarios
   ↓
3. Spring Boot Controller (WelcomeControllerV13)
   ↓
   Recebe requisição no método cadastrarUsuario()
   ↓
   Valida dados (nome não vazio, data válida)
   ↓
   Chama usuarioRepository.save(usuario)
   ↓
4. Spring Data MongoDB (UsuarioRepository)
   ↓
   Converte objeto Java em documento BSON
   ↓
   Conecta com MongoDB na porta 27017
   ↓
5. MongoDB
   ↓
   Acessa banco "passos"
   ↓
   Acessa coleção "dados_usuario"
   ↓
   Gera ID único: "65a1b2c3d4e5f6789012345"
   ↓
   Insere documento no banco
   ↓
   Retorna documento com ID gerado
   ↓
6. Spring Boot Controller
   ↓
   Recebe usuário salvo com ID
   ↓
   Cria resposta JSON com sucesso
   ↓
   Retorna HTTP 201 Created
   ↓
7. Frontend
   ↓
   Recebe resposta JSON
   ↓
   Exibe mensagem de sucesso
   ↓
   Atualiza lista de usuários
```

---

## 🛠️ TROUBLESHOOTING (RESOLUÇÃO DE PROBLEMAS)

### Problema 1: MongoDB não conecta

**Erro:**
```
MongoSocketOpenException: Exception opening socket
```

**Soluções:**

1. Verificar se MongoDB está rodando:
```powershell
podman ps
```

2. Se não estiver rodando, inicie:
```powershell
podman start mongodb-steps
```

3. Verificar logs do MongoDB:
```powershell
podman logs mongodb-steps
```

4. Testar conexão manual:
```powershell
podman exec -it mongodb-steps mongosh
```

### Problema 2: Porta 27017 ocupada

**Erro:**
```
Error: port 27017 is already in use
```

**Soluções:**

1. Ver o que está usando a porta:
```powershell
netstat -ano | findstr :27017
```

2. Parar processo ou usar outra porta:
```powershell
podman run -d --name mongodb-steps -p 27018:27017 ...
```

E atualizar `application.properties`:
```properties
spring.data.mongodb.uri=mongodb://localhost:27018/passos
```

### Problema 3: Dependência MongoDB não encontrada

**Erro:**
```
Cannot resolve symbol 'MongoRepository'
```

**Soluções:**

1. Verificar se a dependência está no `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

2. Recarregar dependências Maven:
```powershell
mvn clean install
```

3. No VS Code: `Ctrl+Shift+P` → "Java: Clean Java Language Server Workspace"

### Problema 4: Dados não persistem após reiniciar MongoDB

**Problema:** Ao reiniciar o container, os dados somem.

**Solução:** Certifique-se de usar volume no Podman:

```powershell
# ERRADO (sem volume):
podman run -d --name mongodb-steps -p 27017:27017 mongo:latest

# CORRETO (com volume):
podman run -d --name mongodb-steps -p 27017:27017 -v mongodb-data:/data/db mongo:latest
```

### Problema 5: CORS Error no Frontend

**Erro no console do navegador:**
```
Access to fetch blocked by CORS policy
```

**Solução:**

Verificar se o controller tem `@CrossOrigin`:
```java
@RestController
@RequestMapping("/api/v1.3")
@CrossOrigin(origins = "*")  // ← Esta anotação é necessária
public class WelcomeControllerV13 {
```

### Problema 6: Backend não inicia

**Erro:**
```
Port 8080 is already in use
```

**Soluções:**

1. Parar processo na porta 8080:
```powershell
# Encontrar processo
netstat -ano | findstr :8080

# Matar processo (substituir PID pelo número encontrado)
taskkill /PID 1234 /F
```

2. Ou mudar a porta no `application.properties`:
```properties
server.port=8081
```

---

## 📈 MONITORAMENTO E LOGS

### Ver Logs do Backend Spring Boot

O Spring Boot exibe logs no console onde foi executado. Para salvar em arquivo:

```powershell
mvn spring-boot:run > logs.txt 2>&1
```

### Ver Logs do MongoDB

```powershell
# Logs completos
podman logs mongodb-steps

# Logs em tempo real
podman logs -f mongodb-steps

# Últimas 100 linhas
podman logs --tail 100 mongodb-steps
```

### Monitorar Performance

```powershell
# Uso de CPU e memória do MongoDB
podman stats mongodb-steps

# Informações detalhadas
podman inspect mongodb-steps
```

### Endpoints de Monitoramento (Actuator)

```
http://localhost:8080/actuator/health
http://localhost:8080/actuator/metrics
http://localhost:8080/actuator/info
```

---

## 🔐 SEGURANÇA (PARA PRODUÇÃO)

⚠️ **IMPORTANTE:** As configurações atuais são para DESENVOLVIMENTO. Para produção:

### 1. Adicionar autenticação ao MongoDB

```powershell
podman run -d `
  --name mongodb-steps `
  -p 27017:27017 `
  -e MONGO_INITDB_ROOT_USERNAME=admin `
  -e MONGO_INITDB_ROOT_PASSWORD=senhaSegura123 `
  -e MONGO_INITDB_DATABASE=passos `
  -v mongodb-data:/data/db `
  mongo:latest
```

Atualizar `application.properties`:
```properties
spring.data.mongodb.uri=mongodb://admin:senhaSegura123@localhost:27017/passos?authSource=admin
```

### 2. Remover CORS aberto

Trocar:
```java
@CrossOrigin(origins = "*")
```

Por:
```java
@CrossOrigin(origins = "https://seudominio.com")
```

### 3. Adicionar validações no backend

Usar Bean Validation:
```java
@NotBlank(message = "Nome é obrigatório")
@Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
private String nome;
```

### 4. HTTPS em produção

Configure SSL/TLS no Spring Boot.

---

## 📚 CONCEITOS APRENDIDOS NA V1.3

### 1. NoSQL e MongoDB
- Diferença entre SQL e NoSQL
- Estrutura de bancos NoSQL (coleções e documentos)
- Quando usar MongoDB vs bancos SQL

### 2. Containers com Podman
- O que são containers
- Como executar MongoDB em container
- Persistência de dados com volumes

### 3. Spring Data MongoDB
- Integração Spring Boot com MongoDB
- Repositories automáticos
- Queries por convenção de nomes

### 4. API REST Completa (CRUD)
- CREATE: POST para cadastrar
- READ: GET para listar e buscar
- UPDATE: (pode ser adicionado depois)
- DELETE: DELETE para remover

### 5. Validações
- Validações no frontend (HTML5)
- Validações no backend (Java)
- Respostas HTTP apropriadas (201, 400, 404, 500)

### 6. Frontend Dinâmico
- Comunicação assíncrona (fetch API)
- Atualização em tempo real
- Tratamento de erros

---

## 🎓 PRÓXIMOS PASSOS E MELHORIAS

### Funcionalidades que podem ser adicionadas:

1. **Edição de Usuários**
   - Endpoint PUT `/api/v1.3/usuarios/{id}`
   - Formulário de edição no frontend

2. **Busca Avançada**
   - Busca por faixa de idade
   - Busca por inicial do nome
   - Ordenação personalizável

3. **Paginação**
   - Listar usuários em páginas (10 por vez)
   - Navegação entre páginas

4. **Validações Avançadas**
   - CPF, email, telefone
   - Verificar duplicatas
   - Idade mínima/máxima

5. **Dashboard**
   - Gráficos de estatísticas
   - Distribuição de idades
   - Total de cadastros por mês

6. **Autenticação**
   - Login de usuários
   - Permissões diferentes

7. **Testes Automatizados**
   - Testes unitários (JUnit)
   - Testes de integração
   - Testes de API (RestAssured)

---

## 📞 SUPORTE E RECURSOS

### Documentação Oficial:

- **MongoDB:** https://www.mongodb.com/docs/
- **Spring Data MongoDB:** https://spring.io/projects/spring-data-mongodb
- **Podman:** https://podman.io/docs
- **Spring Boot:** https://spring.io/projects/spring-boot

### Comandos Rápidos de Referência:

```powershell
# MONGODB
podman start mongodb-steps          # Iniciar
podman stop mongodb-steps           # Parar
podman logs mongodb-steps           # Ver logs
podman exec -it mongodb-steps mongosh  # Abrir shell

# SPRING BOOT
mvn clean package                   # Compilar
mvn spring-boot:run                 # Executar
java -jar target/steps-backend-1.3.0.jar  # Executar JAR

# TESTES
curl http://localhost:8080/api/v1.3/   # Testar API
```

---

## ✅ CHECKLIST DE VERIFICAÇÃO

Antes de considerar a V1.3 funcionando, verifique:

- [ ] Podman instalado e funcionando
- [ ] Container MongoDB rodando (`podman ps`)
- [ ] Conexão com MongoDB funciona (`mongosh`)
- [ ] Backend compila sem erros (`mvn clean package`)
- [ ] Backend inicia corretamente (`mvn spring-boot:run`)
- [ ] Endpoint API responde (`http://localhost:8080/api/v1.3/`)
- [ ] Interface web carrega (`http://localhost:8080/demo-v13.html`)
- [ ] Cadastro de usuário funciona (formulário)
- [ ] Lista de usuários atualiza automaticamente
- [ ] Dados persistem no MongoDB (verificar com `mongosh`)
- [ ] Deletar usuário funciona
- [ ] Validações funcionam (nome vazio, data futura)

---

## 🎉 CONCLUSÃO

A **Versão 1.3** do Projeto Steps adiciona persistência real de dados com MongoDB, transformando a aplicação em um sistema completo de cadastro. 

Você aprendeu:
- ✅ Como funciona um banco de dados NoSQL
- ✅ Como executar MongoDB em containers
- ✅ Como integrar Spring Boot com MongoDB
- ✅ Como criar uma API REST completa
- ✅ Como construir um frontend que consome APIs
- ✅ Como validar e tratar dados

**Este é um grande passo no desenvolvimento de aplicações modernas!** 🚀

---

**Documentação criada em:** 16 de dezembro de 2024  
**Versão:** 1.3.0  
**Autor:** Projeto Steps V1
