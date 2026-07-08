import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Appointment, CreateAppointmentPayload } from '../models/appointment.model';

@Injectable({
  providedIn: 'root'
})
export class AppointmentApiService {
  private readonly baseUrl = 'http://localhost:8080/api/rendezvous';

  constructor(private readonly http: HttpClient) {}

  getAppointments(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(this.baseUrl);
  }

  createAppointment(payload: CreateAppointmentPayload): Observable<Appointment> {
    return this.http.post<Appointment>(this.baseUrl, payload);
  }

  updateAppointmentStatus(appointmentId: string, status: 'HONORE' | 'ANNULE'): Observable<Appointment> {
    return this.http.patch<Appointment>(`${this.baseUrl}/${appointmentId}/status`, { statut: status });
  }
}