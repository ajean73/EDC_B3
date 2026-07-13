## Organisation & Workflow Git

Le projet suit un workflow base sur **GitHub Flow**:
- Branches de travail: `feature/*`, `fix/*`
- Branche protegee: `main`
- Integration via Pull Request uniquement

### Regle de merge demandee

Avant merge vers `main`, la CI simule un merge **sans commit** avec `main`:
- `git merge --no-commit --no-ff origin/main`
- Si le merge temporaire, le build et les tests passent, la PR peut etre mergee.

Cette logique est automatisee dans GitHub Actions.

## CI/CD

Fichier pipeline:
- `.github/workflows/ci-cd.yml`

### Sur Pull Request vers main

La pipeline execute:
1. Checkout du code
2. Merge temporaire sans commit avec `main`
3. Build backend (`./gradlew clean test bootJar`)
4. Build frontend (`npm ci && npm run build`)
5. Build Docker (`docker compose build`)
6. Relance applicative de validation:
    - `docker compose up --build -d`
7. Nettoyage:
    - `docker compose down -v`

### Sur push sur main

La pipeline execute une relance applicative:
- `docker compose up --build -d`

## Notes

- La partie push d'images DockerHub est volontairement reportee.
- Le projet reste en API ouverte (pas de JWT, pas de securisation avancee), conforme a la contrainte.
