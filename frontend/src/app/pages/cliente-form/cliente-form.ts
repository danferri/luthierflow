import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ClienteService, Cliente } from '../../services/cliente.service';
import { switchMap } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cliente-form.html',
  styleUrls: ['./cliente-form.scss']
})
export class ClienteFormComponent implements OnInit {

  clienteForm!: FormGroup;
  isEditMode = false;
  clienteId: number | null = null;
  
  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private router: Router,
    private route: ActivatedRoute 
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    this.checkEditMode();
  }

  initializeForm(): void {
    this.clienteForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      cpf: ['', [Validators.required, Validators.minLength(11), Validators.maxLength(11)]],
      telefone: ['', Validators.required],
      cep: [''],
      rua: [''],
      cidade: [''],
      estado: ['']
    });
  }
  
  checkEditMode(): void {    
    this.route.paramMap.pipe(
      switchMap(params => {
        const idParam = params.get('id');
        if (idParam) {
          this.isEditMode = true;
          this.clienteId = Number(idParam);           
          return this.clienteService.buscarPorId(this.clienteId);
        } else {          
          this.isEditMode = false;
          this.clienteId = null;
          return of(null);
        }
      })
    ).subscribe(cliente => {
      if (cliente) {        
        this.clienteForm.patchValue({
          ...cliente,          
          telefone: cliente.telefones && cliente.telefones.length > 0 ? cliente.telefones[0] : ''
        });        
        this.clienteForm.controls['cpf'].disable();
      }
    });
  }


  onSubmit(): void {
    this.clienteForm.markAllAsTouched();

    if (this.clienteForm.valid || (this.isEditMode && this.clienteForm.controls['cpf'].disabled)) {
      const formValues = this.clienteForm.getRawValue();//.value;
      const cleanedCpf = formValues.cpf ? formValues.cpf.replace(/\D/g, '') : '';      
      const clienteData: Partial<Cliente> = {
        nome: formValues.nome,
        email: formValues.email,
        cpf: cleanedCpf,        
        cep: formValues.cep,
        rua: formValues.rua,
        cidade: formValues.cidade,
        estado: formValues.estado
      };

      if (formValues.telefone && formValues.telefone.trim() !== '') {
        clienteData.telefones = [formValues.telefone.trim()];
      } else if (this.isEditMode) {      
        clienteData.telefones = [];
      }
      
      if (this.isEditMode && this.clienteId !== null) {        
        console.log('Formulário válido! Atualizando dados:', clienteData);
        this.clienteService.atualizar(this.clienteId, clienteData).subscribe({
          next: (clienteAtualizado) => {
            console.log('Cliente atualizado com sucesso:', clienteAtualizado);
            this.router.navigate(['/clientes']);
          },
          error: (erro) => {
            console.error('Erro ao atualizar cliente:', erro);
            alert(`Erro ao atualizar cliente: ${erro.error?.message || erro.message}`);
          }
        });
      } else {        
        console.log('Formulário válido! Enviando dados:', clienteData);
        this.clienteService.salvar(clienteData).subscribe({
          next: (clienteSalvo) => {
            console.log('Cliente salvo com sucesso:', clienteSalvo);
            this.router.navigate(['/clientes']);
          },
          error: (erro) => {
            console.error('Erro ao salvar cliente:', erro);
            alert(`Erro ao salvar cliente: ${erro.error?.message || erro.message}`);
          }
        });
      }

    } else {
      console.log('Formulário inválido. Verifique os campos.');
    }
  }

  onCancel(): void {
    this.router.navigate(['/clientes']);
  }
}