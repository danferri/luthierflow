import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * Interface para RECEBER dados da API.
 * Baseada no seu InstrumentoResponseDTO.java
 */
export interface Instrumento {
  id: number;
  tipo: string;
  marca: string;
  modelo: string;
  numeroSerie: string;
  idCliente: number;
  nomeCliente: string; // Vem do DTO
}

/**
 * Interface para ENVIAR dados para a API.
 * Baseada no seu InstrumentoRequestDTO.java
 */
export interface InstrumentoRequest {
  tipo: string;
  marca: string;
  modelo: string;
  numeroSerie?: string;
  idCliente: number;
}


@Injectable({
  providedIn: 'root'
})
export class InstrumentoService {

  // Aponta para o seu InstrumentoController
  private apiUrl = 'http://localhost:8080/instrumentos'; 

  constructor(private http: HttpClient) {}

  /**
   * POST /instrumentos
   * Salva um novo instrumento
   */
  salvar(instrumento: InstrumentoRequest): Observable<Instrumento> {  
    return this.http.post<Instrumento>(this.apiUrl, instrumento);
  }
  
  /**
   * GET /instrumentos/{id}
   * Busca um instrumento pelo seu ID (para o modal de edição)
   */
  buscarPorId(id: number): Observable<Instrumento> {    
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<Instrumento>(url);
  }

  /**
   * PUT /instrumentos/{id}
   * Atualiza um instrumento existente (para o modal de edição)
   */
  atualizar(id: number, instrumento: InstrumentoRequest): Observable<Instrumento> {
    const url = `${this.apiUrl}/${id}`;    
    return this.http.put<Instrumento>(url, instrumento);
  }

  /**
   * DELETE /instrumentos/{id}
   * Deleta (arquiva) um instrumento
   */
  deletar(id: number): Observable<void> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url);
  }  
}