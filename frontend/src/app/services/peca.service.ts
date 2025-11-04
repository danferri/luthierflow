import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Peca {
  id: number;
  nomePeca: string;
  modelo: string;
  fabricante: string;
  qtdEstoque: number;
  precoVenda: number;
}

export interface PecaRequest {
  nomePeca?: string;
  modelo?: string;
  fabricante?: string;
  qtdEstoque?: number;
  precoVenda?: number; 
}

@Injectable({
  providedIn: 'root'
})
export class PecaService {
  
  private apiUrl = 'http://localhost:8080/pecas';

  constructor(private http: HttpClient) {}

  salvar(peca: PecaRequest): Observable<Peca> {  
    return this.http.post<Peca>(this.apiUrl, peca);
  }
  
  listar(): Observable<Peca[]> {
    return this.http.get<Peca[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<Peca> {    
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<Peca>(url);
  }

  atualizar(id: number, peca: PecaRequest): Observable<Peca> {
    const url = `${this.apiUrl}/${id}`;    
    return this.http.put<Peca>(url, peca);
  }

  deletar(id: number): Observable<void> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url);
  }  
}