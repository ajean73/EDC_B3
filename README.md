## Organisation & Workflow Git

Pour le projet ShopWise, j'ai choisi le **GitHub Flow**. Ce choix va permettre la mise en place d'un pipeline CI/CD automatisée, visant un déploiement continu et robuste. Cela permet aussi un travail collaboratif avec les autres développeurs et la simplification du déploiement d'un fix ou d'une nouvelle fonctionnalité.

### Schéma du flux de travail

```text
main     : ─── C1 ── C2 ─── M1 ─── M2 ────
                       │     ↑      ↑
feature/ :             └─── C3 ┘    │  (ex: feature/US-01-clients)
fix/     :                          └─ C4   (ex: fix/bug-XX-XX)
