export interface Client {
  id: string;
  nom: string;
  email: string;
  telephone: string;
  pointsFidelite: number;
}

export interface ClientPayload {
  nom: string;
  email: string;
  telephone: string;
}
