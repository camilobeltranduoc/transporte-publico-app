import { Routes } from '@angular/router';
import { MsalGuard } from '@azure/msal-angular';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent),
    canActivate: [MsalGuard]
  },
  {
    path: 'vehiculos',
    loadComponent: () => import('./pages/vehiculos/vehiculos.component').then(m => m.VehiculosComponent),
    canActivate: [MsalGuard]
  },
  {
    path: 'rutas',
    loadComponent: () => import('./pages/rutas/rutas.component').then(m => m.RutasComponent),
    canActivate: [MsalGuard]
  },
  {
    path: 'horarios',
    loadComponent: () => import('./pages/horarios/horarios.component').then(m => m.HorariosComponent),
    canActivate: [MsalGuard]
  },
  {
    path: 'paradas',
    loadComponent: () => import('./pages/paradas/paradas.component').then(m => m.ParadasComponent),
    canActivate: [MsalGuard]
  }
];
