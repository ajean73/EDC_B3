## Git Workflow (Feature Branch Workflow)

Le projet utilise un **Feature Branch Workflow**. Chaque fonctionnalité ou correction est développée sur une branche dédiée, puis intégrée dans `main` via une Pull Request.

```text
main     : ─── C1 ────── C2 ─── M1 ─── M2 ───
                          │      ↑       ↑
feature/a:                └── C3 ┘       │
feature/b:                     └── C4 ── C5 ┘
```

### Fonctionnement

1. Créer une branche depuis `main` :

```bash
git switch -c feature/nom-feature
# ou
git switch -c fix/nom-correctif
```

2. Développer et réaliser les commits.

3. Pousser la branche sur GitHub.

4. Ouvrir une Pull Request.

5. Une fois la Pull Request validée, fusionner dans `main`.

6. Supprimer la branche devenue inutile.

### Convention de nommage

- `feature/<nom-feature>` : nouvelle fonctionnalité.
- `fix/<nom-correctif>` : correction de bug.

---

## Pipeline CI/CD

À chaque Pull Request, GitHub Actions exécute automatiquement la pipeline d'intégration continue afin de garantir que le projet reste fonctionnel avant toute fusion dans `main`.

```text
         main
          │
          │
┌─────────┴─────────┐
│                   │
feature/ma-feature  fix/mon-correctif
│                   │
├── Développement
├── Commits
└── Push
        │
        ▼
Pull Request vers main
        │
        ▼
GitHub Actions (CI)
├── Simulation du merge avec main
├── Compilation du backend
├── Tests backend + couverture JaCoCo
├── Tests frontend + couverture
├── Build du frontend
├── Build des images Docker
└── Validation avec Docker Compose
        │
        ▼
✅ Tous les contrôles passent
        │
        ▼
Merge dans main
        │
        ▼
GitHub Actions (CD)
├── Build des images Docker
├── Publication sur Docker Hub
└── Déploiement avec Docker Compose
```
```

### Convention de nommage des branches

- Nouvelle fonctionnalité

```bash
feature/nom-feature
```

- Correction de bug

```bash
fix/nom-correctif
```

### Règles

- Ne jamais développer directement sur `main`.
- Chaque évolution doit être réalisée dans une branche dédiée.
- Une **Pull Request** est obligatoire avant toute fusion dans `main`.
- La fusion n'est autorisée que si tous les contrôles de la pipeline CI sont validés.
- Chaque fusion dans `main` déclenche automatiquement la pipeline de déploiement (CD).
