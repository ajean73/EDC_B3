import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Client, ClientPayload } from '../models/client.model';

export interface LoyaltyTransaction {
  id: string;
  points: number;
  description: string;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class ClientApiService {
  private readonly baseUrl = 'http://localhost:8080/api/clients';

  constructor(private readonly http: HttpClient) {}

  getClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.baseUrl);
  }

  createClient(payload: ClientPayload): Observable<Client> {
    return this.http.post<Client>(this.baseUrl, payload);
  }

  updateClient(clientId: string, payload: ClientPayload): Observable<Client> {
    return this.http.put<Client>(`${this.baseUrl}/${clientId}`, payload);
  }

  getClientById(clientId: string): Observable<Client> {
    return this.http.get<Client>(`${this.baseUrl}/${clientId}`);
  }

  getClientTransactions(clientId: string): Observable<LoyaltyTransaction[]> {
    return this.http.get<LoyaltyTransaction[]>(`${this.baseUrl}/${clientId}/transactions`);
  }
}
