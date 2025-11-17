import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Peca, PecaService } from '../../services/peca.service';
import { PecaFormModalComponent } from '../peca-form-modal/peca-form-modal';

@Component({
  selector: 'app-peca-list',
  standalone: true,  
  imports: [CommonModule, PecaFormModalComponent, CurrencyPipe],
  templateUrl: './peca-list.html',
  styleUrls: ['./peca-list.scss']
})
export class PecaListComponent implements OnInit {

  pecas: Peca[] = [];
  
  isPecaModalVisible = false;
  pecaIdParaEditar: number | null = null;
  
  constructor(private pecaService: PecaService) {}

  ngOnInit(): void {
    this.buscarPecas();
  }

  buscarPecas(): void {
    this.pecaService.listar()
      .subscribe({
        next: (dados) => {
          this.pecas = dados;
        },
        error: (erro) => {
          console.error('Erro ao buscar peças:', erro);
          alert(`Erro ao buscar peças: ${erro.error?.message || erro.message}`);
        }
      });
  }
  
  excluirPeca(peca: Peca): void {    
    const confirmacao = window.confirm(`Tem certeza que deseja excluir a peça "${peca.nomePeca}" (ID: ${peca.id})?`);
    
    if (confirmacao) {      
      this.pecaService.deletar(peca.id).subscribe({
        next: () => {          
          this.pecas = this.pecas.filter(p => p.id !== peca.id);
          console.log(`Peça ${peca.id} excluída com sucesso.`);          
        },
        error: (erro) => {          
          console.error(`Erro ao excluir peça ${peca.id}:`, erro);
          alert(`Erro ao excluir: ${erro.error?.message || erro.message || 'Erro desconhecido.'}`);         
        }
      });
    }
  }
  
  abrirModalPeca(pecaId: number | null = null): void {
    this.pecaIdParaEditar = pecaId;
    this.isPecaModalVisible = true;
  }

  aoFecharModal(salvou: boolean): void {
    this.isPecaModalVisible = false;    
    if (salvou) {
      this.buscarPecas();
    }
  }
}