import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Appointment, CreateAppointmentPayload } from '../models/appointment.model';

export interface AppointmentFilters {
  date?: string;
  statut?: 'PLANIFIE' | 'HONORE' | 'ANNULE';
  clientId?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AppointmentApiService {
  private readonly baseUrl = 'http://localhost:8080/api/rendezvous';

  constructor(private readonly http: HttpClient) {}

  getAppointments(filters?: AppointmentFilters): Observable<Appointment[]> {
    let params = new HttpParams();

    if (filters?.date) {
      params = params.set('date', filters.date);
    }

    if (filters?.statut) {
      params = params.set('statut', filters.statut);
    }

    if (filters?.clientId) {
      params = params.set('clientId', filters.clientId);
    }

    return this.http.get<Appointment[]>(this.baseUrl, { params });
  }

  createAppointment(payload: CreateAppointmentPayload): Observable<Appointment> {
    return this.http.post<Appointment>(this.baseUrl, payload);
  }

  updateAppointmentStatus(appointmentId: string, status: 'HONORE' | 'ANNULE'): Observable<Appointment> {
    return this.http.patch<Appointment>(`${this.baseUrl}/${appointmentId}/status`, { statut: status });
  }
}