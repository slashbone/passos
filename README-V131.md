# 🚀 Projeto Steps V1.3.1 - CRUD Completo MongoDB

![Version](https://img.shields.io/badge/version-1.3.1-blue)
![Java](https://img.shields.io/badge/java-21-orange)
![Spring Boot](https://img.shields.io/badge/spring--boot-3.2.1-green)
![MongoDB](https://img.shields.io/badge/mongodb-6.0-darkgreen)

## ✨ **Nova Versão V1.3.1 - CRUD Completo!**

### 🎯 **O que há de novo:**

- **✅ CRUD COMPLETO**: Criar, Consultar, Editar e Excluir usuários
- **🎨 Interface Moderna**: Design completamente renovado com navegação por tabs
- **📊 Dashboard**: Estatísticas em tempo real e monitoramento do sistema
- **🔍 Busca Avançada**: Consulta por ID, nome ou listagem completa
- **✏️ Edição Modal**: Interface intuitiva para atualização de dados
- **📱 Design Responsivo**: Funciona perfeitamente em todos os dispositivos
- **🛡️ Validações**: Sistema robusto de validação frontend e backend

---

## 🌐 **Acesso Rápido**

| **Funcionalidade** | **URL** | **Descrição** |
|-------------------|---------|---------------|
| **🎮 Interface V1.3.1** | [http://localhost:8080/demo-v131.html](http://localhost:8080/demo-v131.html) | **Interface completa com CRUD** |
| **📊 API V1.3.1** | [http://localhost:8080/api/v1.31/](http://localhost:8080/api/v1.31/) | **API REST com todos os endpoints** |
| **🏥 Health Check** | [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health) | Status do sistema |

---

## 🚀 **Como Iniciar**

### **1. Inicialização Rápida**
```bash
# Navegar para o projeto
cd projeto-steps-V1

# Executar script de build e deploy
build-e-executar.bat
```

### **2. Manual (alternativo)**
```bash
# 1. Iniciar MongoDB
deploy-mongodb.bat

# 2. Navegar para backend
cd backend

# 3. Executar Spring Boot
mvn spring-boot:run
```

---

## 🎯 **Funcionalidades V1.3.1**

### **📊 Dashboard**
- **Estatísticas em tempo real**: Total de usuários, último cadastro
- **Status do sistema**: Conexão MongoDB, status da API
- **Informações da API**: Endpoints disponíveis e versão

### **📝 Gerenciamento de Usuários**

#### **Cadastrar**
- Formulário intuitivo com validação
- Campos: Nome completo e data de nascimento
- Validações automáticas

#### **Consultar** 
- **Por ID**: Busca exata
- **Por Nome**: Busca parcial (case-insensitive)  
- **Listar Todos**: Visualização completa

#### **Editar**
- Modal overlay elegante
- Formulário pré-preenchido
- Validação em tempo real
- Atualização automática das listas

#### **Excluir**
- Confirmação de segurança
- Feedback visual
- Atualização automática

---

## 📋 **API REST V1.3.1**

### **Base URL:** `http://localhost:8080/api/v1.31`

| **Método** | **Endpoint** | **Descrição** |
|------------|--------------|---------------|
| `GET` | `/` | Informações da API |
| `GET` | `/estatisticas` | Estatísticas do sistema |
| `POST` | `/usuarios` | Criar usuário |
| `GET` | `/usuarios` | Listar todos |
| `GET` | `/usuarios/{id}` | Buscar por ID |
| `GET` | `/usuarios/buscar?nome={nome}` | Buscar por nome |
| `PUT` | `/usuarios/{id}` | **✨ NOVO: Atualizar usuário** |
| `DELETE` | `/usuarios/{id}` | **✨ NOVO: Excluir usuário** |

### **Exemplo de Uso:**

```bash
# Criar usuário
curl -X POST http://localhost:8080/api/v1.31/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome": "João Silva", "dataNascimento": "1990-05-15"}'

# Atualizar usuário (NOVO!)
curl -X PUT http://localhost:8080/api/v1.31/usuarios/{id} \
  -H "Content-Type: application/json" \
  -d '{"nome": "João Santos", "dataNascimento": "1990-05-15"}'

# Excluir usuário (NOVO!)
curl -X DELETE http://localhost:8080/api/v1.31/usuarios/{id}
```

---

## 🎨 **Screenshots das Funcionalidades**

### **📊 Dashboard V1.3.1**
- Interface moderna com estatísticas
- Status em tempo real
- Navegação por tabs intuitiva

### **✏️ Modal de Edição**
- Interface elegante para updates
- Formulário pré-preenchido  
- Validação instantânea

### **🔍 Sistema de Busca**
- Múltiplas opções de consulta
- Resultados dinâmicos
- Interface responsiva

---

## 🛠️ **Tecnologias**

- **Backend**: Spring Boot 3.2.1, Java 21
- **Banco**: MongoDB 6.0 (via Podman/Docker)
- **Frontend**: HTML5, CSS3, JavaScript ES6
- **Build**: Maven
- **Containerização**: Podman (compatível com Docker)

---

## 📈 **Melhorias da V1.3.0 → V1.3.1**

| **Aspecto** | **V1.3.0** | **V1.3.1** |
|-------------|------------|-------------|
| **CRUD** | Apenas Create + Read | **✅ CRUD Completo** |
| **Interface** | Página simples | **🎨 Interface com tabs** |
| **Dashboard** | ❌ Não tinha | **📊 Dashboard completo** |
| **Busca** | Básica | **🔍 Busca avançada** |
| **Edição** | ❌ Não tinha | **✏️ Modal de edição** |
| **API** | Endpoints básicos | **🛡️ API robusta com validações** |

---

## ✅ **Teste Rápido**

1. **Acesse:** [http://localhost:8080/demo-v131.html](http://localhost:8080/demo-v131.html)
2. **Dashboard:** Veja as estatísticas em tempo real
3. **Cadastre** um usuário na aba "Cadastrar"
4. **Liste** os usuários na aba "Listar" 
5. **Edite** clicando no botão "✏️ Editar"
6. **Consulte** na aba "Consultar" por nome ou ID
7. **Exclua** um usuário se necessário

---

## 📞 **Suporte**

- **Documentação Completa**: [VERSAO-V131-DOCUMENTACAO.md](VERSAO-V131-DOCUMENTACAO.md)
- **Versões Anteriores**: [VERSAO-V13-DOCUMENTACAO.md](VERSAO-V13-DOCUMENTACAO.md)

---

**🎉 Projeto Steps V1.3.1 - Sistema Completo de Gerenciamento de Usuários!**

*Desenvolvido com Spring Boot 3.2.1 + MongoDB + Interface Moderna*