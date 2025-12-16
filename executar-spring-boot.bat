@echo off
echo ===============================================
echo     PROJETO STEPS V1 - SPRING BOOT BACKEND
echo ===============================================
echo.

cd backend

echo [1/3] Verificando ambiente...
where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven nao encontrado! 
    echo    Instale o Maven ou use o wrapper incluido.
    echo    Download: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

where java >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java nao encontrado!
    echo    Instale Java 17+ ou configure JAVA_HOME
    pause
    exit /b 1
)

echo ✅ Ambiente OK

echo.
echo [2/3] Compilando aplicacao Spring Boot...
mvn clean compile

if %errorlevel% neq 0 (
    echo ❌ Erro na compilacao!
    echo    Verifique os logs acima para detalhes.
    pause
    exit /b 1
)

echo ✅ Compilacao concluida

echo.
echo [3/3] Iniciando servidor Spring Boot...
echo.
echo ================================================
echo 🚀 INICIANDO APLICACAO...
echo ================================================
echo 📍 URL da aplicacao: http://localhost:8080
echo 📊 Actuator Health: http://localhost:8080/actuator/health
echo 📈 Todos endpoints: http://localhost:8080/actuator
echo ================================================
echo 💡 Para parar: Ctrl+C
echo ================================================
echo.

mvn spring-boot:run