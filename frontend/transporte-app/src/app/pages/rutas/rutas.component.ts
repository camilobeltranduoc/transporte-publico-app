import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RutaService, Ruta } from '../../services/ruta.service';

@Component({
  selector: 'app-rutas',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  template: `
    <div class="container">
      <div class="page-header">
        <h1>Gestion de Rutas</h1>
        <button class="btn btn-primary" (click)="abrirModal()">Nueva Ruta</button>
      </div>

      <div class="card">
        <div class="loading" *ngIf="cargando">
          <div class="spinner"></div>
        </div>

        <table *ngIf="!cargando && rutas.length > 0">
          <thead>
            <tr>
              <th>ID</th>
              <th>Codigo</th>
              <th>Nombre</th>
              <th>Origen</th>
              <th>Destino</th>
              <th>Distancia (km)</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let ruta of rutas">
              <td>{{ ruta.id }}</td>
              <td>{{ ruta.codigo }}</td>
              <td>{{ ruta.nombre }}</td>
              <td>{{ ruta.origen || '-' }}</td>
              <td>{{ ruta.destino || '-' }}</td>
              <td>{{ ruta.distanciaKm || '-' }}</td>
              <td>
                <span class="badge" [ngClass]="ruta.activa ? 'badge-success' : 'badge-danger'">
                  {{ ruta.activa ? 'Activa' : 'Inactiva' }}
                </span>
              </td>
              <td class="actions">
                <button class="btn btn-secondary" (click)="editar(ruta)">Editar</button>
                <button class="btn btn-danger" (click)="confirmarEliminar(ruta)">Eliminar</button>
              </td>
            </tr>
          </tbody>
        </table>

        <p *ngIf="!cargando && rutas.length === 0" class="empty-message">
          No hay rutas registradas
        </p>
      </div>

      <!-- Modal -->
      <div class="modal-overlay" *ngIf="mostrarModal" (click)="cerrarModal()">
        <div class="modal" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h2>{{ editando ? 'Editar' : 'Nueva' }} Ruta</h2>
            <button class="modal-close" (click)="cerrarModal()">&times;</button>
          </div>
          <form [formGroup]="formulario" (ngSubmit)="guardar()">
            <div class="form-group">
              <label>Codigo *</label>
              <input type="text" class="form-control" formControlName="codigo"
                     [class.invalid]="formulario.get('codigo')?.invalid && formulario.get('codigo')?.touched">
              <span class="error-message" *ngIf="formulario.get('codigo')?.hasError('required') && formulario.get('codigo')?.touched">
                El codigo es obligatorio
              </span>
            </div>

            <div class="form-group">
              <label>Nombre *</label>
              <input type="text" class="form-control" formControlName="nombre"
                     [class.invalid]="formulario.get('nombre')?.invalid && formulario.get('nombre')?.touched">
              <span class="error-message" *ngIf="formulario.get('nombre')?.hasError('required') && formulario.get('nombre')?.touched">
                El nombre es obligatorio
              </span>
            </div>

            <div class="form-group">
              <label>Descripcion</label>
              <textarea class="form-control" formControlName="descripcion" rows="2"></textarea>
            </div>

            <div class="form-group">
              <label>Origen</label>
              <input type="text" class="form-control" formControlName="origen">
            </div>

            <div class="form-group">
              <label>Destino</label>
              <input type="text" class="form-control" formControlName="destino">
            </div>

            <div class="form-group">
              <label>Distancia (km)</label>
              <input type="number" class="form-control" formControlName="distanciaKm" min="0" step="0.1">
            </div>

            <div class="form-group">
              <label>
                <input type="checkbox" formControlName="activa"> Ruta Activa
              </label>
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
          <p>Esta seguro de eliminar la ruta {{ rutaEliminar?.nombre }}?</p>
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
export class RutasComponent implements OnInit {
  private rutaService = inject(RutaService);
  private fb = inject(FormBuilder);

  rutas: Ruta[] = [];
  cargando = false;
  mostrarModal = false;
  mostrarConfirmar = false;
  editando = false;
  rutaActual: Ruta | null = null;
  rutaEliminar: Ruta | null = null;

  formulario: FormGroup = this.fb.group({
    codigo: ['', Validators.required],
    nombre: ['', Validators.required],
    descripcion: [''],
    origen: [''],
    destino: [''],
    distanciaKm: [null],
    activa: [true]
  });

  ngOnInit() {
    this.cargarRutas();
  }

  cargarRutas() {
    this.cargando = true;
    this.rutaService.obtenerTodas().subscribe({
      next: (data) => {
        this.rutas = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando rutas', err);
        this.cargando = false;
      }
    });
  }

  abrirModal() {
    this.editando = false;
    this.rutaActual = null;
    this.formulario.reset({ activa: true });
    this.mostrarModal = true;
  }

  editar(ruta: Ruta) {
    this.editando = true;
    this.rutaActual = ruta;
    this.formulario.patchValue(ruta);
    this.mostrarModal = true;
  }

  cerrarModal() {
    this.mostrarModal = false;
    this.formulario.reset();
  }

  guardar() {
    if (this.formulario.invalid) return;

    const ruta: Ruta = this.formulario.value;

    if (this.editando && this.rutaActual?.id) {
      this.rutaService.actualizar(this.rutaActual.id, ruta).subscribe({
        next: () => {
          this.cargarRutas();
          this.cerrarModal();
        },
        error: (err) => console.error('Error actualizando', err)
      });
    } else {
      this.rutaService.crear(ruta).subscribe({
        next: () => {
          this.cargarRutas();
          this.cerrarModal();
        },
        error: (err) => console.error('Error creando', err)
      });
    }
  }

  confirmarEliminar(ruta: Ruta) {
    this.rutaEliminar = ruta;
    this.mostrarConfirmar = true;
  }

  eliminar() {
    if (this.rutaEliminar?.id) {
      this.rutaService.eliminar(this.rutaEliminar.id).subscribe({
        next: () => {
          this.cargarRutas();
          this.mostrarConfirmar = false;
        },
        error: (err) => console.error('Error eliminando', err)
      });
    }
  }
}
