import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { VehiculoService, Vehiculo } from '../../services/vehiculo.service';

@Component({
  selector: 'app-vehiculos',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  template: `
    <div class="container">
      <div class="page-header">
        <h1>Gestion de Vehiculos</h1>
        <button class="btn btn-primary" (click)="abrirModal()">Nuevo Vehiculo</button>
      </div>

      <div class="card">
        <div class="loading" *ngIf="cargando">
          <div class="spinner"></div>
        </div>

        <table *ngIf="!cargando && vehiculos.length > 0">
          <thead>
            <tr>
              <th>ID</th>
              <th>Patente</th>
              <th>Tipo</th>
              <th>Marca</th>
              <th>Modelo</th>
              <th>Capacidad</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let vehiculo of vehiculos">
              <td>{{ vehiculo.id }}</td>
              <td>{{ vehiculo.patente }}</td>
              <td>{{ vehiculo.tipo }}</td>
              <td>{{ vehiculo.marca || '-' }}</td>
              <td>{{ vehiculo.modelo || '-' }}</td>
              <td>{{ vehiculo.capacidad || '-' }}</td>
              <td>
                <span class="badge" [ngClass]="getBadgeClass(vehiculo.estado)">
                  {{ vehiculo.estado }}
                </span>
              </td>
              <td class="actions">
                <button class="btn btn-secondary" (click)="editar(vehiculo)">Editar</button>
                <button class="btn btn-danger" (click)="confirmarEliminar(vehiculo)">Eliminar</button>
              </td>
            </tr>
          </tbody>
        </table>

        <p *ngIf="!cargando && vehiculos.length === 0" class="empty-message">
          No hay vehiculos registrados
        </p>
      </div>

      <!-- Modal -->
      <div class="modal-overlay" *ngIf="mostrarModal" (click)="cerrarModal()">
        <div class="modal" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h2>{{ editando ? 'Editar' : 'Nuevo' }} Vehiculo</h2>
            <button class="modal-close" (click)="cerrarModal()">&times;</button>
          </div>
          <form [formGroup]="formulario" (ngSubmit)="guardar()">
            <div class="form-group">
              <label>Patente *</label>
              <input type="text" class="form-control" formControlName="patente"
                     [class.invalid]="formulario.get('patente')?.invalid && formulario.get('patente')?.touched">
              <span class="error-message" *ngIf="formulario.get('patente')?.hasError('required') && formulario.get('patente')?.touched">
                La patente es obligatoria
              </span>
              <span class="error-message" *ngIf="formulario.get('patente')?.hasError('pattern') && formulario.get('patente')?.touched">
                Formato de patente invalido
              </span>
            </div>

            <div class="form-group">
              <label>Tipo *</label>
              <select class="form-control" formControlName="tipo">
                <option value="">Seleccione...</option>
                <option value="BUS">Bus</option>
                <option value="MICROBUS">Microbus</option>
                <option value="METRO">Metro</option>
                <option value="TRANVIA">Tranvia</option>
              </select>
              <span class="error-message" *ngIf="formulario.get('tipo')?.hasError('required') && formulario.get('tipo')?.touched">
                El tipo es obligatorio
              </span>
            </div>

            <div class="form-group">
              <label>Marca</label>
              <input type="text" class="form-control" formControlName="marca">
            </div>

            <div class="form-group">
              <label>Modelo</label>
              <input type="text" class="form-control" formControlName="modelo">
            </div>

            <div class="form-group">
              <label>Capacidad</label>
              <input type="number" class="form-control" formControlName="capacidad" min="1">
              <span class="error-message" *ngIf="formulario.get('capacidad')?.hasError('min') && formulario.get('capacidad')?.touched">
                La capacidad debe ser mayor a 0
              </span>
            </div>

            <div class="form-group">
              <label>Estado *</label>
              <select class="form-control" formControlName="estado">
                <option value="ACTIVO">Activo</option>
                <option value="INACTIVO">Inactivo</option>
                <option value="MANTENCION">En Mantencion</option>
                <option value="FUERA_DE_SERVICIO">Fuera de Servicio</option>
              </select>
            </div>

            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" (click)="cerrarModal()">Cancelar</button>
              <button type="submit" class="btn btn-primary" [disabled]="formulario.invalid">Guardar</button>
            </div>
          </form>
        </div>
      </div>

      <!-- Modal Confirmar Eliminar -->
      <div class="modal-overlay" *ngIf="mostrarConfirmar" (click)="mostrarConfirmar = false">
        <div class="modal" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h2>Confirmar Eliminacion</h2>
            <button class="modal-close" (click)="mostrarConfirmar = false">&times;</button>
          </div>
          <p>Esta seguro de eliminar el vehiculo {{ vehiculoEliminar?.patente }}?</p>
          <div class="modal-footer">
            <button class="btn btn-secondary" (click)="mostrarConfirmar = false">Cancelar</button>
            <button class="btn btn-danger" (click)="eliminar()">Eliminar</button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .empty-message {
      text-align: center;
      color: #666;
      padding: 40px;
    }
  `]
})
export class VehiculosComponent implements OnInit {
  private vehiculoService = inject(VehiculoService);
  private fb = inject(FormBuilder);

  vehiculos: Vehiculo[] = [];
  cargando = false;
  mostrarModal = false;
  mostrarConfirmar = false;
  editando = false;
  vehiculoActual: Vehiculo | null = null;
  vehiculoEliminar: Vehiculo | null = null;

  formulario: FormGroup = this.fb.group({
    patente: ['', [Validators.required, Validators.pattern(/^[A-Z]{2,4}[0-9]{2,4}$/)]],
    tipo: ['', Validators.required],
    marca: [''],
    modelo: [''],
    capacidad: [null, Validators.min(1)],
    estado: ['ACTIVO', Validators.required]
  });

  ngOnInit() {
    this.cargarVehiculos();
  }

  cargarVehiculos() {
    console.log('=== INICIANDO CARGA DE VEHICULOS ===');
    this.cargando = true;
    this.vehiculoService.obtenerTodos().subscribe({
      next: (data) => {
        console.log('=== VEHICULOS RECIBIDOS ===', data);
        this.vehiculos = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('=== ERROR CARGANDO VEHICULOS ===', err);
        this.cargando = false;
      }
    });
  }

  abrirModal() {
    this.editando = false;
    this.vehiculoActual = null;
    this.formulario.reset({ estado: 'ACTIVO' });
    this.mostrarModal = true;
  }

  editar(vehiculo: Vehiculo) {
    this.editando = true;
    this.vehiculoActual = vehiculo;
    this.formulario.patchValue(vehiculo);
    this.mostrarModal = true;
  }

  cerrarModal() {
    this.mostrarModal = false;
    this.formulario.reset();
  }

  guardar() {
    if (this.formulario.invalid) return;

    const vehiculo: Vehiculo = this.formulario.value;

    if (this.editando && this.vehiculoActual?.id) {
      this.vehiculoService.actualizar(this.vehiculoActual.id, vehiculo).subscribe({
        next: () => {
          this.cargarVehiculos();
          this.cerrarModal();
        },
        error: (err) => console.error('Error actualizando', err)
      });
    } else {
      this.vehiculoService.crear(vehiculo).subscribe({
        next: () => {
          this.cargarVehiculos();
          this.cerrarModal();
        },
        error: (err) => console.error('Error creando', err)
      });
    }
  }

  confirmarEliminar(vehiculo: Vehiculo) {
    this.vehiculoEliminar = vehiculo;
    this.mostrarConfirmar = true;
  }

  eliminar() {
    if (this.vehiculoEliminar?.id) {
      this.vehiculoService.eliminar(this.vehiculoEliminar.id).subscribe({
        next: () => {
          this.cargarVehiculos();
          this.mostrarConfirmar = false;
        },
        error: (err) => console.error('Error eliminando', err)
      });
    }
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
