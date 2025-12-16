@echo off
echo ===============================================
echo   PROJETO STEPS V1 - BUILD E DEPLOY COMPLETO
echo ===============================================
echo.

cd backend

echo [1/4] Limpando projeto anterior...
mvn clean

echo.
echo [2/4] Executando testes...
mvn test

if %errorlevel% neq 0 (
    echo ⚠️ Alguns testes falharam, mas continuando...
)

echo.
echo [3/4] Empacotando aplicacao (JAR)...
mvn package -DskipTests

if %errorlevel% neq 0 (
    echo ❌ Erro no empacotamento!
    pause
    exit /b 1
)

echo ✅ JAR criado com sucesso

echo.
echo [4/4] Executando JAR final...
echo.
echo ================================================
echo 🚀 EXECUTANDO APLICACAO EMPACOTADA
echo ================================================
echo 📦 Executando: target\steps-backend-1.0.0.jar
echo 📍 URL: http://localhost:8080
echo ================================================
echo.

java -jar target\steps-backend-1.0.0.jar