import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Instrumento } from './instrumento.service';
import { Cliente } from './cliente.service';

export interface ItemServico {
  pecaId: number;
  nomePeca: string;
  modelo: string;
  precoVenda: number;
  quantidadeUsada: number;
}

export interface OrdemServico {
  id: number;
  tipoServico: string;
  dataEntrada: string;
  dataFinalizacao: string | null;
  status: string;
  descricaoProblema: string;
  diagnosticoServico: string;
  valorMaoDeObra: number;
  cliente: Cliente;
  instrumento: Instrumento | null;
  itens: ItemServico[];
}

export interface OrdemServicoCreateRequest {
  clienteId: number;
  instrumentoId?: number | null;
  tipoServico: string;
  descricaoProblema: string;
}

export interface OrdemServicoUpdateRequest {
  tipoServico?: string;
  descricaoProblema?: string;
  diagnosticoServico?: string;
  valorMaoDeObra?: number;
  status?: string;
}

export interface AdicionarPecaRequest {
  pecaId: number;
  quantidade: number;
}


@Injectable({
  providedIn: 'root'
})
export class OrdemServicoService {

  private apiUrl = 'http://localhost:8080/ordens-servico';

  constructor(private http: HttpClient) {}

  listar(): Observable<OrdemServico[]> {
    return this.http.get<OrdemServico[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<OrdemServico> {
    return this.http.get<OrdemServico>(`${this.apiUrl}/${id}`);
  }

  salvar(os: OrdemServicoCreateRequest): Observable<OrdemServico> {
    return this.http.post<OrdemServico>(this.apiUrl, os);
  }

  atualizar(id: number, os: OrdemServicoUpdateRequest): Observable<OrdemServico> {
    return this.http.put<OrdemServico>(`${this.apiUrl}/${id}`, os);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  adicionarPeca(osId: number, pecaRequest: AdicionarPecaRequest): Observable<OrdemServico> {
    return this.http.post<OrdemServico>(`${this.apiUrl}/${osId}/pecas`, pecaRequest);
  }

  removerPeca(osId: number, pecaId: number): Observable<OrdemServico> {
    return this.http.delete<OrdemServico>(`${this.apiUrl}/${osId}/pecas/${pecaId}`);
  }
}