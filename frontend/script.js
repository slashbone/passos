// Aguarda o carregamento completo do DOM
document.addEventListener('DOMContentLoaded', function() {
    
    // Seleciona os elementos da página
    const thankButton = document.getElementById('thankButton');
    const thankMessage = document.getElementById('thankMessage');
    
    // Variável para controlar o timer
    let hideMessageTimer;
    
    // Função para mostrar a mensagem de agradecimento
    function showThankMessage() {
        // Remove a classe hidden e adiciona show para exibir com animação
        thankMessage.classList.remove('hidden');
        thankMessage.classList.add('show');
        
        // Limpa timer anterior se existir
        if (hideMessageTimer) {
            clearTimeout(hideMessageTimer);
        }
        
        // Define timer para esconder a mensagem após 2 segundos
        hideMessageTimer = setTimeout(function() {
            hideThankMessage();
        }, 2000);
        
        // Feedback visual no botão
        thankButton.style.transform = 'scale(0.95)';
        setTimeout(function() {
            thankButton.style.transform = 'scale(1)';
        }, 150);
    }
    
    // Função para esconder a mensagem de agradecimento
    function hideThankMessage() {
        thankMessage.classList.remove('show');
        thankMessage.classList.add('hidden');
    }
    
    // Event listener para o clique do botão
    thankButton.addEventListener('click', function(e) {
        e.preventDefault();
        showThankMessage();
        
        // Adiciona efeito de ondulação no clique
        createRippleEffect(e, thankButton);
    });
    
    // Função para criar efeito de ondulação no botão
    function createRippleEffect(event, element) {
        const circle = document.createElement('span');
        const diameter = Math.max(element.clientWidth, element.clientHeight);
        const radius = diameter / 2;
        
        const rect = element.getBoundingClientRect();
        circle.style.width = circle.style.height = diameter + 'px';
        circle.style.left = (event.clientX - rect.left - radius) + 'px';
        circle.style.top = (event.clientY - rect.top - radius) + 'px';
        circle.classList.add('ripple');
        
        // Adiciona estilos CSS inline para o efeito ripple
        circle.style.position = 'absolute';
        circle.style.borderRadius = '50%';
        circle.style.background = 'rgba(255, 255, 255, 0.6)';
        circle.style.transform = 'scale(0)';
        circle.style.animation = 'ripple 600ms linear';
        circle.style.pointerEvents = 'none';
        
        // Remove ripples anteriores
        const ripples = element.querySelectorAll('.ripple');
        ripples.forEach(ripple => ripple.remove());
        
        element.appendChild(circle);
        
        // Remove o elemento após a animação
        setTimeout(() => {
            circle.remove();
        }, 600);
    }
    
    // Adiciona animação CSS para o ripple effect
    const style = document.createElement('style');
    style.textContent = `
        @keyframes ripple {
            to {
                transform: scale(4);
                opacity: 0;
            }
        }
        
        .action-button {
            position: relative;
            overflow: hidden;
        }
    `;
    document.head.appendChild(style);
    
    // Adiciona suporte para teclado (Enter e Espaço)
    thankButton.addEventListener('keydown', function(e) {
        if (e.key === 'Enter' || e.key === ' ') {
            e.preventDefault();
            showThankMessage();
        }
    });
    
    // Efeito de entrada suave para a página
    document.body.style.opacity = '0';
    setTimeout(function() {
        document.body.style.transition = 'opacity 0.5s ease-in';
        document.body.style.opacity = '1';
    }, 100);
    
    // Console log para debug
    console.log('Interface Web - Projeto Steps V1 carregado com sucesso!');
    console.log('Funcionalidades ativas: Botão interativo, animações, efeitos visuais');
    
});