#!/bin/bash

# Script pour vérifier la conformité des commentaires dans le code source
# Usage: ./check-comments.sh [directory]

# Répertoire à analyser (par défaut: src)
DIR=${1:-src}

# Couleurs pour l'affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo "Vérification des commentaires dans le répertoire: $DIR"
echo "======================================================"

# Compteurs
TOTAL_FILES=0
FILES_WITH_COPYRIGHT=0
FILES_WITH_CLASS_JAVADOC=0
FILES_WITH_METHOD_JAVADOC=0
FILES_MISSING_COPYRIGHT=0
FILES_MISSING_CLASS_JAVADOC=0
METHODS_MISSING_JAVADOC=0

# Trouver tous les fichiers Java
JAVA_FILES=$(find $DIR -name "*.java")

# Parcourir chaque fichier Java
for FILE in $JAVA_FILES; do
    TOTAL_FILES=$((TOTAL_FILES + 1))
    
    # Vérifier l'en-tête de copyright
    if grep -q "Copyright (c) [0-9]\{4\} DEVOLUTION" "$FILE"; then
        FILES_WITH_COPYRIGHT=$((FILES_WITH_COPYRIGHT + 1))
    else
        FILES_MISSING_COPYRIGHT=$((FILES_MISSING_COPYRIGHT + 1))
        echo -e "${YELLOW}Fichier sans en-tête de copyright:${NC} $FILE"
    fi
    
    # Vérifier la documentation de classe
    if grep -q "^\s*\/\*\*\s*$" "$FILE" && grep -q "^\s*\*\s*[A-Za-z]" "$FILE"; then
        FILES_WITH_CLASS_JAVADOC=$((FILES_WITH_CLASS_JAVADOC + 1))
    else
        FILES_MISSING_CLASS_JAVADOC=$((FILES_MISSING_CLASS_JAVADOC + 1))
        echo -e "${YELLOW}Fichier sans documentation de classe:${NC} $FILE"
    fi
    
    # Compter les méthodes publiques
    PUBLIC_METHODS=$(grep -c "^\s*public\s\+[A-Za-z0-9_<>]\+\s\+[A-Za-z0-9_]\+\s*(" "$FILE")
    
    # Compter les méthodes avec JavaDoc
    METHODS_WITH_JAVADOC=$(grep -c "^\s*\/\*\*\s*$" "$FILE")
    
    # Si le nombre de méthodes publiques est supérieur au nombre de JavaDoc
    if [ $PUBLIC_METHODS -gt $METHODS_WITH_JAVADOC ]; then
        MISSING=$(($PUBLIC_METHODS - $METHODS_WITH_JAVADOC))
        METHODS_MISSING_JAVADOC=$((METHODS_MISSING_JAVADOC + MISSING))
        echo -e "${YELLOW}Fichier avec méthodes sans JavaDoc ($MISSING manquantes):${NC} $FILE"
    fi
    
    FILES_WITH_METHOD_JAVADOC=$((FILES_WITH_METHOD_JAVADOC + METHODS_WITH_JAVADOC))
done

# Afficher les statistiques
echo "======================================================"
echo "Statistiques:"
echo "Nombre total de fichiers Java: $TOTAL_FILES"
echo "Fichiers avec en-tête de copyright: $FILES_WITH_COPYRIGHT ($(($FILES_WITH_COPYRIGHT * 100 / $TOTAL_FILES))%)"
echo "Fichiers avec documentation de classe: $FILES_WITH_CLASS_JAVADOC ($(($FILES_WITH_CLASS_JAVADOC * 100 / $TOTAL_FILES))%)"
echo "Méthodes avec JavaDoc: $FILES_WITH_METHOD_JAVADOC"
echo "======================================================"
echo "Problèmes détectés:"
echo "Fichiers sans en-tête de copyright: $FILES_MISSING_COPYRIGHT"
echo "Fichiers sans documentation de classe: $FILES_MISSING_CLASS_JAVADOC"
echo "Méthodes sans JavaDoc: $METHODS_MISSING_JAVADOC"
echo "======================================================"

# Déterminer le statut de sortie
if [ $FILES_MISSING_COPYRIGHT -eq 0 ] && [ $FILES_MISSING_CLASS_JAVADOC -eq 0 ] && [ $METHODS_MISSING_JAVADOC -eq 0 ]; then
    echo -e "${GREEN}Tous les fichiers sont conformes aux standards de commentaires!${NC}"
    exit 0
else
    echo -e "${RED}Certains fichiers ne sont pas conformes aux standards de commentaires.${NC}"
    exit 1
fi
