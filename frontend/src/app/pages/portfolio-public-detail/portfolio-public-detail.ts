import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PortfolioService, ProjetoPortfolio } from '../../services/portfolio.service';

@Component({
  selector: 'app-portfolio-public-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, DatePipe],
  templateUrl: './portfolio-public-detail.html',
  styleUrls: ['./portfolio-public-detail.scss']
})
export class PortfolioPublicDetailComponent implements OnInit {

  projeto: ProjetoPortfolio | null = null;

  constructor(
    private route: ActivatedRoute,
    private portfolioService: PortfolioService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.portfolioService.buscarPorId(id).subscribe({
        next: (dados) => this.projeto = dados,
        error: () => alert('Projeto não encontrado ou indisponível.')
      });
    }
  }
}