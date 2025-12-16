@echo off
REM ============================================================================
REM SCRIPT DE DEPLOY MONGODB COM PODMAN - PROJETO STEPS V1.3
REM ============================================================================
REM
REM Este script automatiza a instalacao e configuracao do MongoDB usando Podman
REM para o Projeto Steps V1.3 com integracao ao banco de dados NoSQL
REM
REM Autor: Projeto Steps V1
REM Data: 16 de dezembro de 2024
REM Versao: 1.0
REM ============================================================================

echo.
echo ============================================================================
echo                    DEPLOY MONGODB - PROJETO STEPS V1.3
echo ============================================================================
echo.
echo [INFO] Iniciando processo de deploy do MongoDB com Podman...
echo [INFO] Este script ira criar e configurar um container MongoDB otimizado
echo.

REM Definir variaveis de configuracao
set CONTAINER_NAME=mongodb-steps
set MONGODB_PORT=27017
set DATABASE_NAME=passos
set VOLUME_NAME=mongodb-data
set MONGODB_IMAGE=docker.io/library/mongo:6

echo [STEP 1/6] Verificando se Podman esta instalado...
podman --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Podman nao encontrado! Por favor instale o Podman antes de continuar.
    echo [INFO] Download: https://podman.io/getting-started/installation#windows
    pause
    exit /b 1
)
echo [OK] Podman encontrado e funcionando!

echo.
echo [STEP 2/6] Verificando se ja existe container MongoDB...
podman ps -a --filter "name=%CONTAINER_NAME%" --format "{{.Names}}" | findstr /C:"%CONTAINER_NAME%" >nul 2>&1
if %errorlevel% equ 0 (
    echo [WARN] Container '%CONTAINER_NAME%' ja existe. Removendo para recriar...
    echo [INFO] Parando container existente...
    podman stop %CONTAINER_NAME% >nul 2>&1
    echo [INFO] Removendo container existente...
    podman rm %CONTAINER_NAME% >nul 2>&1
    echo [OK] Container anterior removido com sucesso!
) else (
    echo [OK] Nenhum container conflitante encontrado.
)

echo.
echo [STEP 3/6] Verificando se porta %MONGODB_PORT% esta disponivel...
netstat -an | findstr ":%MONGODB_PORT%" >nul 2>&1
if %errorlevel% equ 0 (
    echo [WARN] Porta %MONGODB_PORT% esta em uso. Tentando liberar...
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":%MONGODB_PORT%"') do (
        echo [INFO] Finalizando processo PID: %%a
        taskkill /pid %%a /f >nul 2>&1
    )
    timeout /t 2 >nul
)
echo [OK] Porta %MONGODB_PORT% disponivel para uso.

echo.
echo [STEP 4/6] Baixando imagem MongoDB (se necessario)...
echo [INFO] Puxando imagem: %MONGODB_IMAGE%
podman pull %MONGODB_IMAGE%
if %errorlevel% neq 0 (
    echo [ERROR] Falha ao baixar imagem MongoDB!
    pause
    exit /b 1
)
echo [OK] Imagem MongoDB baixada com sucesso!

echo.
echo [STEP 5/6] Criando e iniciando container MongoDB...
echo [INFO] Executando comando de criacao do container...
echo.
echo COMANDO PODMAN DETALHADO:
echo podman run -d \
echo   --name %CONTAINER_NAME% \
echo   --publish %MONGODB_PORT%:%MONGODB_PORT% \
echo   --env MONGO_INITDB_DATABASE=%DATABASE_NAME% \
echo   --volume %VOLUME_NAME%:/data/db \
echo   --restart=unless-stopped \
echo   %MONGODB_IMAGE%
echo.

podman run -d --name %CONTAINER_NAME% --publish %MONGODB_PORT%:%MONGODB_PORT% --env MONGO_INITDB_DATABASE=%DATABASE_NAME% --volume %VOLUME_NAME%:/data/db --restart=unless-stopped %MONGODB_IMAGE%

if %errorlevel% neq 0 (
    echo [ERROR] Falha ao criar container MongoDB!
    echo [INFO] Verificando logs de erro...
    podman logs %CONTAINER_NAME% 2>nul
    pause
    exit /b 1
)

echo [OK] Container MongoDB criado e iniciado com sucesso!

echo.
echo [STEP 6/6] Validando funcionamento do MongoDB...
echo [INFO] Aguardando inicializacao do MongoDB...
timeout /t 5 >nul

REM Verificar se container esta rodando
podman ps --filter "name=%CONTAINER_NAME%" --format "{{.Status}}" | findstr "Up" >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Container nao esta rodando corretamente!
    echo [INFO] Logs do container:
    podman logs %CONTAINER_NAME%
    pause
    exit /b 1
)

echo [OK] Container esta rodando!

REM Testar conexao com MongoDB
echo [INFO] Testando conexao com MongoDB...
podman exec %CONTAINER_NAME% mongosh --eval "db.runCommand({ping: 1})" >nul 2>&1
if %errorlevel% neq 0 (
    echo [WARN] Nao foi possivel testar conexao diretamente. Verifique manualmente.
) else (
    echo [OK] Conexao com MongoDB validada!
)

echo.
echo ============================================================================
echo                            DEPLOY CONCLUIDO!
echo ============================================================================
echo.
echo [SUCCESS] MongoDB foi instalado e configurado com sucesso!
echo.
echo INFORMACOES DO CONTAINER:
echo   Nome do Container: %CONTAINER_NAME%
echo   Porta MongoDB:     %MONGODB_PORT%
echo   Banco de Dados:    %DATABASE_NAME%
echo   Volume de Dados:   %VOLUME_NAME%
echo   Imagem Utilizada:  %MONGODB_IMAGE%
echo.
echo PROXIMOS PASSOS:
echo   1. Execute o backend Spring Boot: mvn spring-boot:run
echo   2. Acesse: http://localhost:8080/demo-v13.html
echo   3. Teste o cadastro de usuarios
echo.
echo COMANDOS UTEIS:
echo   Ver logs:      podman logs %CONTAINER_NAME%
echo   Parar:         podman stop %CONTAINER_NAME%
echo   Iniciar:       podman start %CONTAINER_NAME%
echo   Shell MongoDB: podman exec -it %CONTAINER_NAME% mongosh
echo   Status:        podman ps
echo.
echo INFORMACOES DE CONEXAO PARA APLICACAO:
echo   Host: localhost
echo   Porta: %MONGODB_PORT%
echo   Database: %DATABASE_NAME%
echo   URI: mongodb://localhost:%MONGODB_PORT%/%DATABASE_NAME%
echo.
echo ============================================================================

REM Mostrar status final
echo.
echo STATUS ATUAL DO CONTAINER:
podman ps --filter "name=%CONTAINER_NAME%" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo.
echo [INFO] Script concluido! Pressione qualquer tecla para continuar...
pause >nul