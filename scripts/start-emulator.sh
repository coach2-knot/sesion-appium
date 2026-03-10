#!/bin/bash

###############################################################################
# start-emulator.sh
#
# Script para iniciar el emulador de Android
#
# Uso:
#   ./scripts/start-emulator.sh
#   ./scripts/start-emulator.sh Medium_Phone
#   ./scripts/start-emulator.sh -avd Medium_Phone
#
# Descripción:
#   - Inicia el emulador de Android
#   - Requiere que Android SDK esté instalado
#   - Por defecto usa Medium_Phone
###############################################################################

set -e

DEFAULT_AVD_NAME="Medium_Phone"
ANDROID_HOME="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-$HOME/Library/Android/sdk}}"
EMULATOR_BIN="$ANDROID_HOME/emulator/emulator"
ADB_BIN="$ANDROID_HOME/platform-tools/adb"
AVD_NAME="$DEFAULT_AVD_NAME"

usage() {
    echo "Uso:"
    echo "  ./scripts/start-emulator.sh"
    echo "  ./scripts/start-emulator.sh <avd-name>"
    echo "  ./scripts/start-emulator.sh -avd <avd-name>"
}

while [[ $# -gt 0 ]]; do
    case "$1" in
        -avd|--avd)
            if [[ -z "${2:-}" ]]; then
                echo "ERROR: Debes indicar un nombre de AVD después de $1"
                echo ""
                usage
                exit 1
            fi
            AVD_NAME="$2"
            shift 2
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        *)
            AVD_NAME="$1"
            shift
            ;;
    esac
done

echo "=========================================="
echo "Iniciando emulador de Android"
echo "=========================================="
echo "AVD: $AVD_NAME"
echo ""

# Verificar que ANDROID_HOME está configurado
if [ ! -d "$ANDROID_HOME" ]; then
    echo "ERROR: Android SDK no está configurado correctamente"
    echo "ANDROID_HOME / ANDROID_SDK_ROOT: $ANDROID_HOME"
    echo ""
    echo "Por favor configura ANDROID_HOME o ANDROID_SDK_ROOT en tu ~/.bash_profile o ~/.zshrc:"
    echo "  export ANDROID_HOME=\$HOME/Library/Android/sdk"
    exit 1
fi

# Verificar que emulator existe
if [ ! -f "$EMULATOR_BIN" ]; then
    echo "ERROR: Emulador no encontrado en $ANDROID_HOME/emulator/"
    exit 1
fi

# Verificar que adb existe
if [ ! -f "$ADB_BIN" ]; then
    echo "ERROR: adb no encontrado en $ANDROID_HOME/platform-tools/"
    exit 1
fi

# Listar AVDs disponibles
echo "AVDs disponibles:"
"$EMULATOR_BIN" -list-avds
echo ""

if ! "$EMULATOR_BIN" -list-avds | grep -Fxq "$AVD_NAME"; then
    echo "ERROR: El AVD '$AVD_NAME' no existe"
    echo ""
    echo "Usa uno de los AVDs listados arriba o ejecuta:"
    echo "  ./scripts/start-emulator.sh -avd Medium_Phone"
    exit 1
fi

# Iniciar emulador
echo "Iniciando emulador $AVD_NAME..."
echo "Esto puede tomar algunos minutos..."
echo ""

"$EMULATOR_BIN" -avd "$AVD_NAME" &

# Esperar a que el emulador esté listo
echo "Esperando a que el emulador se inicie..."
sleep 10

# Verificar estado
for i in {1..30}; do
    if "$ADB_BIN" wait-for-device >/dev/null 2>&1 && "$ADB_BIN" shell getprop sys.boot_completed 2>/dev/null | grep -q 1; then
        echo ""
        echo "✓ Emulador está listo"
        echo ""
        "$ADB_BIN" devices
        exit 0
    fi
    echo "Intento $i/30..."
    sleep 2
done

echo "ADVERTENCIA: Emulador puede no estar completamente listo"
echo "Puedes verificar el estado con: $ADB_BIN devices"
