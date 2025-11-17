import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Peca, PecaRequest, PecaService } from '../../services/peca.service';

@Component({
  selector: 'app-peca-form-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './peca-form-modal.html',
  styleUrls: ['./peca-form-modal.scss']
})
export class PecaFormModalComponent implements OnInit {
  
  @Input() pecaIdParaEditar: number | null = null; 
  
  @Output() fechar = new EventEmitter<boolean>();

  pecaForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private pecaService: PecaService
  ) {}

  ngOnInit(): void {
    this.initializeForm();    
    
    if (this.pecaIdParaEditar) {
      this.pecaService.buscarPorId(this.pecaIdParaEditar).subscribe(peca => {        
        this.pecaForm.patchValue(peca);
      });
    }
  }

  initializeForm(): void {    
    this.pecaForm = this.fb.group({
      nomePeca: ['', [Validators.required]],
      fabricante: [''],
      modelo: [''],
      qtdEstoque: [0, [Validators.required, Validators.min(0)]],
      precoVenda: [null, [Validators.required, Validators.min(0.01)]]
    });
  }

  onSubmit(): void {
    this.pecaForm.markAllAsTouched();
    if (this.pecaForm.invalid) return;
   
    const pecaData: PecaRequest = this.pecaForm.getRawValue();

    if (this.pecaIdParaEditar) {      
      this.pecaService.atualizar(this.pecaIdParaEditar, pecaData).subscribe({
        next: () => this.fechar.emit(true),
        error: (err) => this.handleError(err)
      });
    } else {      
      this.pecaService.salvar(pecaData).subscribe({
        next: () => this.fechar.emit(true),
        error: (err) => this.handleError(err)
      });
    }
  }

  handleError(err: any): void {    
    const userMessage = err.error?.message || err.message || 'Erro desconhecido.';
    alert(`Erro ao salvar peça: ${userMessage}`);
    console.error(err);
  }

  onCancel(): void {
    this.fechar.emit(false);
  }
}