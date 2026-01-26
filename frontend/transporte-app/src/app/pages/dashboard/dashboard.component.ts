import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VehiculoService, Vehiculo } from '../../services/vehiculo.service';
import { RutaService, Ruta } from '../../services/ruta.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container">
      <div class="page-header">
        <h1>Dashboard</h1>
      </div>

      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon vehiculos">
            <span class="material-icons">directions_bus</span>
          </div>
          <div class="stat-info">
            <h3>{{ vehiculos.length }}</h3>
            <p>Vehiculos Totales</p>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon activos">
            <span class="material-icons">check_circle</span>
          </div>
          <div class="stat-info">
            <h3>{{ vehiculosActivos }}</h3>
            <p>Vehiculos Activos</p>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon rutas">
            <span class="material-icons">route</span>
          </div>
          <div class="stat-info">
            <h3>{{ rutas.length }}</h3>
            <p>Rutas Totales</p>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon mantencion">
            <span class="material-icons">build</span>
          </div>
          <div class="stat-info">
            <h3>{{ vehiculosMantencion }}</h3>
            <p>En Mantencion</p>
          </div>
        </div>
      </div>

      <div class="dashboard-grid">
        <div class="card">
          <h2>Ultimos Vehiculos</h2>
          <table *ngIf="vehiculos.length > 0">
            <thead>
              <tr>
                <th>Patente</th>
                <th>Tipo</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let v of vehiculos.slice(0, 5)">
                <td>{{ v.patente }}</td>
                <td>{{ v.tipo }}</td>
                <td>
                  <span class="badge" [ngClass]="getBadgeClass(v.estado)">
                    {{ v.estado }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
          <p *ngIf="vehiculos.length === 0" class="empty-message">No hay vehiculos registrados</p>
        </div>

        <div class="card">
          <h2>Rutas Activas</h2>
          <table *ngIf="rutas.length > 0">
            <thead>
              <tr>
                <th>Codigo</th>
                <th>Nombre</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let r of rutas.slice(0, 5)">
                <td>{{ r.codigo }}</td>
                <td>{{ r.nombre }}</td>
                <td>
                  <span class="badge" [ngClass]="r.activa ? 'badge-success' : 'badge-danger'">
                    {{ r.activa ? 'Activa' : 'Inactiva' }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
          <p *ngIf="rutas.length === 0" class="empty-message">No hay rutas registradas</p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 20px;
      margin-bottom: 30px;
    }
    .stat-card {
      background: white;
      border-radius: 8px;
      padding: 20px;
      display: flex;
      align-items: center;
      gap: 15px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .stat-icon .material-icons {
      font-size: 30px;
      color: white;
    }
    .stat-icon.vehiculos { background: #1976d2; }
    .stat-icon.activos { background: #388e3c; }
    .stat-icon.rutas { background: #7b1fa2; }
    .stat-icon.mantencion { background: #f57c00; }
    .stat-info h3 {
      font-size: 28px;
      margin: 0;
      color: #333;
    }
    .stat-info p {
      margin: 5px 0 0;
      color: #666;
    }
    .dashboard-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
      gap: 20px;
    }
    .empty-message {
      text-align: center;
      color: #666;
      padding: 20px;
    }
  `]
})
export class DashboardComponent implements OnInit {
  private vehiculoService = inject(VehiculoService);
  private rutaService = inject(RutaService);

  vehiculos: Vehiculo[] = [];
  rutas: Ruta[] = [];

  get vehiculosActivos(): number {
    return this.vehiculos.filter(v => v.estado === 'ACTIVO').length;
  }

  get vehiculosMantencion(): number {
    return this.vehiculos.filter(v => v.estado === 'MANTENCION').length;
  }

  ngOnInit() {
    this.cargarDatos();
  }

  cargarDatos() {
    this.vehiculoService.obtenerTodos().subscribe({
      next: (data) => this.vehiculos = data,
      error: (err) => console.error('Error cargando vehiculos', err)
    });

    this.rutaService.obtenerTodas().subscribe({
      next: (data) => this.rutas = data,
      error: (err) => console.error('Error cargando rutas', err)
    });
  }

  getBadgeClass(estado: string): string {
    switch (estado) {
      case 'ACTIVO': return 'badge-success';
      case 'INACTIVO': return 'badge-danger';
      case 'MANTENCION': return 'badge-warning';
      default: return 'badge-info';
    }
  }
}
