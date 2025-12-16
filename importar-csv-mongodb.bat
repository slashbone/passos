@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo ===============================================
echo   IMPORTADOR CSV PARA MONGODB - PROJETO STEPS
echo ===============================================
echo.

REM Definir cores
for /F %%a in ('echo prompt $E ^| cmd') do set "ESC=%%a"
set "GREEN=!ESC![32m"
set "RED=!ESC![31m"
set "YELLOW=!ESC![33m"
set "BLUE=!ESC![34m"
set "RESET=!ESC![0m"

REM Verificar se o arquivo CSV existe
set "CSV_FILE=cadastro-mongo.csv"
if not exist "%CSV_FILE%" (
    echo !RED![ERRO]!RESET! Arquivo %CSV_FILE% não encontrado!
    echo Por favor, certifique-se que o arquivo existe no diretório atual.
    pause
    exit /b 1
)

REM Verificar se a API está rodando
echo !BLUE![INFO]!RESET! Verificando se a API está rodando...
powershell -Command "try { $response = Invoke-RestMethod -Uri 'http://localhost:8080/api/v1.3/' -Method GET; if ($response) { exit 0 } else { exit 1 } } catch { exit 1 }"
if %errorlevel% neq 0 (
    echo !RED![ERRO]!RESET! API não está respondendo em http://localhost:8080
    echo.
    echo Por favor, inicie o Spring Boot primeiro:
    echo 1. Abra um novo terminal
    echo 2. Execute: cd backend
    echo 3. Execute: mvn spring-boot:run
    echo.
    pause
    exit /b 1
)

echo !GREEN![SUCESSO]!RESET! API está respondendo!
echo.

REM Inicializar contadores
set "TOTAL_LINHAS=0"
set "SUCESSO=0"
set "ERROS=0"
set "LINHA_ATUAL=0"

REM Contar total de linhas (excluindo cabeçalho)
for /f "skip=1 tokens=*" %%i in (%CSV_FILE%) do (
    set /a TOTAL_LINHAS+=1
)

echo !BLUE![INFO]!RESET! Encontradas %TOTAL_LINHAS% linhas para importar
echo !BLUE![INFO]!RESET! Iniciando importação...
echo.

REM Processar cada linha do CSV (pular cabeçalho)
for /f "skip=1 tokens=1,2 delims=," %%a in (%CSV_FILE%) do (
    set /a LINHA_ATUAL+=1
    set "NOME=%%a"
    set "DATA_RAW=%%b"
    
    REM Remover espaços em branco
    for /f "tokens=* delims= " %%x in ("!NOME!") do set "NOME=%%x"
    for /f "tokens=* delims= " %%x in ("!DATA_RAW!") do set "DATA_RAW=%%x"
    
    REM Converter data de ddmmaaaa para aaaa-mm-dd
    set "DIA=!DATA_RAW:~0,2!"
    set "MES=!DATA_RAW:~2,2!"
    set "ANO=!DATA_RAW:~4,4!"
    set "DATA_ISO=!ANO!-!MES!-!DIA!"
    
    echo [!LINHA_ATUAL!/!TOTAL_LINHAS!] Importando: !NOME! ^(!DATA_ISO!^)
    
    REM Criar JSON e fazer requisição
    set "JSON={\"nome\": \"!NOME!\", \"dataNascimento\": \"!DATA_ISO!\"}"
    
    REM Fazer requisição POST usando PowerShell
    powershell -Command "$json = '!JSON!'; try { $headers = @{'Content-Type'='application/json'; 'Accept'='application/json'}; $response = Invoke-RestMethod -Uri 'http://localhost:8080/api/v1.3/usuarios' -Method POST -Body $json -Headers $headers -ContentType 'application/json'; Write-Host '  ✅ Sucesso: ID' $response.usuario.id -ForegroundColor Green; exit 0 } catch { Write-Host '  ❌ Erro:' $_.Exception.Message -ForegroundColor Red; exit 1 }" 2>nul
    
    if !errorlevel! equ 0 (
        set /a SUCESSO+=1
    ) else (
        set /a ERROS+=1
    )
    
    REM Pequena pausa para não sobrecarregar a API
    timeout /t 1 /nobreak >nul
)

echo.
echo ===============================================
echo   RELATÓRIO DE IMPORTAÇÃO
echo ===============================================
echo.
echo !BLUE!Total de registros processados:!RESET! %TOTAL_LINHAS%
echo !GREEN!Sucessos:!RESET! %SUCESSO%
echo !RED!Erros:!RESET! %ERROS%
echo.

if %ERROS% equ 0 (
    echo !GREEN![SUCESSO TOTAL]!RESET! Todos os registros foram importados com sucesso!
) else (
    echo !YELLOW![ATENÇÃO]!RESET! Alguns registros falharam na importação.
    echo Verifique se:
    echo - O MongoDB está rodando
    echo - A API está funcionando corretamente
    echo - Os dados do CSV estão no formato correto
)

echo.
echo ===============================================
echo   VERIFICAÇÃO DOS DADOS IMPORTADOS
echo ===============================================
echo.

REM Verificar quantos usuários foram importados
echo !BLUE![INFO]!RESET! Consultando estatísticas da API...
powershell -Command "try { $response = Invoke-RestMethod -Uri 'http://localhost:8080/api/v1.3/estatisticas' -Method GET; Write-Host 'Total de usuários no banco:' $response.total_usuarios -ForegroundColor Cyan; exit 0 } catch { Write-Host 'Erro ao consultar estatísticas' -ForegroundColor Red; exit 1 }"

echo.
echo !BLUE![INFO]!RESET! Para ver todos os usuários cadastrados, acesse:
echo   !YELLOW!http://localhost:8080/demo-v13.html!RESET!
echo   !YELLOW!http://localhost:8080/demo-v131.html!RESET!
echo.

REM Perguntar se quer ver os dados importados
set /p "VER_DADOS=Deseja ver os últimos 5 usuários cadastrados? (s/n): "
if /i "!VER_DADOS!"=="s" (
    echo.
    echo !BLUE![INFO]!RESET! Últimos usuários cadastrados:
    powershell -Command "try { $response = Invoke-RestMethod -Uri 'http://localhost:8080/api/v1.3/usuarios' -Method GET; $usuarios = $response.usuarios | Select-Object -Last 5; foreach($user in $usuarios) { Write-Host '- Nome:' $user.nome '| Data:' $user.dataNascimento '| ID:' $user.id.Substring(0,8)'...' -ForegroundColor Green } } catch { Write-Host 'Erro ao listar usuários' -ForegroundColor Red }"
)

echo.
echo !GREEN![CONCLUÍDO]!RESET! Importação finalizada!
echo.
pause