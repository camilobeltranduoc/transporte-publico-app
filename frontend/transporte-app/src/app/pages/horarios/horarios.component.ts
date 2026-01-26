import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HorarioService, Horario } from '../../services/horario.service';
import { RutaService, Ruta } from '../../services/ruta.service';

@Component({
  selector: 'app-horarios',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  template: `
    <div class="container">
      <div class="page-header">
        <h1>Gestion de Horarios</h1>
        <button class="btn btn-primary" (click)="abrirModal()">Nuevo Horario</button>
      </div>

      <div class="card">
        <div class="loading" *ngIf="cargando">
          <div class="spinner"></div>
        </div>

        <table *ngIf="!cargando && horarios.length > 0">
          <thead>
            <tr>
              <th>ID</th>
              <th>Ruta</th>
              <th>Dia</th>
              <th>Hora Inicio</th>
              <th>Hora Fin</th>
              <th>Frecuencia</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let horario of horarios">
              <td>{{ horario.id }}</td>
              <td>{{ horario.codigoRuta }}</td>
              <td>{{ traducirDia(horario.diaSemana) }}</td>
              <td>{{ horario.horaInicio }}</td>
              <td>{{ horario.horaFin }}</td>
              <td>{{ horario.frecuenciaMinutos ? horario.frecuenciaMinutos + ' min' : '-' }}</td>
              <td>
                <span class="badge" [ngClass]="horario.activo ? 'badge-success' : 'badge-danger'">
                  {{ horario.activo ? 'Activo' : 'Inactivo' }}
                </span>
              </td>
              <td class="actions">
                <button class="btn btn-secondary" (click)="editar(horario)">Editar</button>
                <button class="btn btn-danger" (click)="confirmarEliminar(horario)">Eliminar</button>
              </td>
            </tr>
          </tbody>
        </table>

        <p *ngIf="!cargando && horarios.length === 0" class="empty-message">
          No hay horarios registrados
        </p>
      </div>

      <!-- Modal -->
      <div class="modal-overlay" *ngIf="mostrarModal" (click)="cerrarModal()">
        <div class="modal" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h2>{{ editando ? 'Editar' : 'Nuevo' }} Horario</h2>
            <button class="modal-close" (click)="cerrarModal()">&times;</button>
          </div>
          <form [formGroup]="formulario" (ngSubmit)="guardar()">
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
              <label>Dia de la Semana *</label>
              <select class="form-control" formControlName="diaSemana">
                <option value="">Seleccione...</option>
                <option value="MONDAY">Lunes</option>
                <option value="TUESDAY">Martes</option>
                <option value="WEDNESDAY">Miercoles</option>
                <option value="THURSDAY">Jueves</option>
                <option value="FRIDAY">Viernes</option>
                <option value="SATURDAY">Sabado</option>
                <option value="SUNDAY">Domingo</option>
              </select>
              <span class="error-message" *ngIf="formulario.get('diaSemana')?.hasError('required') && formulario.get('diaSemana')?.touched">
                El dia es obligatorio
              </span>
            </div>

            <div class="form-group">
              <label>Hora Inicio *</label>
              <input type="time" class="form-control" formControlName="horaInicio">
              <span class="error-message" *ngIf="formulario.get('horaInicio')?.hasError('required') && formulario.get('horaInicio')?.touched">
                La hora de inicio es obligatoria
              </span>
            </div>

            <div class="form-group">
              <label>Hora Fin *</label>
              <input type="time" class="form-control" formControlName="horaFin">
              <span class="error-message" *ngIf="formulario.get('horaFin')?.hasError('required') && formulario.get('horaFin')?.touched">
                La hora de fin es obligatoria
              </span>
            </div>

            <div class="form-group">
              <label>Frecuencia (minutos)</label>
              <input type="number" class="form-control" formControlName="frecuenciaMinutos" min="1">
            </div>

            <div class="form-group">
              <label>
                <input type="checkbox" formControlName="activo"> Horario Activo
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
          <p>Esta seguro de eliminar este horario?</p>
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
export class HorariosComponent implements OnInit {
  private horarioService = inject(HorarioService);
  private rutaService = inject(RutaService);
  private fb = inject(FormBuilder);

  horarios: Horario[] = [];
  rutas: Ruta[] = [];
  cargando = false;
  mostrarModal = false;
  mostrarConfirmar = false;
  editando = false;
  horarioActual: Horario | null = null;
  horarioEliminar: Horario | null = null;

  formulario: FormGroup = this.fb.group({
    rutaId: ['', Validators.required],
    diaSemana: ['', Validators.required],
    horaInicio: ['', Validators.required],
    horaFin: ['', Validators.required],
    frecuenciaMinutos: [null],
    activo: [true]
  });

  ngOnInit() {
    this.cargarHorarios();
    this.cargarRutas();
  }

  cargarHorarios() {
    this.cargando = true;
    this.horarioService.obtenerTodos().subscribe({
      next: (data) => {
        this.horarios = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando horarios', err);
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
    this.horarioActual = null;
    this.formulario.reset({ activo: true });
    this.mostrarModal = true;
  }

  editar(horario: Horario) {
    this.editando = true;
    this.horarioActual = horario;
    this.formulario.patchValue(horario);
    this.mostrarModal = true;
  }

  cerrarModal() {
    this.mostrarModal = false;
    this.formulario.reset();
  }

  guardar() {
    if (this.formulario.invalid) return;

    const horario: Horario = this.formulario.value;

    if (this.editando && this.horarioActual?.id) {
      this.horarioService.actualizar(this.horarioActual.id, horario).subscribe({
        next: () => {
          this.cargarHorarios();
          this.cerrarModal();
        },
        error: (err) => console.error('Error actualizando', err)
      });
    } else {
      this.horarioService.crear(horario).subscribe({
        next: () => {
          this.cargarHorarios();
          this.cerrarModal();
        },
        error: (err) => console.error('Error creando', err)
      });
    }
  }

  confirmarEliminar(horario: Horario) {
    this.horarioEliminar = horario;
    this.mostrarConfirmar = true;
  }

  eliminar() {
    if (this.horarioEliminar?.id) {
      this.horarioService.eliminar(this.horarioEliminar.id).subscribe({
        next: () => {
          this.cargarHorarios();
          this.mostrarConfirmar = false;
        },
        error: (err) => console.error('Error eliminando', err)
      });
    }
  }

  traducirDia(dia: string): string {
    const dias: { [key: string]: string } = {
      'MONDAY': 'Lunes',
      'TUESDAY': 'Martes',
      'WEDNESDAY': 'Miercoles',
      'THURSDAY': 'Jueves',
      'FRIDAY': 'Viernes',
      'SATURDAY': 'Sabado',
      'SUNDAY': 'Domingo'
    };
    return dias[dia] || dia;
  }
}
