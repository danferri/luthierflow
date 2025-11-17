import { Routes, CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { LoginComponent } from './pages/login/login';
import { DashboardComponent } from './pages/dashboard/dashboard';
import { fetchAuthSession } from 'aws-amplify/auth';

const authGuard: CanActivateFn = async () => {
  const router = inject(Router);
  try {
    await fetchAuthSession();    
    return true;

  } catch {    
    return router.parseUrl('/login');

  }
};

export const routes: Routes = [  
  {
    path: 'login',
    component: LoginComponent
  },
  
  {
    path: '',
    loadComponent: () =>
      import('./layout/main/main').then((m: any) => m.MainLayoutComponent ?? m.MainComponent ?? m.MainLayout ?? m.default),
    canActivate: [authGuard],
    children: [      
      {
        path: 'dashboard',
        loadComponent: () => import('./pages/dashboard/dashboard').then(m => m.DashboardComponent)
      },
      {
        path: 'clientes',
        loadComponent: () => import('./pages/cliente-list/cliente-list').then(m => m.ClienteListComponent)
      },
      {        
        path: 'clientes/novo',
        loadComponent: () => import('./pages/cliente-form/cliente-form').then(m => m.ClienteFormComponent)
      },
      {        
        path: 'clientes/editar/:id',
        loadComponent: () => import('./pages/cliente-form/cliente-form').then(m => m.ClienteFormComponent)
      },
      {
        path: 'pecas',
        loadComponent: () => import('./pages/peca-list/peca-list').then(m => m.PecaListComponent)
      },      
      {
        path: 'ordens-servico',
        loadComponent: () => import('./pages/os-list/os-list').then(m => m.OsListComponent)
      },
      {
        path: 'ordens-servico/:id',
        loadComponent: () => import('./pages/os-detail/os-detail').then(m => m.OsDetailComponent)
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  },
  
  { path: '**', redirectTo: '' }
];