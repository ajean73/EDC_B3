import { Component } from '@angular/core';
import { ClientManagementComponent } from './components/client-management/client-management.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ClientManagementComponent],
  templateUrl: './app.html',
  styleUrls: ['./app.css'],
})
export class App {
}
