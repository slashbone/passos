@echo off
chcp 65001 > nul
color 0B
title Executar Steps V1.4 - SQL Server Edition

echo.
echo ===============================================
echo   🚀 STEPS V1.4 - EXECUTAR APLICAÇÃO COMPLETA
echo ===============================================
echo.
echo 🎯 Este script vai:
echo    1. Verificar e iniciar SQL Server no Podman
echo    2. Executar Spring Boot com perfil SQL Server
echo    3. Abrir frontend automaticamente
echo.
pause

echo.
echo [INFO] Verificando se Podman está disponível...
podman --version > nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo ❌ ERRO: Podman não encontrado!
    echo 💡 Execute primeiro: .\setup-sqlserver-podman.bat
    pause
    exit /b 1
)

echo ✅ Podman encontrado!

echo.
echo [INFO] Verificando container SQL Server...
podman ps --format "table {{.Names}}" | findstr "sqlserver-steps" > nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo [INFO] Container SQL Server não está rodando. Iniciando...
    podman start sqlserver-steps
    if %errorlevel% neq 0 (
        echo ❌ Erro ao iniciar SQL Server. Execute: .\setup-sqlserver-podman.bat
        pause
        exit /b 1
    )
    echo ✅ SQL Server iniciado!
    echo [INFO] Aguardando SQL Server ficar disponível...
    timeout /t 10 > nul
) else (
    echo ✅ SQL Server já está rodando!
)

echo.
echo [INFO] Testando conexão com SQL Server...
podman exec sqlserver-steps /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "StepsPassword123!" -Q "SELECT 1" > nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ SQL Server não está respondendo!
    echo 💡 Verifique logs: podman logs sqlserver-steps
    pause
    exit /b 1
)

echo ✅ SQL Server respondendo corretamente!

echo.
echo [INFO] Entrando no diretório backend...
cd backend
if not exist "pom.xml" (
    echo ❌ ERRO: Arquivo pom.xml não encontrado!
    echo 💡 Execute este script do diretório raiz do projeto
    pause
    exit /b 1
)

echo.
echo [INFO] Compilando projeto Spring Boot...
echo 📦 Executando: mvn clean compile
mvn clean compile
if %errorlevel% neq 0 (
    echo ❌ ERRO na compilação!
    echo 💡 Verifique as dependências e configurações do Maven
    pause
    exit /b 1
)

echo ✅ Compilação concluída com sucesso!

echo.
echo [INFO] Iniciando Spring Boot com perfil SQL Server...
echo 🚀 Executando: mvn spring-boot:run -Dspring-boot.run.profiles=sqlserver
echo.
echo ===============================================
echo   ✨ APLICAÇÃO INICIANDO...
echo ===============================================
echo.
echo 🌐 URLs da aplicação:
echo    📋 API:        http://localhost:8080/api/v1.4/info
echo    👥 Usuários:   http://localhost:8080/api/v1.4/usuarios  
echo    📊 Stats:      http://localhost:8080/api/v1.4/estatisticas
echo    💓 Health:     http://localhost:8080/actuator/health
echo.
echo 🎯 FRONTEND SEPARADO:
echo    Abra o arquivo: frontend-v14\index.html
echo    Ou execute: .\abrir-frontend-v14.bat
echo.
echo ⚠️  Para parar a aplicação: Ctrl+C
echo ===============================================

start "" ".\abrir-frontend-v14.bat"

mvn spring-boot:run -Dspring-boot.run.profiles=sqlserver