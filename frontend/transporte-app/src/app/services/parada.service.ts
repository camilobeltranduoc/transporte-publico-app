import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Parada {
  id?: number;
  nombre: string;
  direccion?: string;
  latitud: number;
  longitud: number;
  orden?: number;
  rutaId: number;
  codigoRuta?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ParadaService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/paradas`;

  obtenerTodas(): Observable<Parada[]> {
    return this.http.get<Parada[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Parada> {
    return this.http.get<Parada>(`${this.apiUrl}/${id}`);
  }

  obtenerPorRuta(rutaId: number): Observable<Parada[]> {
    return this.http.get<Parada[]>(`${this.apiUrl}/ruta/${rutaId}`);
  }

  crear(parada: Parada): Observable<Parada> {
    return this.http.post<Parada>(this.apiUrl, parada);
  }

  actualizar(id: number, parada: Parada): Observable<Parada> {
    return this.http.put<Parada>(`${this.apiUrl}/${id}`, parada);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
