import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { OrdemServico, OrdemServicoService, OrdemServicoUpdateRequest } from '../../services/ordem-servico.service';
import { Peca, PecaService } from '../../services/peca.service';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-os-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, CurrencyPipe],
  templateUrl: './os-detail.html',
  styleUrls: ['./os-detail.scss']
})
export class OsDetailComponent implements OnInit {

  os: OrdemServico | null = null;
  osUpdateForm!: FormGroup;
  pecaAddForm!: FormGroup;
  statusControl = new FormControl<string | null>(null);

  pecasDisponiveis: Peca[] = [];
  
  subtotalPecas: number = 0;
  totalServico: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private osService: OrdemServicoService,
    private pecaService: PecaService
  ) {}

  ngOnInit(): void {
    this.initializeForms();
    this.loadOsData();
    this.loadPecasDisponiveis();

    this.statusControl.valueChanges.subscribe(novoStatus => {
      if (novoStatus && novoStatus !== this.os?.status) {        
        this.onUpdateStatus(novoStatus);
      }
    });
  }

  initializeForms(): void {
    this.osUpdateForm = this.fb.group({
      diagnosticoServico: [''],
      valorMaoDeObra: [0, Validators.min(0)]
    });
    this.pecaAddForm = this.fb.group({
      pecaId: [null, [Validators.required]],
      quantidade: [1, [Validators.required, Validators.min(1)]]
    });
  }

  loadOsData(): void {
    this.route.paramMap.pipe(
      switchMap(params => {
        const id = Number(params.get('id'));
        if (id) {
          return this.osService.buscarPorId(id);
        }
        throw new Error('ID da Ordem de Serviço não encontrado');
      })
    ).subscribe({
      next: (os) => {
        this.os = os;
        this.updateFormsComOsData(os);
        this.calcularTotais(os);
      },
      error: (err) => {
        alert('Erro ao carregar Ordem de Serviço.');
        this.router.navigate(['/ordens-servico']);
      }
    });
  }

  loadPecasDisponiveis(): void {
    this.pecaService.listar().subscribe(pecas => {
      this.pecasDisponiveis = pecas.filter(p => p.qtdEstoque > 0);
    });
  }

  updateFormsComOsData(os: OrdemServico): void {
    this.osUpdateForm.patchValue({
      diagnosticoServico: os.diagnosticoServico,
      valorMaoDeObra: os.valorMaoDeObra
    });
    
    this.osUpdateForm.markAsPristine();
    this.statusControl.setValue(os.status, { emitEvent: false });
  }

  calcularTotais(os: OrdemServico): void {
    this.subtotalPecas = os.itens.reduce((acc, item) => {
      return acc + (item.precoVenda * item.quantidadeUsada);
    }, 0);
    this.totalServico = this.subtotalPecas + (os.valorMaoDeObra || 0);
  }

  
  onUpdateFormSubmit(): void {
    if (!this.os || !this.osUpdateForm.dirty) {      
      this.router.navigate(['/ordens-servico']);
      return;
    }

    const formData = this.osUpdateForm.getRawValue();
    const updateRequest: OrdemServicoUpdateRequest = {
      diagnosticoServico: formData.diagnosticoServico,
      valorMaoDeObra: formData.valorMaoDeObra,
    };

    this.osService.atualizar(this.os.id, updateRequest).subscribe({
      next: (osAtualizada) => {        
        alert('Ordem de Serviço atualizada com sucesso!');
        this.router.navigate(['/ordens-servico']);
      },
      error: (err) => alert('Erro ao atualizar a O.S.')
    });
  }
  
  onUpdateStatus(novoStatus: string): void {
    if (!this.os) return;

    this.osService.atualizar(this.os.id, { status: novoStatus }).subscribe({
      next: (osAtualizada) => {
        this.os = osAtualizada;
        this.updateFormsComOsData(osAtualizada);
        this.calcularTotais(osAtualizada);
        console.log('Status salvo!', osAtualizada);
      },
      error: (err) => alert('Erro ao atualizar o status.')
    });
  }

  onAdicionarPeca(): void {
    if (this.pecaAddForm.invalid || !this.os) return;

    const request = this.pecaAddForm.getRawValue();
    this.osService.adicionarPeca(this.os.id, { pecaId: request.pecaId, quantidade: request.quantidade }).subscribe({
      next: (osAtualizada) => {
        this.os = osAtualizada;
        this.calcularTotais(osAtualizada);
        this.pecaAddForm.reset({ quantidade: 1 });
        this.loadPecasDisponiveis();
      },
      error: (err) => alert(err.error?.message || 'Erro ao adicionar peça.')
    });
  }

  onRemoverPeca(pecaId: number): void {
    if (!this.os) return;
    if (window.confirm('Tem certeza que deseja remover esta peça da O.S.? O estoque será devolvido.')) {
      this.osService.removerPeca(this.os.id, pecaId).subscribe({
         next: (osAtualizada) => {
          this.os = osAtualizada;
          this.calcularTotais(osAtualizada);
          this.loadPecasDisponiveis();
        },
        error: (err) => alert('Erro ao remover peça.')
      });
    }
  }
  
  voltarParaLista(): void {
    this.router.navigate(['/ordens-servico']);
  }
}