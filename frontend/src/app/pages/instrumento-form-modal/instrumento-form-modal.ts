import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Instrumento, InstrumentoRequest, InstrumentoService } from '../../services/instrumento.service';

@Component({
  selector: 'app-instrumento-form-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './instrumento-form-modal.html',
  styleUrls: ['./instrumento-form-modal.scss']
})
export class InstrumentoFormModalComponent implements OnInit {

  // ENTRADA: Recebe o ID do cliente do componente pai
  @Input() clienteId!: number;
  
  // ENTRADA: Se receber um ID, entra em modo de edição
  @Input() instrumentoIdParaEditar: number | null = null; 

  // SAÍDA: Avisa o componente pai que algo foi salvo (para fechar e recarregar a lista)
  @Output() fechar = new EventEmitter<boolean>();

  instrumentoForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private instrumentoService: InstrumentoService
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    
    // Se recebeu um ID para editar...
    if (this.instrumentoIdParaEditar) {
      this.instrumentoService.buscarPorId(this.instrumentoIdParaEditar).subscribe(instrumento => {
        // Preenche o formulário com os dados do instrumento
        this.instrumentoForm.patchValue(instrumento);
      });
    }
  }

  initializeForm(): void {
    this.instrumentoForm = this.fb.group({
      tipo: ['', [Validators.required]],
      marca: ['', [Validators.required]],
      modelo: ['', [Validators.required]],
      numeroSerie: ['']
    });
  }

  onSubmit(): void {
    this.instrumentoForm.markAllAsTouched();
    if (this.instrumentoForm.invalid) return;

    const formValues = this.instrumentoForm.getRawValue();
    
    const instrumentoData: InstrumentoRequest = {
      ...formValues,
      idCliente: this.clienteId // Associa o ID do cliente recebido via @Input
    };

    if (this.instrumentoIdParaEditar) {
      // Lógica de ATUALIZAR
      this.instrumentoService.atualizar(this.instrumentoIdParaEditar, instrumentoData).subscribe({
        next: () => this.fechar.emit(true), // Emite evento de sucesso
        error: (err) => alert('Erro ao atualizar instrumento.')
      });
    } else {
      // Lógica de SALVAR
      this.instrumentoService.salvar(instrumentoData).subscribe({
        next: () => this.fechar.emit(true), // Emite evento de sucesso
        error: (err) => alert('Erro ao salvar instrumento.')
      });
    }
  }

  onCancel(): void {
    this.fechar.emit(false); // Emite evento de "cancelar"
  }
}