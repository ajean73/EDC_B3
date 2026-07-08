export interface Appointment {
  id: string;
  dateRendezVous: string;
  service: string;
  statut: 'PLANIFIE' | 'HONORE' | 'ANNULE';
  clientId: string;
  clientNom: string;
}

export interface CreateAppointmentPayload {
  clientId: string;
  dateRendezVous: string; // Format: YYYY-MM-DDTHH:mm
  service: string;
}
