@echo off
chcp 65001 > nul
color 0A
title Setup SQL Server no Podman - Projeto Steps V1.4

echo.
echo ===============================================
echo   SETUP SQL SERVER NO PODMAN - STEPS V1.4
echo ===============================================
echo.
echo 🚀 Este script vai configurar automaticamente:
echo    - Microsoft SQL Server 2022 no Podman
echo    - Banco de dados "steps_db" 
echo    - Tabela "usuarios" com estrutura completa
echo    - Configurações para desenvolvimento
echo.
pause

echo.
echo [INFO] Verificando se Podman está instalado...
podman --version > nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo ❌ ERRO: Podman não encontrado!
    echo.
    echo 💡 COMO INSTALAR PODMAN:
    echo    1. Acesse: https://podman.io/getting-started/installation
    echo    2. Baixe Podman Desktop para Windows
    echo    3. Execute o instalador
    echo    4. Reinicie o sistema
    echo    5. Execute este script novamente
    echo.
    pause
    exit /b 1
)

echo ✅ Podman encontrado!
podman --version

echo.
echo [INFO] Parando container SQL Server existente (se houver)...
podman stop sqlserver-steps 2> nul
podman rm sqlserver-steps 2> nul

echo.
echo [INFO] Baixando imagem do SQL Server 2022...
echo 📦 Isso pode demorar alguns minutos na primeira execução...
podman pull mcr.microsoft.com/mssql/server:2022-latest

echo.
echo [INFO] Criando container SQL Server...
echo 🔧 Configurações:
echo    - Nome do container: sqlserver-steps
echo    - Porta: 1433
echo    - SA Password: StepsPassword123!
echo    - Banco: steps_db

podman run -d ^
  --name sqlserver-steps ^
  -e "ACCEPT_EULA=Y" ^
  -e "SA_PASSWORD=StepsPassword123!" ^
  -e "MSSQL_PID=Express" ^
  -p 1433:1433 ^
  mcr.microsoft.com/mssql/server:2022-latest

if %errorlevel% neq 0 (
    echo.
    echo ❌ ERRO ao criar container SQL Server!
    echo 💡 Verifique se a porta 1433 está livre
    pause
    exit /b 1
)

echo.
echo [INFO] Aguardando SQL Server inicializar...
echo ⏳ Isso pode levar 30-60 segundos...

timeout /t 30 > nul

echo.
echo [INFO] Verificando se SQL Server está respondendo...

for /l %%i in (1,1,10) do (
    podman exec sqlserver-steps /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "StepsPassword123!" -Q "SELECT 1" > nul 2>&1
    if !errorlevel! equ 0 (
        echo ✅ SQL Server está respondendo!
        goto :create_database
    )
    echo [%%i/10] Aguardando SQL Server... 
    timeout /t 5 > nul
)

echo ❌ SQL Server não está respondendo após 50 segundos
echo 💡 Verifique os logs: podman logs sqlserver-steps
pause
exit /b 1

:create_database
echo.
echo [INFO] Criando banco de dados "steps_db"...

podman exec sqlserver-steps /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "StepsPassword123!" -Q ^
"CREATE DATABASE steps_db; USE steps_db; CREATE TABLE usuarios ( id INT IDENTITY(1,1) PRIMARY KEY, nome NVARCHAR(255) NOT NULL, data_nascimento DATE NOT NULL, created_at DATETIME2 DEFAULT GETDATE(), updated_at DATETIME2 DEFAULT GETDATE() ); INSERT INTO usuarios (nome, data_nascimento) VALUES ('Admin Teste', '1990-01-01'), ('Usuario Exemplo', '1985-05-15');"

if %errorlevel% neq 0 (
    echo ❌ ERRO ao criar banco de dados!
    echo 💡 Verifique os logs do container
    pause
    exit /b 1
)

echo.
echo [INFO] Testando conexão e estrutura...
podman exec sqlserver-steps /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "StepsPassword123!" -d steps_db -Q ^
"SELECT COUNT(*) as total_usuarios FROM usuarios; SELECT TOP 1 nome, data_nascimento FROM usuarios;"

echo.
echo ===============================================
echo   ✅ SQL SERVER CONFIGURADO COM SUCESSO!
echo ===============================================
echo.
echo 🎯 INFORMAÇÕES DE CONEXÃO:
echo    Host: localhost
echo    Porta: 1433  
echo    Banco: steps_db
echo    Usuário: sa
echo    Senha: StepsPassword123!
echo.
echo 📋 COMANDOS ÚTEIS:
echo    Parar:     podman stop sqlserver-steps
echo    Iniciar:   podman start sqlserver-steps  
echo    Logs:      podman logs sqlserver-steps
echo    Conectar:  podman exec -it sqlserver-steps bash
echo.
echo 🔧 PRÓXIMOS PASSOS:
echo    1. Execute: .\executar-spring-boot-v14.bat
echo    2. Acesse: http://localhost:8080
echo    3. Teste a nova interface com SQL Server!
echo.
pause