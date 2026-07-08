import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Client } from '../../models/client.model';
import { ClientApiService, LoyaltyTransaction } from '../../services/client-api.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card">
      <h3>Profil client (public)</h3>
      <div class="form-group">
        <label for="clientSelect">Sélectionner un client</label>
        <select id="clientSelect" [(ngModel)]="selectedClientId" (change)="onClientChange()">
          <option value="">-- Choisir --</option>
          <option *ngFor="let c of clients" [value]="c.id">{{ c.nom }}</option>
        </select>
      </div>

      <div *ngIf="profile">
        <p><strong>Nom:</strong> {{ profile.nom }}</p>
        <p><strong>Email:</strong> {{ profile.email }}</p>
        <p><strong>Tél:</strong> {{ profile.telephone }}</p>
        <p><strong>Points fidélité:</strong> {{ profile.pointsFidelite }}</p>
      </div>

      <h4 *ngIf="transactions.length>0">Historique des transactions</h4>
      <ul>
        <li *ngFor="let t of transactions">
          {{ t.createdAt | date:'dd/MM/yyyy HH:mm' }}: {{ t.points }} pts — {{ t.description }}
        </li>
      </ul>
    </div>
  `
})
export class ProfileComponent implements OnInit {
  profile: Client | null = null;
  transactions: LoyaltyTransaction[] = [];
  clients: Client[] = [];
  selectedClientId = '';

  constructor(private readonly clientApiService: ClientApiService) {}

  ngOnInit(): void {
    this.clientApiService.getClients().subscribe({ next: (clients) => {
      this.clients = clients || [];
      if (this.clients.length === 1) {
        this.selectedClientId = this.clients[0].id;
        this.loadForClient(this.selectedClientId);
      }
    }});
  }

  onClientChange(): void {
    if (this.selectedClientId) this.loadForClient(this.selectedClientId);
    else {
      this.profile = null;
      this.transactions = [];
    }
  }

  private loadForClient(clientId: string): void {
    this.clientApiService.getClientById(clientId).subscribe({ next: (profile) => (this.profile = profile) });
    this.clientApiService.getClientTransactions(clientId).subscribe({ next: (transactions) => (this.transactions = transactions) });
  }
}
