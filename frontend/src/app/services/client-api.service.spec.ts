import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { ClientApiService } from './client-api.service';

describe('ClientApiService', () => {
  let service: ClientApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ClientApiService, provideHttpClient(), provideHttpClientTesting()]
    });

    service = TestBed.inject(ClientApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('doit recuperer les clients', () => {
    service.getClients().subscribe((clients) => {
      expect(clients.length).toBe(1);
      expect(clients[0].nom).toBe('Alice');
    });

    const req = httpMock.expectOne('http://localhost:8080/api/clients');
    expect(req.request.method).toBe('GET');
    req.flush([{ id: 'c1', nom: 'Alice', email: 'a@a.com', telephone: '000', pointsFidelite: 10 }]);
  });

  it('doit recuperer les transactions par id client', () => {
    service.getClientTransactions('c1').subscribe((txs) => {
      expect(txs.length).toBe(1);
      expect(txs[0].points).toBe(10);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/clients/c1/transactions');
    expect(req.request.method).toBe('GET');
    req.flush([{ id: 't1', points: 10, description: 'Rendez-vous honoré', createdAt: '2026-07-09T10:00:00' }]);
  });
});
