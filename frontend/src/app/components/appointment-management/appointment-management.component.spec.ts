import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { vi } from 'vitest';
import { AppointmentManagementComponent } from './appointment-management.component';
import { AppointmentApiService } from '../../services/appointment-api.service';
import { ClientApiService } from '../../services/client-api.service';

describe('AppointmentManagementComponent', () => {
  let fixture: ComponentFixture<AppointmentManagementComponent>;
  let component: AppointmentManagementComponent;

  const appointmentApiServiceMock = {
    getAppointments: vi.fn(),
    createAppointment: vi.fn(),
    updateAppointmentStatus: vi.fn()
  };

  const clientApiServiceMock = {
    getClients: vi.fn()
  };

  beforeEach(async () => {
    clientApiServiceMock.getClients.mockReturnValue(of([{ id: 'c1', nom: 'Alice', email: 'a@a.com', telephone: '0', pointsFidelite: 0 }]));
    appointmentApiServiceMock.getAppointments.mockReturnValue(of([]));
    appointmentApiServiceMock.createAppointment.mockReturnValue(of({}));
    appointmentApiServiceMock.updateAppointmentStatus.mockReturnValue(of({}));

    await TestBed.configureTestingModule({
      imports: [AppointmentManagementComponent],
      providers: [
        { provide: AppointmentApiService, useValue: appointmentApiServiceMock },
        { provide: ClientApiService, useValue: clientApiServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppointmentManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('doit charger les donnees initiales', () => {
    expect(clientApiServiceMock.getClients).toHaveBeenCalled();
    expect(appointmentApiServiceMock.getAppointments).toHaveBeenCalled();
    expect(component.loading).toBeFalsy();
  });

  it('doit gerer erreur de chargement des rendez-vous', () => {
    appointmentApiServiceMock.getAppointments.mockReturnValue(throwError(() => new Error('erreur')));

    component.loadAppointments();

    expect(component.errorMessage).toContain('Impossible de charger les rendez-vous');
    expect(component.loading).toBeFalsy();
  });

  it('doit construire des filtres et appeler l API', () => {
    component.filterModel = { date: '2026-07-13', statut: 'PLANIFIE', clientId: 'c1' };

    component.loadAppointments();

    expect(appointmentApiServiceMock.getAppointments).toHaveBeenCalledWith({
      date: '2026-07-13',
      statut: 'PLANIFIE',
      clientId: 'c1'
    });
  });

  it('doit refuser la creation si formulaire incomplet', () => {
    component.formModel = { clientId: '', date: '', time: '', service: '' };

    component.submitForm();

    expect(component.errorMessage).toContain('Tous les champs sont obligatoires');
    expect(appointmentApiServiceMock.createAppointment).not.toHaveBeenCalled();
  });

  it('doit creer un rendez-vous avec un formulaire valide', () => {
    component.formModel = { clientId: 'c1', date: '2026-07-13', time: '10:30', service: 'Conseil' };

    component.submitForm();

    expect(appointmentApiServiceMock.createAppointment).toHaveBeenCalledWith({
      clientId: 'c1',
      dateRendezVous: '2026-07-13T10:30',
      service: 'Conseil'
    });
  });

  it('doit gerer erreur de mise a jour de statut', () => {
    appointmentApiServiceMock.updateAppointmentStatus.mockReturnValue(throwError(() => new Error('echec')));

    component.updateStatus('rdv1', 'HONORE');

    expect(component.errorMessage).toContain('Erreur lors de la mise à jour du statut');
  });

  it('doit retourner Client inconnu si client absent', () => {
    const name = component.getClientName('absent');
    expect(name).toBe('Client inconnu');
  });
});
