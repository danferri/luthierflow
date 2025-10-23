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
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  },
  
  { path: '**', redirectTo: '' }
];