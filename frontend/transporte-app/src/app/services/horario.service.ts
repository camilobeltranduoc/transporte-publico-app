import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Horario {
  id?: number;
  rutaId: number;
  codigoRuta?: string;
  diaSemana: string;
  horaInicio: string;
  horaFin: string;
  frecuenciaMinutos?: number;
  activo?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class HorarioService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/horarios`;

  obtenerTodos(): Observable<Horario[]> {
    return this.http.get<Horario[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Horario> {
    return this.http.get<Horario>(`${this.apiUrl}/${id}`);
  }

  obtenerPorRuta(rutaId: number): Observable<Horario[]> {
    return this.http.get<Horario[]>(`${this.apiUrl}/ruta/${rutaId}`);
  }

  crear(horario: Horario): Observable<Horario> {
    return this.http.post<Horario>(this.apiUrl, horario);
  }

  actualizar(id: number, horario: Horario): Observable<Horario> {
    return this.http.put<Horario>(`${this.apiUrl}/${id}`, horario);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
