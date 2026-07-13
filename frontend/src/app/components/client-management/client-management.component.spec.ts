import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { vi } from 'vitest';
import { ClientManagementComponent } from './client-management.component';
import { ClientApiService } from '../../services/client-api.service';

describe('ClientManagementComponent', () => {
  let fixture: ComponentFixture<ClientManagementComponent>;
  let component: ClientManagementComponent;

  const clientApiServiceMock = {
    getClients: vi.fn(),
    createClient: vi.fn(),
    updateClient: vi.fn()
  };

  beforeEach(async () => {
    clientApiServiceMock.getClients.mockReturnValue(of([]));
    clientApiServiceMock.createClient.mockReturnValue(of({
      id: '1',
      nom: 'Alice',
      email: 'a@a.com',
      telephone: '010203',
      pointsFidelite: 0
    }));
    clientApiServiceMock.updateClient.mockReturnValue(of({
      id: '1',
      nom: 'Alice',
      email: 'a@a.com',
      telephone: '010203',
      pointsFidelite: 0
    }));

    await TestBed.configureTestingModule({
      imports: [ClientManagementComponent],
      providers: [{ provide: ClientApiService, useValue: clientApiServiceMock }]
    }).compileComponents();

    fixture = TestBed.createComponent(ClientManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('doit charger les clients au demarrage', () => {
    expect(clientApiServiceMock.getClients).toHaveBeenCalled();
    expect(component.loading).toBeFalsy();
  });

  it('doit gerer une erreur de chargement des clients', () => {
    clientApiServiceMock.getClients.mockReturnValue(throwError(() => new Error('erreur')));

    component.loadClients();

    expect(component.errorMessage).toBe('Impossible de charger les clients.');
    expect(component.loading).toBeFalsy();
  });

  it('doit refuser la sauvegarde si nom/email manquants', () => {
    component.formModel.nom = '';
    component.formModel.email = '';

    component.submitForm();

    expect(component.errorMessage).toContain('obligatoires');
    expect(clientApiServiceMock.createClient).not.toHaveBeenCalled();
  });

  it('doit creer un client si aucun client selectionne', () => {
    component.formModel = { nom: 'Alice', email: 'a@a.com', telephone: '010203' };

    component.submitForm();

    expect(clientApiServiceMock.createClient).toHaveBeenCalled();
    expect(component.successMessage).toContain('cree');
  });

  it('doit mettre a jour un client si un client est selectionne', () => {
    component.selectedClientId = '1';
    component.formModel = { nom: 'Alice', email: 'a@a.com', telephone: '010203' };

    component.submitForm();

    expect(clientApiServiceMock.updateClient).toHaveBeenCalledWith('1', {
      nom: 'Alice',
      email: 'a@a.com',
      telephone: '010203'
    });
    expect(component.successMessage).toContain('mis a jour');
  });

  it('doit gerer une erreur de sauvegarde', () => {
    clientApiServiceMock.createClient.mockReturnValue(throwError(() => new Error('echec')));
    component.formModel = { nom: 'Alice', email: 'a@a.com', telephone: '010203' };

    component.submitForm();

    expect(component.errorMessage).toContain('Une erreur est survenue');
  });

  it('doit preparer le formulaire en edition puis le reinitialiser', () => {
    component.editClient({
      id: '1',
      nom: 'Alice',
      email: 'a@a.com',
      telephone: '010203',
      pointsFidelite: 5
    });

    expect(component.selectedClientId).toBe('1');
    expect(component.formModel.nom).toBe('Alice');

    component.resetForm();

    expect(component.selectedClientId).toBeNull();
    expect(component.formModel.nom).toBe('');
  });
});
