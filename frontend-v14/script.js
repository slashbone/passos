/**
 * 🚀 STEPS V1.4 - SISTEMA CRUD COM SQL SERVER
 * 
 * JavaScript principal para interface frontend separada
 * Conecta com API Spring Boot rodando no Podman
 * 
 * @version 1.4.0
 * @author Projeto Steps
 */

// ===============================================
// 🔧 CONFIGURAÇÕES E CONSTANTES
// ===============================================

const API_BASE_URL = 'http://localhost:8080/api/v1.4';
const ENDPOINTS = {
    usuarios: `${API_BASE_URL}/usuarios`,
    estatisticas: `${API_BASE_URL}/estatisticas`,
    info: `${API_BASE_URL}/info`
};

// Estados da aplicação
let currentEditingUser = null;
let isLoading = false;

// ===============================================
// 🎯 INICIALIZAÇÃO DA APLICAÇÃO
// ===============================================

document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 Steps V1.4 - Inicializando aplicação...');
    
    // Inicializar componentes
    initializeEventListeners();
    checkApiConnection();
    loadStatistics();
    loadAllUsers();
    
    // Configurar validações de formulário
    setupFormValidation();
    
    console.log('✅ Aplicação inicializada com sucesso!');
});

// ===============================================
// 🎮 EVENT LISTENERS
// ===============================================

function initializeEventListeners() {
    // Formulário principal de cadastro
    const form = document.getElementById('usuarioForm');
    form.addEventListener('submit', handleCreateUser);
    
    // Botão de limpar campos
    document.getElementById('limparBtn').addEventListener('click', clearForm);
    
    // Busca de usuários
    document.getElementById('searchBtn').addEventListener('click', handleSearch);
    document.getElementById('searchInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') handleSearch();
    });
    
    // Botões de listagem
    document.getElementById('listarTodosBtn').addEventListener('click', loadAllUsers);
    document.getElementById('aniversariantesBtn').addEventListener('click', loadBirthdays);
    
    // Modal de edição
    document.getElementById('closeModal').addEventListener('click', closeEditModal);
    document.getElementById('cancelEdit').addEventListener('click', closeEditModal);
    document.getElementById('editForm').addEventListener('submit', handleUpdateUser);
    
    // Outros controles
    document.getElementById('refreshStats').addEventListener('click', loadStatistics);
    
    // Fechar modal clicando fora
    document.getElementById('editModal').addEventListener('click', function(e) {
        if (e.target === this) closeEditModal();
    });
}

// ===============================================
// 🌐 CONEXÃO COM API
// ===============================================

async function checkApiConnection() {
    const statusElement = document.getElementById('connectionStatus');
    
    try {
        showLoading('Verificando conexão com API...');
        
        const response = await fetch(`${API_BASE_URL}/info`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        if (response.ok) {
            const info = await response.json();
            statusElement.className = 'connection-status connected';
            statusElement.innerHTML = '<i class="fas fa-circle"></i><span>Conectado - ' + info.nome + '</span>';
            showNotification('Conexão estabelecida com sucesso!', 'success');
        } else {
            throw new Error('API não está respondendo corretamente');
        }
    } catch (error) {
        console.error('❌ Erro de conexão:', error);
        statusElement.className = 'connection-status disconnected';
        statusElement.innerHTML = '<i class="fas fa-circle"></i><span>Desconectado</span>';
        showNotification('Erro de conexão com a API. Verifique se o servidor está rodando.', 'error');
    } finally {
        hideLoading();
    }
}

// ===============================================
// 📊 ESTATÍSTICAS
// ===============================================

async function loadStatistics() {
    try {
        showLoading('Carregando estatísticas...');
        
        const response = await fetch(ENDPOINTS.estatisticas);
        if (!response.ok) throw new Error('Erro ao carregar estatísticas');
        
        const stats = await response.json();
        displayStatistics(stats);
        
    } catch (error) {
        console.error('❌ Erro ao carregar estatísticas:', error);
        showNotification('Erro ao carregar estatísticas', 'error');
    } finally {
        hideLoading();
    }
}

function displayStatistics(stats) {
    const statsGrid = document.getElementById('statsGrid');
    
    const statsHTML = `
        <div class="stat-card">
            <div class="stat-icon primary">
                <i class="fas fa-users"></i>
            </div>
            <div class="stat-value">${stats.total_usuarios || 0}</div>
            <div class="stat-label">Total de Usuários</div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon success">
                <i class="fas fa-user-plus"></i>
            </div>
            <div class="stat-value">${stats.usuarios_cadastrados_hoje || 0}</div>
            <div class="stat-label">Cadastros Hoje</div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon warning">
                <i class="fas fa-birthday-cake"></i>
            </div>
            <div class="stat-value">${stats.aniversariantes_hoje || 0}</div>
            <div class="stat-label">Aniversariantes</div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon info">
                <i class="fas fa-chart-line"></i>
            </div>
            <div class="stat-value">${stats.idade_media ? Math.round(stats.idade_media) : 0}</div>
            <div class="stat-label">Idade Média</div>
        </div>
    `;
    
    statsGrid.innerHTML = statsHTML;
}

// ===============================================
// 👥 GERENCIAMENTO DE USUÁRIOS
// ===============================================

async function loadAllUsers() {
    try {
        showLoading('Carregando usuários...');
        
        const response = await fetch(ENDPOINTS.usuarios);
        if (!response.ok) throw new Error('Erro ao carregar usuários');
        
        const data = await response.json();
        displayUsers(data.usuarios, `Todos os usuários (${data.total})`);
        
    } catch (error) {
        console.error('❌ Erro ao carregar usuários:', error);
        showNotification('Erro ao carregar lista de usuários', 'error');
    } finally {
        hideLoading();
    }
}

async function handleSearch() {
    const searchTerm = document.getElementById('searchInput').value.trim();
    
    if (!searchTerm) {
        showNotification('Digite um nome para buscar', 'warning');
        return;
    }
    
    try {
        showLoading('Buscando usuários...');
        
        const response = await fetch(`${ENDPOINTS.usuarios}/nome/${encodeURIComponent(searchTerm)}`);
        if (!response.ok) throw new Error('Erro na busca');
        
        const data = await response.json();
        displayUsers(data.usuarios, `Resultados para "${searchTerm}" (${data.total})`);
        
    } catch (error) {
        console.error('❌ Erro na busca:', error);
        showNotification('Erro ao realizar busca', 'error');
    } finally {
        hideLoading();
    }
}

async function loadBirthdays() {
    try {
        showLoading('Carregando aniversariantes...');
        
        const response = await fetch(`${ENDPOINTS.usuarios}/aniversario`);
        if (!response.ok) throw new Error('Erro ao carregar aniversariantes');
        
        const data = await response.json();
        displayUsers(data.aniversariantes, `Aniversariantes de hoje (${data.total})`);
        
        if (data.total > 0) {
            showNotification(`🎉 ${data.total} aniversariante(s) hoje!`, 'success');
        } else {
            showNotification('Nenhum aniversariante hoje', 'info');
        }
        
    } catch (error) {
        console.error('❌ Erro ao carregar aniversariantes:', error);
        showNotification('Erro ao carregar aniversariantes', 'error');
    } finally {
        hideLoading();
    }
}

function displayUsers(usuarios, title) {
    const usersList = document.getElementById('usuariosList');
    const resultsCount = document.getElementById('resultsCount');
    
    resultsCount.textContent = title;
    
    if (!usuarios || usuarios.length === 0) {
        usersList.innerHTML = `
            <div style="text-align: center; padding: 3rem; color: var(--text-muted);">
                <i class="fas fa-users" style="font-size: 3rem; margin-bottom: 1rem;"></i>
                <p>Nenhum usuário encontrado</p>
            </div>
        `;
        return;
    }
    
    const usersHTML = usuarios.map(usuario => `
        <div class="usuario-item">
            <div class="usuario-info">
                <h3>${escapeHtml(usuario.nome)}</h3>
                <p><i class="fas fa-calendar-alt"></i> ${formatDate(usuario.dataNascimento)}</p>
                <div class="usuario-meta">
                    <span><i class="fas fa-clock"></i> Criado: ${formatDateTime(usuario.createdAt)}</span>
                    ${usuario.updatedAt !== usuario.createdAt ? 
                        `<span><i class="fas fa-edit"></i> Atualizado: ${formatDateTime(usuario.updatedAt)}</span>` 
                        : ''}
                </div>
            </div>
            <div class="usuario-actions">
                <button class="btn btn-info btn-sm" onclick="editUser(${usuario.id})">
                    <i class="fas fa-edit"></i>
                    Editar
                </button>
                <button class="btn btn-danger btn-sm" onclick="deleteUser(${usuario.id}, '${escapeHtml(usuario.nome)}')">
                    <i class="fas fa-trash"></i>
                    Excluir
                </button>
            </div>
        </div>
    `).join('');
    
    usersList.innerHTML = usersHTML;
}

// ===============================================
// ✏️ OPERAÇÕES CRUD
// ===============================================

async function handleCreateUser(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const userData = {
        nome: formData.get('nome').trim(),
        dataNascimento: formData.get('dataNascimento')
    };
    
    if (!validateUserData(userData)) return;
    
    try {
        showLoading('Cadastrando usuário...');
        
        const response = await fetch(ENDPOINTS.usuarios, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });
        
        const result = await response.json();
        
        if (response.ok) {
            showNotification(`Usuário "${userData.nome}" cadastrado com sucesso!`, 'success');
            clearForm();
            loadAllUsers();
            loadStatistics();
        } else {
            if (result.status === 'duplicate') {
                showNotification('Já existe um usuário com este nome', 'warning');
            } else {
                throw new Error(result.message || 'Erro desconhecido');
            }
        }
        
    } catch (error) {
        console.error('❌ Erro ao criar usuário:', error);
        showNotification('Erro ao cadastrar usuário: ' + error.message, 'error');
    } finally {
        hideLoading();
    }
}

async function editUser(userId) {
    try {
        showLoading('Carregando dados do usuário...');
        
        const response = await fetch(`${ENDPOINTS.usuarios}/${userId}`);
        if (!response.ok) throw new Error('Usuário não encontrado');
        
        const data = await response.json();
        const usuario = data.usuario;
        
        // Preencher modal de edição
        document.getElementById('editId').value = usuario.id;
        document.getElementById('editNome').value = usuario.nome;
        document.getElementById('editDataNascimento').value = usuario.dataNascimento;
        
        currentEditingUser = usuario;
        showEditModal();
        
    } catch (error) {
        console.error('❌ Erro ao carregar usuário:', error);
        showNotification('Erro ao carregar dados do usuário', 'error');
    } finally {
        hideLoading();
    }
}

async function handleUpdateUser(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const userData = {
        nome: formData.get('nome') ? formData.get('nome').trim() : document.getElementById('editNome').value.trim(),
        dataNascimento: formData.get('dataNascimento') || document.getElementById('editDataNascimento').value
    };
    
    const userId = document.getElementById('editId').value;
    
    if (!validateUserData(userData)) return;
    
    try {
        showLoading('Atualizando usuário...');
        
        const response = await fetch(`${ENDPOINTS.usuarios}/${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });
        
        const result = await response.json();
        
        if (response.ok) {
            showNotification(`Usuário "${userData.nome}" atualizado com sucesso!`, 'success');
            closeEditModal();
            loadAllUsers();
            loadStatistics();
        } else {
            throw new Error(result.message || 'Erro desconhecido');
        }
        
    } catch (error) {
        console.error('❌ Erro ao atualizar usuário:', error);
        showNotification('Erro ao atualizar usuário: ' + error.message, 'error');
    } finally {
        hideLoading();
    }
}

async function deleteUser(userId, userName) {
    if (!confirm(`Tem certeza que deseja excluir o usuário "${userName}"?\n\nEsta ação não pode ser desfeita.`)) {
        return;
    }
    
    try {
        showLoading('Excluindo usuário...');
        
        const response = await fetch(`${ENDPOINTS.usuarios}/${userId}`, {
            method: 'DELETE'
        });
        
        const result = await response.json();
        
        if (response.ok) {
            showNotification(`Usuário "${userName}" excluído com sucesso!`, 'success');
            loadAllUsers();
            loadStatistics();
        } else {
            throw new Error(result.message || 'Erro desconhecido');
        }
        
    } catch (error) {
        console.error('❌ Erro ao excluir usuário:', error);
        showNotification('Erro ao excluir usuário: ' + error.message, 'error');
    } finally {
        hideLoading();
    }
}

// ===============================================
// 🔧 FUNÇÕES UTILITÁRIAS
// ===============================================

function validateUserData(userData) {
    // Limpar mensagens de erro anteriores
    document.getElementById('nomeError').textContent = '';
    document.getElementById('dataError').textContent = '';
    
    let isValid = true;
    
    // Validar nome
    if (!userData.nome || userData.nome.length < 2) {
        document.getElementById('nomeError').textContent = 'Nome deve ter pelo menos 2 caracteres';
        isValid = false;
    } else if (userData.nome.length > 255) {
        document.getElementById('nomeError').textContent = 'Nome deve ter no máximo 255 caracteres';
        isValid = false;
    }
    
    // Validar data de nascimento
    if (!userData.dataNascimento) {
        document.getElementById('dataError').textContent = 'Data de nascimento é obrigatória';
        isValid = false;
    } else {
        const birthDate = new Date(userData.dataNascimento);
        const today = new Date();
        const minDate = new Date(1900, 0, 1);
        
        if (birthDate >= today) {
            document.getElementById('dataError').textContent = 'Data de nascimento deve ser no passado';
            isValid = false;
        } else if (birthDate < minDate) {
            document.getElementById('dataError').textContent = 'Data de nascimento deve ser posterior a 1900';
            isValid = false;
        }
    }
    
    return isValid;
}

function setupFormValidation() {
    // Configurar validação em tempo real
    const nomeInput = document.getElementById('nome');
    const dataInput = document.getElementById('dataNascimento');
    
    nomeInput.addEventListener('blur', function() {
        validateUserData({
            nome: this.value.trim(),
            dataNascimento: dataInput.value
        });
    });
    
    dataInput.addEventListener('change', function() {
        validateUserData({
            nome: nomeInput.value.trim(),
            dataNascimento: this.value
        });
    });
}

function clearForm() {
    document.getElementById('usuarioForm').reset();
    document.getElementById('nomeError').textContent = '';
    document.getElementById('dataError').textContent = '';
    document.getElementById('nome').focus();
}

function showEditModal() {
    const modal = document.getElementById('editModal');
    modal.classList.add('show');
    document.body.style.overflow = 'hidden';
    document.getElementById('editNome').focus();
}

function closeEditModal() {
    const modal = document.getElementById('editModal');
    modal.classList.remove('show');
    document.body.style.overflow = 'auto';
    currentEditingUser = null;
}

function showLoading(message = 'Carregando...') {
    if (isLoading) return;
    
    isLoading = true;
    const overlay = document.getElementById('loadingOverlay');
    const loadingText = overlay.querySelector('p');
    loadingText.textContent = message;
    overlay.classList.add('show');
}

function hideLoading() {
    if (!isLoading) return;
    
    isLoading = false;
    const overlay = document.getElementById('loadingOverlay');
    overlay.classList.remove('show');
}

function showNotification(message, type = 'info') {
    const container = document.getElementById('notifications');
    
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.innerHTML = `
        <div style="display: flex; align-items: center; gap: 0.5rem;">
            <i class="fas fa-${getIconForType(type)}"></i>
            <span>${message}</span>
        </div>
    `;
    
    container.appendChild(notification);
    
    // Auto remover após 5 segundos
    setTimeout(() => {
        if (notification.parentNode) {
            notification.style.animation = 'notificationSlideOut 0.3s ease-in-out';
            setTimeout(() => {
                container.removeChild(notification);
            }, 300);
        }
    }, 5000);
}

function getIconForType(type) {
    const icons = {
        success: 'check-circle',
        error: 'exclamation-circle',
        warning: 'exclamation-triangle',
        info: 'info-circle'
    };
    return icons[type] || 'info-circle';
}

function formatDate(dateString) {
    if (!dateString) return 'Data inválida';
    
    try {
        const date = new Date(dateString + 'T00:00:00');
        return date.toLocaleDateString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    } catch (error) {
        return 'Data inválida';
    }
}

function formatDateTime(dateTimeString) {
    if (!dateTimeString) return 'Data inválida';
    
    try {
        const date = new Date(dateTimeString);
        return date.toLocaleString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    } catch (error) {
        return 'Data inválida';
    }
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// ===============================================
// 🔄 ATUALIZAÇÃO AUTOMÁTICA
// ===============================================

// Atualizar estatísticas a cada 30 segundos
setInterval(() => {
    if (!isLoading) {
        loadStatistics();
    }
}, 30000);

// ===============================================
// 🎯 ATALHOS DE TECLADO
// ===============================================

document.addEventListener('keydown', function(e) {
    // ESC para fechar modal
    if (e.key === 'Escape') {
        closeEditModal();
    }
    
    // Ctrl+N para novo usuário
    if (e.ctrlKey && e.key === 'n') {
        e.preventDefault();
        document.getElementById('nome').focus();
    }
    
    // F5 para atualizar dados
    if (e.key === 'F5') {
        e.preventDefault();
        loadAllUsers();
        loadStatistics();
    }
});

console.log('📜 Steps V1.4 JavaScript carregado com sucesso!');