import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PortfolioService, ProjetoPortfolio } from '../../services/portfolio.service';

@Component({
  selector: 'app-portfolio-public-list',
  standalone: true,
  imports: [CommonModule, RouterLink, DatePipe],
  templateUrl: './portfolio-public-list.html',
  styleUrls: ['./portfolio-public-list.scss']
})
export class PortfolioPublicListComponent implements OnInit {

  projetos: ProjetoPortfolio[] = [];

  constructor(private portfolioService: PortfolioService) {}

  ngOnInit(): void {    
    this.portfolioService.listarPublicados().subscribe({
      next: (dados) => this.projetos = dados,
      error: (err) => console.error('Erro ao carregar vitrine:', err)
    });
  }
}