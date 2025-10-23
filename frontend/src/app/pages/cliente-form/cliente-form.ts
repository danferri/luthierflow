import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ClienteService, Cliente } from '../../services/cliente.service';

@Component({
  selector: 'app-cliente-form',
  standalone: true,  
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cliente-form.html',
  styleUrls: ['./cliente-form.scss']
})
export class ClienteFormComponent implements OnInit {
  
  clienteForm!: FormGroup;
  
  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.initializeForm();
  }
  
  initializeForm(): void {
    this.clienteForm = this.fb.group({
      
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      cpf: ['', [Validators.required, Validators.minLength(11), Validators.maxLength(11)]],
      telefone: ['', [Validators.required]],     
      cep: [''], 
      rua: [''], 
      cidade: [''], 
      estado: [''] 
    });
  }
  
  onSubmit(): void {    
    this.clienteForm.markAllAsTouched();
    
    if (this.clienteForm.valid) {
      const formValues = this.clienteForm.value;
    
      const novoCliente: Partial<Cliente> = {
        nome: formValues.nome,
        email: formValues.email,
        cpf: formValues.cpf,
        telefones: [formValues.telefone],
        cep: formValues.cep,
        rua: formValues.rua,
        cidade: formValues.cidade,
        estado: formValues.estado
      } as Partial<Cliente>;

      console.log('Formulário válido!', this.clienteForm.value);
      
      this.clienteService.salvar(novoCliente).subscribe({
        next: () => {
          console.log('Cliente adicionado com sucesso!');
          this.router.navigate(['/clientes']);
        },
        error: (err) => {
          console.error('Erro ao adicionar cliente:', err);
          alert(`Erro ao salvar cliente: ${err.error?.message || err.message}`);
        }
      });
      
    } else {
      console.log('Formulário inválido. Verifique os campos.');
      
    }
  }
  
  onCancel(): void {
    this.router.navigate(['/clientes']);
  }
}