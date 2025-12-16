/*
   JAVASCRIPT PARA COMUNICAÇÃO COM API SPRING BOOT
   
   EXPLICAÇÃO DETALHADA PARA INICIANTES:
   
   JavaScript é a linguagem que torna nossa página web interativa.
   Este arquivo contém toda a lógica para:
   1. Comunicar com o backend Spring Boot via HTTP
   2. Manipular elementos da página (DOM)
   3. Responder a cliques e eventos do usuário
   4. Verificar status da API
   
   CONCEITOS IMPORTANTES:
   
   - API REST: Interface que permite comunicação entre frontend e backend
   - HTTP: Protocolo usado para comunicação web (GET, POST, etc.)
   - JSON: Formato de dados usado para trocar informações
   - DOM: Estrutura da página HTML que podemos modificar
   - Async/Await: Forma moderna de lidar com operações assíncronas
   
   FLUXO DA APLICAÇÃO:
   1. Página carrega → Verifica se API está funcionando
   2. Usuário clica botão → Faz requisição POST para API
   3. API responde → JavaScript exibe mensagem na tela
*/

// ========================================
// CONFIGURAÇÕES E CONSTANTES
// ========================================

/*
   URL BASE DA API
   Define onde está rodando nosso backend Spring Boot
*/
const API_BASE_URL = 'http://localhost:8080';

/*
   ENDPOINTS DA API
   Define todas as URLs que nossa aplicação vai acessar
*/
const API_ENDPOINTS = {
    home: `${API_BASE_URL}/`,                    // Página inicial
    obrigado: `${API_BASE_URL}/api/obrigado`,    // Endpoint do botão
    health: `${API_BASE_URL}/api/health`,        // Verificação de saúde
    actuatorHealth: `${API_BASE_URL}/actuator/health` // Health check do Actuator
};

/*
   ELEMENTOS HTML
   Referências para elementos que vamos manipular
*/
let elements = {};

// ========================================
// INICIALIZAÇÃO DA APLICAÇÃO
// ========================================

/*
   EVENTO DE CARREGAMENTO DA PÁGINA
   Executa quando toda a página HTML foi carregada
   
   DOMContentLoaded: Garante que HTML está pronto antes de executar JavaScript
   Sem isso, tentaríamos acessar elementos que ainda não foram criados
*/
document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 Iniciando aplicação frontend...');
    
    // Inicializa referências dos elementos HTML
    initializeElements();
    
    // Configura eventos de clique
    setupEventListeners();
    
    // Verifica se API está funcionando
    checkApiStatus();
    
    // Configura verificação periódica da API (a cada 30 segundos)
    setInterval(checkApiStatus, 30000);
    
    console.log('✅ Frontend inicializado com sucesso!');
});

// ========================================
// INICIALIZAÇÃO DE ELEMENTOS
// ========================================

/*
   FUNÇÃO PARA CAPTURAR ELEMENTOS HTML
   
   document.getElementById(): Busca elementos pelo atributo "id"
   Guardamos referências para usar posteriormente sem buscar novamente
*/
function initializeElements() {
    elements = {
        thankButton: document.getElementById('thankButton'),
        thankMessage: document.getElementById('thankMessage'),
        apiStatus: document.getElementById('apiStatus'),
        statusText: document.getElementById('statusText'),
        apiUrl: document.getElementById('apiUrl')
    };
    
    // Verifica se todos os elementos foram encontrados
    const missingElements = Object.entries(elements)
        .filter(([name, element]) => !element)
        .map(([name]) => name);
        
    if (missingElements.length > 0) {
        console.error('❌ Elementos HTML não encontrados:', missingElements);
    } else {
        console.log('✅ Todos os elementos HTML encontrados');
    }
}

// ========================================
// CONFIGURAÇÃO DE EVENTOS
// ========================================

/*
   FUNÇÃO PARA CONFIGURAR EVENTOS DE CLIQUE
   
   addEventListener(): Adiciona uma função que será executada quando evento ocorrer
   'click': Tipo de evento (clique do mouse)
   handleButtonClick: Função que será chamada quando botão for clicado
*/
function setupEventListeners() {
    if (elements.thankButton) {
        // Evento de clique do botão principal
        elements.thankButton.addEventListener('click', handleButtonClick);
        
        // Suporte para teclado (Enter e Espaço)
        elements.thankButton.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault(); // Previne comportamento padrão
                handleButtonClick();
            }
        });
        
        console.log('✅ Event listeners configurados');
    } else {
        console.error('❌ Botão não encontrado para configurar eventos');
    }
}

// ========================================
// VERIFICAÇÃO DE STATUS DA API
// ========================================

/*
   FUNÇÃO PARA VERIFICAR SE API ESTÁ FUNCIONANDO
   
   async: Palavra-chave que indica função assíncrona
   await: Espera a operação completar antes de continuar
   
   Esta função testa a conexão com o backend e atualiza o status na tela
*/
async function checkApiStatus() {
    console.log('🔍 Verificando status da API...');
    
    updateApiStatus('loading', 'Verificando conexão...');
    
    try {
        /*
           fetch(): Função JavaScript para fazer requisições HTTP
           É o substituto moderno do XMLHttpRequest
           
           Configurações da requisição:
           - method: Tipo de requisição (GET, POST, etc.)
           - headers: Informações adicionais da requisição
           - timeout: Tempo limite para resposta
        */
        const response = await fetch(API_ENDPOINTS.home, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            // Timeout de 5 segundos
            signal: AbortSignal.timeout(5000)
        });
        
        /*
           Verifica se resposta foi bem-sucedida
           response.ok: true se status HTTP está entre 200-299
        */
        if (response.ok) {
            const data = await response.json(); // Converte resposta para objeto JavaScript
            console.log('✅ API online:', data);
            updateApiStatus('online', `Conectado - ${data.message || 'API funcionando'}`);
        } else {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        
    } catch (error) {
        /*
           Tratamento de erros de conexão
           Pode ser: rede indisponível, servidor offline, timeout, etc.
        */
        console.error('❌ Erro ao verificar API:', error);
        updateApiStatus('offline', `Offline - ${error.message}`);
    }
}

/*
   FUNÇÃO PARA ATUALIZAR VISUAL DO STATUS DA API
   
   Atualiza o indicador visual que mostra se API está funcionando
*/
function updateApiStatus(status, message) {
    if (!elements.apiStatus || !elements.statusText) return;
    
    // Remove classes antigas de status
    elements.apiStatus.classList.remove('online', 'offline', 'loading');
    
    // Adiciona nova classe de status
    elements.apiStatus.classList.add(status);
    
    // Atualiza texto do status
    elements.statusText.textContent = message;
    
    // Adiciona emoji baseado no status
    const statusEmojis = {
        online: '🟢',
        offline: '🔴',
        loading: '🟡'
    };
    
    elements.statusText.textContent = `${statusEmojis[status]} ${message}`;
    
    console.log(`📊 Status da API atualizado: ${status} - ${message}`);
}

// ========================================
// LÓGICA DO BOTÃO PRINCIPAL
// ========================================

/*
   FUNÇÃO PRINCIPAL - CLIQUE DO BOTÃO "OBRIGADO"
   
   Esta é a função mais importante - ela é executada quando usuário clica o botão
   Faz uma requisição POST para a API e exibe a resposta
*/
async function handleButtonClick() {
    console.log('👆 Botão clicado - Fazendo requisição para API...');
    
    // Desabilita botão para evitar múltiplos cliques
    setButtonState(true, 'Enviando...');
    
    try {
        /*
           REQUISIÇÃO POST PARA API
           
           POST: Usado para "ações" (como clicar botão)
           Diferente do GET que apenas "busca" informações
        */
        const response = await fetch(API_ENDPOINTS.obrigado, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            // Não enviamos dados no body pois é apenas um clique simples
            body: JSON.stringify({
                action: 'button_clicked',
                timestamp: new Date().toISOString()
            }),
            // Timeout de 5 segundos
            signal: AbortSignal.timeout(5000)
        });
        
        if (response.ok) {
            /*
               SUCESSO - API RESPONDEU CORRETAMENTE
               Converte resposta JSON e exibe mensagem
            */
            const data = await response.json();
            console.log('✅ Resposta da API:', data);
            
            // Exibe mensagem "Obrigado!" na tela
            showThankMessage(data.message || 'Obrigado!');
            
            // Efeito visual no botão
            createRippleEffect();
            
        } else {
            /*
               ERRO HTTP - API respondeu com erro
               Status diferentes de 2xx (200, 201, etc.)
            */
            throw new Error(`Erro HTTP ${response.status}: ${response.statusText}`);
        }
        
    } catch (error) {
        /*
           ERRO DE CONEXÃO OU TIMEOUT
           Rede indisponível, servidor offline, etc.
        */
        console.error('❌ Erro ao chamar API:', error);
        
        // Exibe mensagem de erro para usuário
        showErrorMessage(`Erro: ${error.message}`);
        
        // Atualiza status da API
        updateApiStatus('offline', 'Erro na conexão');
        
    } finally {
        /*
           SEMPRE EXECUTADO (sucesso ou erro)
           Reabilita o botão para novo uso
        */
        setTimeout(() => {
            setButtonState(false, 'Clique Aqui');
        }, 1000); // Aguarda 1 segundo antes de reabilitar
    }
}

// ========================================
// FUNÇÕES DE INTERFACE (UI)
// ========================================

/*
   FUNÇÃO PARA ALTERAR ESTADO DO BOTÃO
   
   Habilita/desabilita botão e altera texto
   Evita que usuário clique múltiplas vezes seguidas
*/
function setButtonState(disabled, text) {
    if (!elements.thankButton) return;
    
    elements.thankButton.disabled = disabled;
    elements.thankButton.textContent = text;
    
    if (disabled) {
        elements.thankButton.classList.add('disabled');
    } else {
        elements.thankButton.classList.remove('disabled');
    }
}

/*
   FUNÇÃO PARA EXIBIR MENSAGEM DE SUCESSO
   
   Mostra a mensagem "Obrigado!" com animação
*/
function showThankMessage(message) {
    if (!elements.thankMessage) return;
    
    // Define texto da mensagem
    elements.thankMessage.textContent = message;
    
    // Remove classe hidden e adiciona show para exibir com animação
    elements.thankMessage.classList.remove('hidden');
    elements.thankMessage.classList.add('show');
    
    console.log(`💬 Exibindo mensagem: ${message}`);
    
    // Define timer para esconder a mensagem após 3 segundos
    setTimeout(() => {
        hideThankMessage();
    }, 3000);
}

/*
   FUNÇÃO PARA EXIBIR MENSAGEM DE ERRO
   
   Mostra mensagem de erro temporariamente
*/
function showErrorMessage(message) {
    if (!elements.thankMessage) return;
    
    // Altera estilo para erro (vermelho)
    elements.thankMessage.style.color = '#dc3545';
    elements.thankMessage.textContent = message;
    
    elements.thankMessage.classList.remove('hidden');
    elements.thankMessage.classList.add('show');
    
    console.log(`⚠️ Exibindo erro: ${message}`);
    
    // Esconde após 4 segundos e restaura cor original
    setTimeout(() => {
        elements.thankMessage.style.color = '#228b22'; // Verde original
        hideThankMessage();
    }, 4000);
}

/*
   FUNÇÃO PARA ESCONDER MENSAGEM
   
   Remove mensagem da tela com animação
*/
function hideThankMessage() {
    if (!elements.thankMessage) return;
    
    elements.thankMessage.classList.remove('show');
    elements.thankMessage.classList.add('hidden');
    
    console.log('👻 Mensagem ocultada');
}

// ========================================
// EFEITOS VISUAIS
// ========================================

/*
   FUNÇÃO PARA EFEITO DE ONDULAÇÃO NO BOTÃO
   
   Cria efeito visual "ripple" quando botão é clicado
   Similar ao Material Design do Google
*/
function createRippleEffect() {
    if (!elements.thankButton) return;
    
    // Cria elemento circular para animação
    const ripple = document.createElement('span');
    const buttonRect = elements.thankButton.getBoundingClientRect();
    const size = Math.max(buttonRect.width, buttonRect.height);
    
    // Configura tamanho e posição do efeito
    ripple.style.width = ripple.style.height = size + 'px';
    ripple.style.left = (buttonRect.width / 2 - size / 2) + 'px';
    ripple.style.top = (buttonRect.height / 2 - size / 2) + 'px';
    ripple.classList.add('ripple');
    
    // Estilos CSS inline para o efeito
    ripple.style.position = 'absolute';
    ripple.style.borderRadius = '50%';
    ripple.style.background = 'rgba(255, 255, 255, 0.6)';
    ripple.style.transform = 'scale(0)';
    ripple.style.animation = 'ripple 600ms linear';
    ripple.style.pointerEvents = 'none';
    
    // Remove ripples anteriores
    const existingRipples = elements.thankButton.querySelectorAll('.ripple');
    existingRipples.forEach(r => r.remove());
    
    // Adiciona novo ripple
    elements.thankButton.appendChild(ripple);
    
    // Remove elemento após animação
    setTimeout(() => {
        ripple.remove();
    }, 600);
    
    console.log('💫 Efeito ripple executado');
}

// ========================================
// UTILITÁRIOS E DEBUGGING
// ========================================

/*
   FUNÇÃO PARA DEBUG/DESENVOLVIMENTO
   
   Fornece informações úteis no console do navegador
   Ativa apenas se estiver em modo desenvolvimento
*/
function debugInfo() {
    console.log('🔧 INFORMAÇÕES DE DEBUG');
    console.log('📍 API Base URL:', API_BASE_URL);
    console.log('🔗 Endpoints:', API_ENDPOINTS);
    console.log('📱 Elementos:', elements);
    console.log('🌐 User Agent:', navigator.userAgent);
    console.log('📏 Viewport:', {
        width: window.innerWidth,
        height: window.innerHeight
    });
}

/*
   FUNÇÃO PARA TESTAR TODOS OS ENDPOINTS
   
   Útil para verificar se API está totalmente funcional
   Pode ser chamada manualmente no console: testAllEndpoints()
*/
async function testAllEndpoints() {
    console.log('🧪 Testando todos os endpoints...');
    
    for (const [name, url] of Object.entries(API_ENDPOINTS)) {
        try {
            const response = await fetch(url);
            console.log(`✅ ${name}: ${response.status} ${response.statusText}`);
        } catch (error) {
            console.log(`❌ ${name}: ${error.message}`);
        }
    }
}

// ========================================
// TRATAMENTO DE ERROS GLOBAIS
// ========================================

/*
   CAPTURA ERROS JAVASCRIPT NÃO TRATADOS
   
   Evita que erros quebrem a aplicação completamente
*/
window.addEventListener('error', function(event) {
    console.error('❌ Erro JavaScript não tratado:', event.error);
    updateApiStatus('offline', 'Erro na aplicação frontend');
});

/*
   CAPTURA PROMISES REJEITADAS NÃO TRATADAS
   
   Promises são operações assíncronas que podem falhar
*/
window.addEventListener('unhandledrejection', function(event) {
    console.error('❌ Promise rejeitada não tratada:', event.reason);
    event.preventDefault(); // Evita que erro apareça no console
});

// ========================================
// INFORMAÇÕES DE INICIALIZAÇÃO
// ========================================

/*
   LOG DE INFORMAÇÕES IMPORTANTES
   
   Exibe no console informações úteis para desenvolvimento
*/
console.log('%c🚀 PROJETO STEPS V1 - FRONTEND', 'color: #0066cc; font-size: 16px; font-weight: bold;');
console.log('%c📋 Funcionalidades carregadas:', 'color: #28a745; font-weight: bold;');
console.log('   • Comunicação com API Spring Boot');
console.log('   • Verificação automática de status');
console.log('   • Interface responsiva e animada');
console.log('   • Tratamento de erros robusto');
console.log('%c💡 Para debug, digite: debugInfo()', 'color: #ffc107;');
console.log('%c🧪 Para testar API, digite: testAllEndpoints()', 'color: #ffc107;');