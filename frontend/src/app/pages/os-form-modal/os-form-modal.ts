import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Cliente, ClienteService } from '../../services/cliente.service';
import { Instrumento, InstrumentoService } from '../../services/instrumento.service';
import { OrdemServicoService, OrdemServicoCreateRequest } from '../../services/ordem-servico.service';

@Component({
  selector: 'app-os-form-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './os-form-modal.html',
  styleUrls: ['./os-form-modal.scss']
})
export class OsFormModalComponent implements OnInit {

  @Output() fechar = new EventEmitter<boolean>();

  osForm!: FormGroup;
  clientes: Cliente[] = [];
  instrumentosDoCliente: Instrumento[] = [];

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,    
    private osService: OrdemServicoService
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    this.loadClientes();
  }

  initializeForm(): void {
    this.osForm = this.fb.group({
      clienteId: [null, [Validators.required]],
      instrumentoId: [null],
      tipoServico: [null, [Validators.required]],
      descricaoProblema: ['', [Validators.required]]
    });
  }

  loadClientes(): void {
    this.clienteService.listar().subscribe({
      next: (dados) => this.clientes = dados,
      error: (err) => alert('Erro ao carregar clientes.')
    });
  }

  onClienteChange(): void {
    const clienteId = this.osForm.get('clienteId')?.value;
    this.instrumentosDoCliente = [];
    this.osForm.get('instrumentoId')?.setValue(null);
    
    if (clienteId) {
      this.clienteService.listarInstrumentosPorCliente(clienteId).subscribe({
        next: (dados) => this.instrumentosDoCliente = dados,
        error: (err) => alert('Erro ao carregar instrumentos deste cliente.')
      });
    }
  }

  onSubmit(): void {
    this.osForm.markAllAsTouched();
    if (this.osForm.invalid) return;

    const requestData: OrdemServicoCreateRequest = this.osForm.getRawValue();

    this.osService.salvar(requestData).subscribe({
      next: () => {
        alert('Ordem de Serviço criada com sucesso!');
        this.fechar.emit(true);
      },
      error: (err) => {
        const userMessage = err.error?.message || err.message || 'Erro desconhecido.';
        alert(`Erro ao criar O.S.: ${userMessage}`);
      }
    });
  }

  onCancel(): void {
    this.fechar.emit(false);
  }
}