import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { OrdemServico } from './ordem-servico.service';

const UPLOADS_BASE_URL = 'http://localhost:8080/uploads/';

export interface FotoPortfolio {
  id: number;
  urlImagem: string;
  legenda: string;
  ordemExibicao: number;
  fullUrl?: string;
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

  constructor(private http: HttpClient) { }

  private mapProjeto(projeto: ProjetoPortfolio): ProjetoPortfolio {
    if (projeto.fotos) {
      projeto.fotos = projeto.fotos.map(foto => ({
        ...foto,
        fullUrl: `${UPLOADS_BASE_URL}${foto.urlImagem}`
      }));
    }
    return projeto;
  }

  listarTodos(): Observable<ProjetoPortfolio[]> {
    return this.http.get<ProjetoPortfolio[]>(this.apiUrl)
      .pipe(
        map(projetos => projetos.map(p => this.mapProjeto(p)))
      );
  }

  buscarPorId(id: number): Observable<ProjetoPortfolio> {
    return this.http.get<ProjetoPortfolio>(`${this.apiUrl}/${id}`)
      .pipe(
        map(projeto => this.mapProjeto(projeto))
      );
  }

  listarPublicados(): Observable<ProjetoPortfolio[]> {
    return this.http.get<ProjetoPortfolio[]>(`${this.apiUrl}/publico`)
      .pipe(
        map(projetos => projetos.map(p => this.mapProjeto(p)))
      );
  }

  atualizar(id: number, dados: ProjetoPortfolioUpdate): Observable<ProjetoPortfolio> {
    return this.http.put<ProjetoPortfolio>(`${this.apiUrl}/${id}`, dados)
      .pipe(
        map(projeto => this.mapProjeto(projeto))
      );
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  adicionarFoto(id: number, file: File, legenda: string): Observable<ProjetoPortfolio> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('legenda', legenda);
    formData.append('ordemExibicao', '0');

    return this.http.post<ProjetoPortfolio>(`${this.apiUrl}/${id}/fotos`, formData)
      .pipe(
        map(projeto => this.mapProjeto(projeto))
      );
  }

  removerFoto(projetoId: number, fotoId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${projetoId}/fotos/${fotoId}`);
  }

}