import { Component } from '@angular/core';
import { ClientManagementComponent } from './components/client-management/client-management.component';
import { AppointmentManagementComponent } from './components/appointment-management/appointment-management.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ClientManagementComponent, AppointmentManagementComponent],
  templateUrl: './app.html',
  styleUrls: ['./app.css'],
})
export class App {
}
