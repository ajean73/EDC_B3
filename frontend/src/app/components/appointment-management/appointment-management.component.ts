import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Appointment, CreateAppointmentPayload } from '../../models/appointment.model';
import { AppointmentApiService, AppointmentFilters } from '../../services/appointment-api.service';
import { Client } from '../../models/client.model';
import { ClientApiService } from '../../services/client-api.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-appointment-management',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './appointment-management.component.html',
  styleUrls: ['./appointment-management.component.css']
})
export class AppointmentManagementComponent implements OnInit {
  appointments: Appointment[] = [];
  clients: Client[] = [];
  readonly statusOptions: Array<'PLANIFIE' | 'HONORE' | 'ANNULE'> = ['PLANIFIE', 'HONORE', 'ANNULE'];
  loading = false;
  errorMessage = '';
  
  formModel: {
    clientId: string;
    date: string;
    time: string;
    service: string;
  } = {
    clientId: '',
    date: '',
    time: '',
    service: ''
  };

  filterModel: {
    date: string;
    statut: '' | 'PLANIFIE' | 'HONORE' | 'ANNULE';
    clientId: string;
  } = {
    date: '',
    statut: '',
    clientId: ''
  };

  constructor(
    private readonly appointmentApiService: AppointmentApiService,
    private readonly clientApiService: ClientApiService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadInitialData();
  }

  getClientName(clientId: string): string {
    const client = this.clients.find(c => c.id === clientId);
    return client ? `${client.nom}` : 'Client inconnu';
  }

  loadInitialData(): void {
    this.loading = true;
    this.errorMessage = '';
    // We can load clients and appointments in parallel
    this.clientApiService.getClients().subscribe({
      next: (clients) => {
        this.clients = clients;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les clients.';
      }
    });

    this.loadAppointments();
  }

  loadAppointments(): void {
    this.appointmentApiService.getAppointments(this.buildFilters()).subscribe({
      next: (appointments) => {
        this.appointments = appointments;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les rendez-vous.';
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    this.loadAppointments();
  }

  resetFilters(): void {
    this.filterModel = {
      date: '',
      statut: '',
      clientId: ''
    };
    this.loadAppointments();
  }

  submitForm(): void {
    this.errorMessage = '';
    if (!this.formModel.clientId || !this.formModel.date || !this.formModel.time || !this.formModel.service) {
      this.errorMessage = 'Tous les champs sont obligatoires.';
      return;
    }

    const payload: CreateAppointmentPayload = {
      clientId: this.formModel.clientId,
      dateRendezVous: `${this.formModel.date}T${this.formModel.time}`,
      service: this.formModel.service
    };

    this.appointmentApiService.createAppointment(payload).subscribe({
      next: () => {
        this.resetForm();
        this.loadAppointments();
      },
      error: () => {
        this.errorMessage = 'Une erreur est survenue lors de la création du rendez-vous.';
      }
    });
  }

  updateStatus(appointmentId: string, status: 'HONORE' | 'ANNULE'): void {
    this.appointmentApiService.updateAppointmentStatus(appointmentId, status).subscribe({
      next: () => {
        this.loadAppointments();
      },
      error: () => {
        this.errorMessage = 'Erreur lors de la mise à jour du statut.';
      }
    });
  }

  resetForm(): void {
    this.formModel = {
      clientId: '',
      date: '',
      time: '',
      service: ''
    };
  }

  private buildFilters(): AppointmentFilters {
    const filters: AppointmentFilters = {};

    if (this.filterModel.date) {
      filters.date = this.filterModel.date;
    }

    if (this.filterModel.statut) {
      filters.statut = this.filterModel.statut;
    }

    if (this.filterModel.clientId) {
      filters.clientId = this.filterModel.clientId;
    }

    return filters;
  }
}