import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PortfolioService, ProjetoPortfolio, ProjetoPortfolioUpdate } from '../../services/portfolio.service';

@Component({
  selector: 'app-portfolio-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './portfolio-form.html',
  styleUrls: ['./portfolio-form.scss']
})
export class PortfolioFormComponent implements OnInit {

  projeto: ProjetoPortfolio | null = null;
  form!: FormGroup;
  id: number | null = null;

  constructor(
    private fb: FormBuilder,
    private portfolioService: PortfolioService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.initForm();
    this.carregarProjeto();
  }

  initForm(): void {
    this.form = this.fb.group({
      tituloPublico: ['', Validators.required],
      descricaoPublica: ['', Validators.required],
      statusPublicacao: ['RASCUNHO', Validators.required]
    });
  }

  carregarProjeto(): void {
    if (this.id) {
      this.portfolioService.buscarPorId(this.id).subscribe({
        next: (dados) => {
          this.projeto = dados;
          this.form.patchValue(dados);
        },
        error: () => {
          alert('Erro ao carregar projeto.');
          this.router.navigate(['/portfolio']);
        }
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid || !this.id) return;

    const dados: ProjetoPortfolioUpdate = this.form.value;
    
    this.portfolioService.atualizar(this.id, dados).subscribe({
      next: () => {
        alert('Projeto atualizado com sucesso!');
        this.router.navigate(['/portfolio']);
      },
      error: () => alert('Erro ao salvar alterações.')
    });
  }
  
  onFileSelected(event: any, legenda: string): void {
    const file: File = event.target.files[0];
    
    if (file && this.id) {      
      this.portfolioService.adicionarFoto(this.id, file, legenda).subscribe({
        next: (projetoAtualizado) => {
          this.projeto = projetoAtualizado;
          alert('Foto enviada com sucesso!');
        },
        error: () => alert('Erro ao enviar foto. Verifique o tamanho ou formato.')
      });
    }
  }

  onRemoverFoto(fotoId: number): void {
    if (!this.id) return;

    if (confirm('Tem certeza que deseja apagar esta foto?')) {
      this.portfolioService.removerFoto(this.id, fotoId).subscribe({
        next: () => {
          // Remove a foto da lista localmente para atualizar a tela sem recarregar
          if (this.projeto) {
            this.projeto.fotos = this.projeto.fotos.filter(f => f.id !== fotoId);
          }
        },
        error: () => alert('Erro ao remover foto.')
      });
    }
  }

}