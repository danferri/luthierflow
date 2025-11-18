import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PortfolioService, ProjetoPortfolio } from '../../services/portfolio.service';

@Component({
  selector: 'app-portfolio-admin-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './portfolio-admin-list.html',
  styleUrls: ['./portfolio-admin-list.scss']
})
export class PortfolioAdminListComponent implements OnInit {

  projetos: ProjetoPortfolio[] = [];

  constructor(private portfolioService: PortfolioService) {}

  ngOnInit(): void {
    this.carregarProjetos();
  }

  carregarProjetos(): void {
    this.portfolioService.listarTodos().subscribe({
      next: (dados) => this.projetos = dados,
      error: (err) => alert('Erro ao carregar portfólio.')
    });
  }

  excluirProjeto(projeto: ProjetoPortfolio): void {
    if(confirm(`Deseja excluir o projeto "${projeto.tituloPublico}"?`)) {
      this.portfolioService.deletar(projeto.id).subscribe({
        next: () => this.carregarProjetos(),
        error: (err) => alert('Erro ao excluir projeto.')
      });
    }
  }
}