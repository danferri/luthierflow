import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';
import { AppComponent } from './app/app.component';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { Amplify } from 'aws-amplify';
import { authInterceptor } from './app/interceptors/auth-interceptor';

Amplify.configure({
  Auth: {
    Cognito: {
      userPoolId: 'us-east-2_gHnFr1s1n',
      userPoolClientId: 'bb4lf4jl7ki66hgonadskr84f'
    }
  }
});

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor]))
  ]
}).catch((err) => console.error(err));
