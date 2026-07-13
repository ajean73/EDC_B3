import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { vi } from 'vitest';

import { ProfileComponent } from './profile.component';
import { ClientApiService } from '../../services/client-api.service';

describe('ProfileComponent', () => {
  let fixture: ComponentFixture<ProfileComponent>;
  let component: ProfileComponent;

  const clientApiServiceMock = {
    getClients: vi.fn(),
    getClientById: vi.fn(),
    getClientTransactions: vi.fn()
  };

  beforeEach(async () => {
    clientApiServiceMock.getClients.mockReturnValue(of([]));
    clientApiServiceMock.getClientById.mockReturnValue(
      of({ id: 'c1', nom: 'Alice', email: 'a@a.com', telephone: '010203', pointsFidelite: 12 })
    );
    clientApiServiceMock.getClientTransactions.mockReturnValue(
      of([{ id: 't1', points: 10, description: 'Rendez-vous honoré', createdAt: '2026-07-13T10:00:00' }])
    );

    await TestBed.configureTestingModule({
      imports: [ProfileComponent],
      providers: [{ provide: ClientApiService, useValue: clientApiServiceMock }]
    }).compileComponents();

    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
  });

  it('doit charger les clients au demarrage', () => {
    fixture.detectChanges();

    expect(clientApiServiceMock.getClients).toHaveBeenCalled();
    expect(component.clients).toEqual([]);
  });

  it('doit auto charger le profil si un seul client est disponible', () => {
    clientApiServiceMock.getClients.mockReturnValue(
      of([{ id: 'c1', nom: 'Alice', email: 'a@a.com', telephone: '010203', pointsFidelite: 12 }])
    );

    fixture.detectChanges();

    expect(component.selectedClientId).toBe('c1');
    expect(clientApiServiceMock.getClientById).toHaveBeenCalledWith('c1');
    expect(clientApiServiceMock.getClientTransactions).toHaveBeenCalledWith('c1');
  });

  it('doit vider profil et transactions si aucun client selectionne', () => {
    component.profile = { id: 'c1', nom: 'Alice', email: 'a@a.com', telephone: '010203', pointsFidelite: 12 };
    component.transactions = [{ id: 't1', points: 10, description: 'ok', createdAt: '2026-07-13T10:00:00' }];
    component.selectedClientId = '';

    component.onClientChange();

    expect(component.profile).toBeNull();
    expect(component.transactions).toEqual([]);
  });

  it('doit charger profil et transactions a la selection d un client', () => {
    component.selectedClientId = 'c1';

    component.onClientChange();

    expect(clientApiServiceMock.getClientById).toHaveBeenCalledWith('c1');
    expect(clientApiServiceMock.getClientTransactions).toHaveBeenCalledWith('c1');
    expect(component.profile?.nom).toBe('Alice');
    expect(component.transactions.length).toBe(1);
  });
});
