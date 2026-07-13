import fs from 'node:fs';
import path from 'node:path';

const candidates = [
  path.resolve('coverage/frontend/coverage-summary.json'),
  path.resolve('coverage/coverage-summary.json')
];

const summaryPath = candidates.find((p) => fs.existsSync(p));

function toPct(covered, total) {
  if (!total) {
    return 100;
  }
  return Number(((covered / total) * 100).toFixed(2));
}

function computeFromCoverageFinal(coverageFinal) {
  let statementsCovered = 0;
  let statementsTotal = 0;
  let branchesCovered = 0;
  let branchesTotal = 0;

  for (const fileData of Object.values(coverageFinal)) {
    const statements = fileData?.s ?? {};
    const branches = fileData?.b ?? {};

    for (const count of Object.values(statements)) {
      statementsTotal += 1;
      if ((count ?? 0) > 0) {
        statementsCovered += 1;
      }
    }

    for (const counts of Object.values(branches)) {
      for (const count of counts ?? []) {
        branchesTotal += 1;
        if ((count ?? 0) > 0) {
          branchesCovered += 1;
        }
      }
    }
  }

  return {
    statements: { pct: toPct(statementsCovered, statementsTotal) },
    branches: { pct: toPct(branchesCovered, branchesTotal) }
  };
}

let total;

if (summaryPath) {
  const summary = JSON.parse(fs.readFileSync(summaryPath, 'utf-8'));
  total = summary.total;
} else {
  const finalCandidates = [
    path.resolve('coverage/frontend/coverage-final.json'),
    path.resolve('coverage/coverage-final.json')
  ];
  const finalPath = finalCandidates.find((p) => fs.existsSync(p));
  if (!finalPath) {
    console.error('Aucun rapport de couverture trouve (coverage-summary.json ou coverage-final.json).');
    process.exit(1);
  }

  const coverageFinal = JSON.parse(fs.readFileSync(finalPath, 'utf-8'));
  total = computeFromCoverageFinal(coverageFinal);
}

const instructionPct = total.statements?.pct ?? 0;
const branchPct = total.branches?.pct ?? 0;

console.log(`Couverture des instructions: ${instructionPct}%`);
console.log(`Couverture des branches: ${branchPct}%`);

if (instructionPct < 60 || branchPct < 60) {
  console.error('Seuils de couverture non atteints (>= 60% pour les instructions/statements et les branches).');
  process.exit(1);
}

console.log('Seuils de couverture atteints.');
