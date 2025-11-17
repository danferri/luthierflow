import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrdemServico } from './ordem-servico.service';

export interface FotoPortfolio {
  id: number;
  urlImagem: string;
  legenda: string;
  ordemExibicao: number;
}

export interface ProjetoPortfolio {
  id: number;
  tituloPublico: string;
  descricaoPublica: string;
  statusPublicacao: 'RASCUNHO' | 'PUBLICADO';
  dataPublicacao: string | null;
  ordemDeServico: OrdemServico;
  fotos: FotoPortfolio[];
}

export interface ProjetoPortfolioUpdate {
  tituloPublico: string;
  descricaoPublica: string;
  statusPublicacao: string;
}

@Injectable({
  providedIn: 'root'
})
export class PortfolioService {

  private apiUrl = 'http://localhost:8080/portfolio';

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<ProjetoPortfolio[]> {
    return this.http.get<ProjetoPortfolio[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<ProjetoPortfolio> {
    return this.http.get<ProjetoPortfolio>(`${this.apiUrl}/${id}`);
  }

  atualizar(id: number, dados: ProjetoPortfolioUpdate): Observable<ProjetoPortfolio> {
    return this.http.put<ProjetoPortfolio>(`${this.apiUrl}/${id}`, dados);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  adicionarFoto(id: number, file: File, legenda: string): Observable<ProjetoPortfolio> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('legenda', legenda);
    formData.append('ordemExibicao', '0');

    return this.http.post<ProjetoPortfolio>(`${this.apiUrl}/${id}/fotos`, formData);
  }
}