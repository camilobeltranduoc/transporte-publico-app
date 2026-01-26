import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MsalService } from '@azure/msal-angular';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="home-container">
      <div class="home-card">
        <h1>TransporteGO</h1>
        <p>Sistema de Gestion de Transporte Publico</p>
        <button class="btn-login" (click)="login()">Iniciar Sesion con Azure</button>
      </div>
    </div>
  `,
  styles: [`
    .home-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #1976d2 0%, #0d47a1 100%);
    }
    .home-card {
      background: white;
      padding: 40px 60px;
      border-radius: 12px;
      text-align: center;
      box-shadow: 0 10px 40px rgba(0,0,0,0.2);
    }
    .home-card h1 {
      margin: 0 0 10px;
      color: #1976d2;
    }
    .home-card p {
      margin: 0 0 30px;
      color: #666;
    }
    .btn-login {
      background: #1976d2;
      color: white;
      border: none;
      padding: 15px 30px;
      font-size: 16px;
      border-radius: 6px;
      cursor: pointer;
    }
    .btn-login:hover {
      background: #1565c0;
    }
  `]
})
export class HomeComponent {
  private authService = inject(MsalService);

  login() {
    this.authService.loginRedirect();
  }
}
