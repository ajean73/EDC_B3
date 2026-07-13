import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { AppointmentApiService } from './appointment-api.service';

describe('AppointmentApiService', () => {
  let service: AppointmentApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AppointmentApiService, provideHttpClient(), provideHttpClientTesting()]
    });

    service = TestBed.inject(AppointmentApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('doit recuperer les rendez-vous sans filtre', () => {
    service.getAppointments().subscribe((appointments) => {
      expect(appointments).toEqual([]);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/rendezvous');
    expect(req.request.method).toBe('GET');
    expect(req.request.params.keys().length).toBe(0);
    req.flush([]);
  });

  it('doit appliquer les filtres date, statut et clientId', () => {
    service
      .getAppointments({ date: '2026-07-13', statut: 'PLANIFIE', clientId: 'c1' })
      .subscribe((appointments) => {
        expect(appointments.length).toBe(1);
      });

    const req = httpMock.expectOne((request) => request.url === 'http://localhost:8080/api/rendezvous');
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('date')).toBe('2026-07-13');
    expect(req.request.params.get('statut')).toBe('PLANIFIE');
    expect(req.request.params.get('clientId')).toBe('c1');
    req.flush([{ id: 'r1' }]);
  });

  it('doit creer un rendez-vous', () => {
    const payload = {
      clientId: 'c1',
      dateRendezVous: '2026-07-13T10:30',
      service: 'Conseil'
    };

    service.createAppointment(payload).subscribe((appointment) => {
      expect(appointment.id).toBe('r1');
    });

    const req = httpMock.expectOne('http://localhost:8080/api/rendezvous');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush({ id: 'r1' });
  });

  it('doit mettre a jour le statut d un rendez-vous', () => {
    service.updateAppointmentStatus('r1', 'HONORE').subscribe((appointment) => {
      expect(appointment.id).toBe('r1');
    });

    const req = httpMock.expectOne('http://localhost:8080/api/rendezvous/r1/status');
    expect(req.request.method).toBe('PATCH');
    expect(req.request.body).toEqual({ statut: 'HONORE' });
    req.flush({ id: 'r1' });
  });
});
