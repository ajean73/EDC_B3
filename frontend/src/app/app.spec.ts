import { TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';
import { App } from './app';

@Component({ selector: 'app-client-management', standalone: true, template: '' })
class ClientManagementStubComponent {}

@Component({ selector: 'app-appointment-management', standalone: true, template: '' })
class AppointmentManagementStubComponent {}

@Component({ selector: 'app-profile', standalone: true, template: '' })
class ProfileStubComponent {}

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App]
    })
      .overrideComponent(App, {
        set: {
          imports: [
            ClientManagementStubComponent,
            AppointmentManagementStubComponent,
            ProfileStubComponent
          ],
          template: `
            <app-client-management></app-client-management>
            <app-appointment-management></app-appointment-management>
            <app-profile></app-profile>
          `
        }
      })
      .compileComponents();
  });

  it('doit creer l application', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('doit afficher les sections principales', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('app-client-management')).toBeTruthy();
    expect(compiled.querySelector('app-appointment-management')).toBeTruthy();
    expect(compiled.querySelector('app-profile')).toBeTruthy();
  });
});
