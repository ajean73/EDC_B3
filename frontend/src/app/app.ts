import { Component } from '@angular/core';
import { ClientManagementComponent } from './components/client-management/client-management.component';
import { AppointmentManagementComponent } from './components/appointment-management/appointment-management.component';
import { ProfileComponent } from './components/profile/profile.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ClientManagementComponent, AppointmentManagementComponent, ProfileComponent],
  templateUrl: './app.html',
  styleUrls: ['./app.css'],
})
export class App {
}
