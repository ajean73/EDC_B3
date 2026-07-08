import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Client, ClientPayload } from '../../models/client.model';
import { ClientApiService } from '../../services/client-api.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-client-management',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './client-management.component.html',
  styleUrls: ['./client-management.component.css'],
})
export class ClientManagementComponent implements OnInit {
  clients: Client[] = [];
  loading = false;
  errorMessage = '';
  successMessage = '';
  selectedClientId: string | null = null;
  newClient: { nom: string; prenom: string; telephone: string } = { nom: '', prenom: '', telephone: '' };

  formModel: ClientPayload = {
    nom: '',
    email: '',
    telephone: ''
  };

  constructor(
    private readonly clientApiService: ClientApiService,
    private readonly cdr: ChangeDetectorRef
    ) {}

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    this.loading = true;
    this.errorMessage = '';
    this.clientApiService.getClients().subscribe({
      next: (clients) => {
        this.clients = clients;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les clients.';
        this.loading = false;
      }
    });
  }

  submitForm(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.formModel.nom || !this.formModel.email) {
      this.errorMessage = 'Le nom et l\'email sont obligatoires.';
      return;
    }

    const request$ = this.selectedClientId
      ? this.clientApiService.updateClient(this.selectedClientId, this.formModel)
      : this.clientApiService.createClient(this.formModel);

    request$.subscribe({
      next: (client) => {
        this.successMessage = this.selectedClientId
          ? 'Client mis a jour avec succes.'
          : 'Client cree avec succes.';
        this.resetForm();
        this.loadClients();
      },
      error: () => {
        this.errorMessage = 'Une erreur est survenue lors de la sauvegarde.';
      }
    });
  }

  editClient(client: Client): void {
    this.selectedClientId = client.id;
    this.formModel = {
      nom: client.nom,
      email: client.email,
      telephone: client.telephone
    };
  }

  resetForm(): void {
    this.selectedClientId = null;
    this.formModel = {
      nom: '',
      email: '',
      telephone: ''
    };
  }
}
