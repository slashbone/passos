@echo off
chcp 65001 > nul
color 0E
title Frontend Steps V1.4 - Interface Separada

echo.
echo ===============================================
echo   🌐 FRONTEND STEPS V1.4 - INTERFACE SEPARADA
echo ===============================================
echo.
echo 🎯 Abrindo interface frontend no navegador...
echo 📁 Localização: frontend-v14\index.html
echo.

:: Verificar se o arquivo existe
if not exist "frontend-v14\index.html" (
    echo ❌ ERRO: Arquivo frontend não encontrado!
    echo 💡 Verifique se está no diretório correto do projeto
    pause
    exit /b 1
)

:: Obter caminho absoluto do arquivo
for %%F in ("frontend-v14\index.html") do set "FRONTEND_PATH=%%~fF"

echo 🚀 Abrindo: %FRONTEND_PATH%
echo.

:: Abrir no navegador padrão
start "" "%FRONTEND_PATH%"

echo ✅ Frontend aberto no navegador!
echo.
echo 💡 DICAS DE USO:
echo    - Certifique-se que a API está rodando (localhost:8080)
echo    - Use F5 para atualizar dados
echo    - Use Ctrl+N para focar no campo de nome
echo    - Use ESC para fechar modais
echo.
echo 🔧 Para desenvolvimento local, você também pode usar:
echo    - Live Server (VS Code)
echo    - Python: python -m http.server 8000
echo    - Node.js: npx serve .
echo.
pause