#!/bin/bash

###############################################################################
# start-appium.sh
#
# Script para iniciar el servidor Appium
#
# Uso:
#   ./scripts/start-appium.sh
#
# Descripción:
#   - Inicia el servidor Appium en localhost:4723
#   - Requiere que Appium esté instalado globalmente:
#     npm install -g appium
###############################################################################

set -e

echo "=========================================="
echo "Iniciando servidor Appium..."
echo "=========================================="

# Verificar que Appium está instalado
if ! command -v appium &> /dev/null; then
    echo "ERROR: Appium no está instalado"
    echo ""
    echo "Para instalar Appium:"
    echo "  npm install -g appium"
    echo ""
    echo "O con Homebrew (macOS):"
    echo "  brew install appium"
    exit 1
fi

# Mostrar versión
echo "Versión de Appium:"
appium --version
echo ""

# Iniciar servidor en puerto 4723
echo "Iniciando Appium en http://127.0.0.1:4723"
echo "Presiona Ctrl+C para detener el servidor"
echo ""

appium --port 4723 --log-level info

