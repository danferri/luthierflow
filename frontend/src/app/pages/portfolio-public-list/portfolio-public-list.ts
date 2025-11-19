import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common'; // Importa o DatePipe
import { RouterLink } from '@angular/router';
import { PortfolioService, ProjetoPortfolio } from '../../services/portfolio.service';

@Component({
  selector: 'app-portfolio-public-list',
  standalone: true,
  imports: [CommonModule, RouterLink, DatePipe], // Adiciona DatePipe aos imports
  templateUrl: './portfolio-public-list.html',
  styleUrls: ['./portfolio-public-list.scss']
})
export class PortfolioPublicListComponent implements OnInit {

  projetos: ProjetoPortfolio[] = [];

  constructor(private portfolioService: PortfolioService) {}

  ngOnInit(): void {
    // Usa o método listarPublicados que adicionamos no serviço otimizado
    this.portfolioService.listarPublicados().subscribe({
      next: (dados) => this.projetos = dados,
      error: (err) => console.error('Erro ao carregar vitrine:', err)
    });
  }
}