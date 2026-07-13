import fs from 'node:fs';
import path from 'node:path';

const candidates = [
  path.resolve('coverage/frontend/coverage-summary.json'),
  path.resolve('coverage/coverage-summary.json')
];

const summaryPath = candidates.find((p) => fs.existsSync(p));
if (!summaryPath) {
  console.error('coverage-summary.json introuvable dans les emplacements attendus.');
  process.exit(1);
}

const summary = JSON.parse(fs.readFileSync(summaryPath, 'utf-8'));
const total = summary.total;

const instructionPct = total.statements?.pct ?? 0;
const branchPct = total.branches?.pct ?? 0;

console.log(`Couverture des instructions: ${instructionPct}%`);
console.log(`Couverture des branches: ${branchPct}%`);

if (instructionPct < 60 || branchPct < 60) {
  console.error('Seuils de couverture non atteints (>= 60% pour les instructions/statements et les branches).');
  process.exit(1);
}

console.log('Seuils de couverture atteints.');
