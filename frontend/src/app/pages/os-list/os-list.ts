import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OrdemServico, OrdemServicoService } from '../../services/ordem-servico.service';
import { OsFormModalComponent } from '../os-form-modal/os-form-modal'; 

@Component({
  selector: 'app-os-list',
  standalone: true,  
  imports: [
    CommonModule, 
    RouterLink, 
    OsFormModalComponent
  ],
  templateUrl: './os-list.html',
  styleUrls: ['./os-list.scss']
})
export class OsListComponent implements OnInit {

  isModalVisible = false;

  ordensOrcamento: OrdemServico[] = [];
  ordensEmAndamento: OrdemServico[] = [];
  ordensAguardandoPecas: OrdemServico[] = [];
  ordensFinalizadas: OrdemServico[] = [];
  
  constructor(private osService: OrdemServicoService) {}

  ngOnInit(): void {
    this.buscarOrdensDeServico();
  }

  buscarOrdensDeServico(): void {
    this.osService.listar().subscribe({
      next: (dados) => {        
        this.ordensOrcamento = dados.filter(os => os.status === 'ORCAMENTO');
        this.ordensEmAndamento = dados.filter(os => os.status === 'EM_ANDAMENTO');
        this.ordensAguardandoPecas = dados.filter(os => os.status === 'AGUARDANDO_PECAS');
        this.ordensFinalizadas = dados.filter(os => os.status === 'FINALIZADO');
      },
      error: (err) => alert('Erro ao carregar Ordens de Serviço.')
    });
  }

  abrirModalOS(): void {
    this.isModalVisible = true;
  }

  aoFecharModal(salvou: boolean): void {
    this.isModalVisible = false;
    if (salvou) {
      this.buscarOrdensDeServico(); 
    }
  }
}