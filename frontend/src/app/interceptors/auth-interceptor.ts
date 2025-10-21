import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpEvent } from '@angular/common/http';
import { from, Observable, switchMap } from 'rxjs';
import { fetchAuthSession } from 'aws-amplify/auth';

export const authInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {
  
  return from(fetchAuthSession({ forceRefresh: false })).pipe(
    switchMap(session => {
      
      const token = session.tokens?.idToken?.toString();

      if (token) {
        
        const clonedReq = req.clone({
          headers: req.headers.set('Authorization', `Bearer ${token}`)
        });
        
        return next(clonedReq);
      } else {
        
        return next(req);
      }
    }),
    
    catchError(error => {
      console.warn('Não foi possível adicionar o token de autenticação:', error);
      
      return next(req);
    })
  );
};

import { catchError } from 'rxjs/operators';