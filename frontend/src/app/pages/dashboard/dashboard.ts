import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common'; // Importamos CommonModule e CurrencyPipe
import { DashboardService, DashboardStats } from '../../services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, CurrencyPipe],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss']
})
export class DashboardComponent implements OnInit {
  
  stats: DashboardStats | null = null;

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.getStats().subscribe({
      next: (dados) => {
        this.stats = dados;
      },
      error: (err) => {
        console.error('Erro ao carregar estatísticas do dashboard', err);        
        this.stats = {
          ordensEmAndamento: 0,
          orcamentosParaAprovar: 0,
          itensComEstoqueBaixo: 0,
          totalFaturadoNoMes: 0
        };
      }
    });
  }
}