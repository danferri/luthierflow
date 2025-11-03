import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ClienteService, Cliente } from '../../services/cliente.service';
import { switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { NgxMaskDirective } from 'ngx-mask';
import { Instrumento, InstrumentoService } from '../../services/instrumento.service';
import { InstrumentoFormModalComponent } from '../../pages/instrumento-form-modal/instrumento-form-modal';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgxMaskDirective, InstrumentoFormModalComponent],
  templateUrl: './cliente-form.html',
  styleUrls: ['./cliente-form.scss']
})
export class ClienteFormComponent implements OnInit {

  clienteForm!: FormGroup;
  isEditMode = false;
  clienteId: number | null = null;

  instrumentosDoCliente: Instrumento[] = [];
  isInstrumentoModalVisible = false;
  instrumentoIdParaEditar: number | null = null;
  
  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private instrumentoService: InstrumentoService,
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
      cpf: ['', [Validators.required]],
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
          this.carregarInstrumentos(this.clienteId);           
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

  carregarInstrumentos(clienteId: number): void {
    this.clienteService.listarInstrumentosPorCliente(clienteId).subscribe({
      next: (dados) => this.instrumentosDoCliente = dados,
      error: (err) => console.error("Erro ao carregar instrumentos", err)
    });
  }

  abrirModalInstrumento(instrumentoId: number | null = null): void {
    this.instrumentoIdParaEditar = instrumentoId; // Se for edição, passa o ID
    this.isInstrumentoModalVisible = true;
  }

  aoFecharModal(salvou: boolean): void {
    this.isInstrumentoModalVisible = false;
    // Se o modal foi fechado com "salvar", recarrega a lista
    if (salvou && this.clienteId) {
      this.carregarInstrumentos(this.clienteId);
    }
  }

  excluirInstrumento(instrumento: Instrumento): void {
    if (window.confirm(`Deseja arquivar o instrumento ${instrumento.marca} ${instrumento.modelo}?`)) {
      this.instrumentoService.deletar(instrumento.id).subscribe({
        next: () => this.carregarInstrumentos(this.clienteId!),
        error: (err) => alert('Erro ao arquivar instrumento.')
      });
    }
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