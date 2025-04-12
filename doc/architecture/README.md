# Documentation d'Architecture

Ce répertoire contient la documentation d'architecture du système.

## Documents d'Architecture (ADR)

Les documents d'architecture (Architecture Decision Records - ADR) décrivent les décisions architecturales prises, les
patterns utilisés et les justifications de ces choix.

- [Module Auto](auto-module-adr.md) - Document d'architecture du module Auto

## Diagrammes

Les diagrammes UML sont disponibles dans le répertoire `diagrams`. Ils sont au format PlantUML et peuvent être
visualisés avec des outils comme:

- [PlantUML Online Server](http://www.plantuml.com/plantuml/uml/)
- Plugin PlantUML pour IntelliJ IDEA, VS Code, etc.
- Outils de génération d'images à partir de fichiers PlantUML

### Diagrammes disponibles

- [Architecture du module Auto](diagrams/auto-module-architecture.puml) - Vue d'ensemble de l'architecture du module
  Auto
- [Modèle de domaine du module Auto](diagrams/auto-module-domain-model.puml) - Entités et relations du module Auto
- [Diagramme de séquence - Création de police](diagrams/auto-module-sequence-create-policy.puml) - Séquence de création
  d'une police d'assurance
- [Diagramme de composants](diagrams/auto-module-component.puml) - Composants principaux et leurs interactions

## Comment utiliser cette documentation

1. Consultez les ADR pour comprendre les décisions architecturales
2. Visualisez les diagrammes pour comprendre la structure et les interactions
3. Référez-vous à cette documentation lors du développement pour assurer la cohérence avec l'architecture définie

## Mise à jour de la documentation

Cette documentation doit être mise à jour lorsque:

- De nouvelles décisions architecturales sont prises
- Des changements significatifs sont apportés à l'architecture
- De nouveaux composants sont ajoutés au système

Pour mettre à jour un ADR:

1. Créez une nouvelle section dans le document existant ou créez un nouveau document
2. Décrivez le contexte, la décision et les conséquences
3. Mettez à jour les diagrammes si nécessaire

Pour mettre à jour un diagramme:

1. Modifiez le fichier PlantUML correspondant
2. Régénérez l'image si nécessaire
3. Mettez à jour les références dans les documents
