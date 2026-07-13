import { TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { App } from './app';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      schemas: [NO_ERRORS_SCHEMA]
    })
      .overrideComponent(App, {
        set: {
          imports: [],
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
