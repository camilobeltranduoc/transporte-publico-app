import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { MsalService, MsalBroadcastService, MsalRedirectComponent } from '@azure/msal-angular';
import { InteractionStatus } from '@azure/msal-browser';
import { Subject, filter, takeUntil } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive, MsalRedirectComponent],
  template: `
    <div class="app-container">
      <nav class="navbar" *ngIf="isAuthenticated">
        <div class="navbar-brand">
          <span class="brand-name">TransporteGO</span>
        </div>
        <div class="navbar-menu">
          <a routerLink="/dashboard" routerLinkActive="active">Dashboard</a>
          <a routerLink="/vehiculos" routerLinkActive="active">Vehiculos</a>
          <a routerLink="/rutas" routerLinkActive="active">Rutas</a>
          <a routerLink="/horarios" routerLinkActive="active">Horarios</a>
          <a routerLink="/paradas" routerLinkActive="active">Paradas</a>
        </div>
        <div class="navbar-user">
          <span>{{ userName }}</span>
          <button class="btn btn-secondary" (click)="logout()">Cerrar Sesion</button>
        </div>
      </nav>
      <main class="main-content">
        <router-outlet></router-outlet>
      </main>
      <app-redirect></app-redirect>
    </div>
  `,
  styles: [`
    .app-container {
      min-height: 100vh;
      display: flex;
      flex-direction: column;
    }
    .navbar {
      background: linear-gradient(135deg, #1976d2 0%, #1565c0 100%);
      color: white;
      padding: 0 20px;
      display: flex;
      align-items: center;
      height: 60px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .navbar-brand {
      display: flex;
      align-items: center;
      gap: 10px;
    }
    .brand-name {
      font-size: 20px;
      font-weight: 700;
    }
    .navbar-menu {
      display: flex;
      gap: 5px;
      margin-left: 40px;
    }
    .navbar-menu a {
      color: rgba(255,255,255,0.8);
      text-decoration: none;
      padding: 8px 16px;
      border-radius: 4px;
      transition: all 0.3s ease;
    }
    .navbar-menu a:hover, .navbar-menu a.active {
      background: rgba(255,255,255,0.1);
      color: white;
    }
    .navbar-user {
      margin-left: auto;
      display: flex;
      align-items: center;
      gap: 15px;
    }
    .btn-secondary {
      background: rgba(255,255,255,0.2);
      color: white;
      border: none;
      padding: 8px 16px;
      border-radius: 4px;
      cursor: pointer;
    }
    .btn-secondary:hover {
      background: rgba(255,255,255,0.3);
    }
    .main-content {
      flex: 1;
      padding: 20px;
    }
  `]
})
export class AppComponent implements OnInit, OnDestroy {
  private msalService = inject(MsalService);
  private msalBroadcastService = inject(MsalBroadcastService);
  private readonly destroying$ = new Subject<void>();

  isAuthenticated = false;
  userName = '';

  ngOnInit(): void {
    this.msalService.handleRedirectObservable().subscribe();

    this.msalBroadcastService.inProgress$
      .pipe(
        filter((status: InteractionStatus) => status === InteractionStatus.None),
        takeUntil(this.destroying$)
      )
      .subscribe(() => {
        this.setAuthenticationStatus();
      });
  }

  setAuthenticationStatus(): void {
    this.isAuthenticated = this.msalService.instance.getAllAccounts().length > 0;
    if (this.isAuthenticated) {
      const account = this.msalService.instance.getAllAccounts()[0];
      this.userName = account.name || account.username || '';
    }
  }

  logout(): void {
    this.msalService.logoutRedirect({
      postLogoutRedirectUri: '/'
    });
  }

  ngOnDestroy(): void {
    this.destroying$.next();
    this.destroying$.complete();
  }
}
