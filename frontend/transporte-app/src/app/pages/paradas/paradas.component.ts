import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ParadaService, Parada } from '../../services/parada.service';
import { RutaService, Ruta } from '../../services/ruta.service';

@Component({
  selector: 'app-paradas',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  template: `
    <div class="container">
      <div class="page-header">
        <h1>Gestion de Paradas</h1>
        <button class="btn btn-primary" (click)="abrirModal()">Nueva Parada</button>
      </div>

      <div class="card">
        <div class="loading" *ngIf="cargando">
          <div class="spinner"></div>
        </div>

        <table *ngIf="!cargando && paradas.length > 0">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Direccion</th>
              <th>Ruta</th>
              <th>Orden</th>
              <th>Coordenadas</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let parada of paradas">
              <td>{{ parada.id }}</td>
              <td>{{ parada.nombre }}</td>
              <td>{{ parada.direccion || '-' }}</td>
              <td>{{ parada.codigoRuta }}</td>
              <td>{{ parada.orden || '-' }}</td>
              <td>{{ parada.latitud?.toFixed(4) }}, {{ parada.longitud?.toFixed(4) }}</td>
              <td class="actions">
                <button class="btn btn-secondary" (click)="editar(parada)">Editar</button>
                <button class="btn btn-danger" (click)="confirmarEliminar(parada)">Eliminar</button>
              </td>
            </tr>
          </tbody>
        </table>

        <p *ngIf="!cargando && paradas.length === 0" class="empty-message">
          No hay paradas registradas
        </p>
      </div>

      <!-- Modal -->
      <div class="modal-overlay" *ngIf="mostrarModal" (click)="cerrarModal()">
        <div class="modal" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h2>{{ editando ? 'Editar' : 'Nueva' }} Parada</h2>
            <button class="modal-close" (click)="cerrarModal()">&times;</button>
          </div>
          <form [formGroup]="formulario" (ngSubmit)="guardar()">
            <div class="form-group">
              <label>Nombre *</label>
              <input type="text" class="form-control" formControlName="nombre"
                     [class.invalid]="formulario.get('nombre')?.invalid && formulario.get('nombre')?.touched">
              <span class="error-message" *ngIf="formulario.get('nombre')?.hasError('required') && formulario.get('nombre')?.touched">
                El nombre es obligatorio
              </span>
            </div>

            <div class="form-group">
              <label>Direccion</label>
              <input type="text" class="form-control" formControlName="direccion">
            </div>

            <div class="form-group">
              <label>Ruta *</label>
              <select class="form-control" formControlName="rutaId">
                <option value="">Seleccione...</option>
                <option *ngFor="let ruta of rutas" [value]="ruta.id">{{ ruta.codigo }} - {{ ruta.nombre }}</option>
              </select>
              <span class="error-message" *ngIf="formulario.get('rutaId')?.hasError('required') && formulario.get('rutaId')?.touched">
                La ruta es obligatoria
              </span>
            </div>

            <div class="form-group">
              <label>Latitud *</label>
              <input type="number" class="form-control" formControlName="latitud" step="0.000001"
                     [class.invalid]="formulario.get('latitud')?.invalid && formulario.get('latitud')?.touched">
              <span class="error-message" *ngIf="formulario.get('latitud')?.hasError('required') && formulario.get('latitud')?.touched">
                La latitud es obligatoria
              </span>
            </div>

            <div class="form-group">
              <label>Longitud *</label>
              <input type="number" class="form-control" formControlName="longitud" step="0.000001"
                     [class.invalid]="formulario.get('longitud')?.invalid && formulario.get('longitud')?.touched">
              <span class="error-message" *ngIf="formulario.get('longitud')?.hasError('required') && formulario.get('longitud')?.touched">
                La longitud es obligatoria
              </span>
            </div>

            <div class="form-group">
              <label>Orden en la Ruta</label>
              <input type="number" class="form-control" formControlName="orden" min="1">
            </div>

            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" (click)="cerrarModal()">Cancelar</button>
              <button type="submit" class="btn btn-primary" [disabled]="formulario.invalid">Guardar</button>
            </div>
          </form>
        </div>
      </div>

      <!-- Modal Confirmar -->
      <div class="modal-overlay" *ngIf="mostrarConfirmar" (click)="mostrarConfirmar = false">
        <div class="modal" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h2>Confirmar Eliminacion</h2>
            <button class="modal-close" (click)="mostrarConfirmar = false">&times;</button>
          </div>
          <p>Esta seguro de eliminar la parada {{ paradaEliminar?.nombre }}?</p>
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
export class ParadasComponent implements OnInit {
  private paradaService = inject(ParadaService);
  private rutaService = inject(RutaService);
  private fb = inject(FormBuilder);

  paradas: Parada[] = [];
  rutas: Ruta[] = [];
  cargando = false;
  mostrarModal = false;
  mostrarConfirmar = false;
  editando = false;
  paradaActual: Parada | null = null;
  paradaEliminar: Parada | null = null;

  formulario: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    direccion: [''],
    rutaId: ['', Validators.required],
    latitud: [null, Validators.required],
    longitud: [null, Validators.required],
    orden: [null]
  });

  ngOnInit() {
    this.cargarParadas();
    this.cargarRutas();
  }

  cargarParadas() {
    this.cargando = true;
    this.paradaService.obtenerTodas().subscribe({
      next: (data) => {
        this.paradas = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando paradas', err);
        this.cargando = false;
      }
    });
  }

  cargarRutas() {
    this.rutaService.obtenerTodas().subscribe({
      next: (data) => this.rutas = data,
      error: (err) => console.error('Error cargando rutas', err)
    });
  }

  abrirModal() {
    this.editando = false;
    this.paradaActual = null;
    this.formulario.reset();
    this.mostrarModal = true;
  }

  editar(parada: Parada) {
    this.editando = true;
    this.paradaActual = parada;
    this.formulario.patchValue(parada);
    this.mostrarModal = true;
  }

  cerrarModal() {
    this.mostrarModal = false;
    this.formulario.reset();
  }

  guardar() {
    if (this.formulario.invalid) return;

    const parada: Parada = this.formulario.value;

    if (this.editando && this.paradaActual?.id) {
      this.paradaService.actualizar(this.paradaActual.id, parada).subscribe({
        next: () => {
          this.cargarParadas();
          this.cerrarModal();
        },
        error: (err) => console.error('Error actualizando', err)
      });
    } else {
      this.paradaService.crear(parada).subscribe({
        next: () => {
          this.cargarParadas();
          this.cerrarModal();
        },
        error: (err) => console.error('Error creando', err)
      });
    }
  }

  confirmarEliminar(parada: Parada) {
    this.paradaEliminar = parada;
    this.mostrarConfirmar = true;
  }

  eliminar() {
    if (this.paradaEliminar?.id) {
      this.paradaService.eliminar(this.paradaEliminar.id).subscribe({
        next: () => {
          this.cargarParadas();
          this.mostrarConfirmar = false;
        },
        error: (err) => console.error('Error eliminando', err)
      });
    }
  }
}
