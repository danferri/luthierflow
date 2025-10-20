import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AmplifyAuthenticatorModule } from '@aws-amplify/ui-angular';
import { Router } from '@angular/router';
import { Hub } from 'aws-amplify/utils';
import { getCurrentUser } from 'aws-amplify/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, AmplifyAuthenticatorModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class LoginComponent implements OnInit {

  constructor(private router: Router) {}
  
  async ngOnInit(): Promise<void> {
    
    try {
      await getCurrentUser();
      
      this.router.navigate(['/dashboard']);
    } catch (error) {      
      console.log('Nenhum usuário logado encontrado, exibindo formulário de login.');
    }
    
    Hub.listen('auth', ({ payload }) => {      
      if (payload.event === 'signedIn') {
        this.router.navigate(['/dashboard']);
      }
    });
  }
}