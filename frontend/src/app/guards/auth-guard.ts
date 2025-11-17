import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { fetchAuthSession } from 'aws-amplify/auth';

export const authGuard: CanActivateFn = async () => {
  const router = inject(Router);

  try {
    const session = await fetchAuthSession();    
    return session.tokens !== undefined;

  } catch (err) {    
    router.navigate(['']);
    return false;
     
  }
};