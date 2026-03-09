#!/bin/bash
# ============================================================
# Career Expert System - Quick Run Script
# ============================================================

echo "=============================================="
echo "  Career Path & Course Recommender System"
echo "  Prolog + Java Expert System"
echo "=============================================="
echo ""

# Check for Java
if ! command -v java &> /dev/null; then
    echo "[ERROR] Java not found. Please install Java 17+."
    exit 1
fi

# Check for Maven
if ! command -v mvn &> /dev/null; then
    echo "[ERROR] Maven not found. Please install Maven 3.8+."
    exit 1
fi

# Check for SWI-Prolog (optional)
if command -v swipl &> /dev/null; then
    echo "[OK] SWI-Prolog found: $(swipl --version 2>&1 | head -1)"
    echo "     Prolog inference engine will be used."
else
    echo "[INFO] SWI-Prolog not found. Java fallback engine will be used."
    echo "       To install SWI-Prolog: https://www.swi-prolog.org/download/stable"
fi

echo ""
echo "Building Java application..."
cd java
mvn clean package -q

if [ $? -ne 0 ]; then
    echo "[ERROR] Build failed. Check Maven output above."
    exit 1
fi

echo "[OK] Build successful."
echo ""
echo "Running tests..."
mvn test -q
echo ""
echo "Launching GUI..."
java -jar target/CareerExpertSystem.jar
