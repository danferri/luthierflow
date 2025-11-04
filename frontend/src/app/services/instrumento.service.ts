import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Instrumento {
  id: number;
  tipo: string;
  marca: string;
  modelo: string;
  numeroSerie: string;
  idCliente: number;
  nomeCliente: string;
}

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

  private apiUrl = 'http://localhost:8080/instrumentos'; 

  constructor(private http: HttpClient) {}
  
  salvar(instrumento: InstrumentoRequest): Observable<Instrumento> {  
    return this.http.post<Instrumento>(this.apiUrl, instrumento);
  }
  
  buscarPorId(id: number): Observable<Instrumento> {    
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<Instrumento>(url);
  }

  atualizar(id: number, instrumento: InstrumentoRequest): Observable<Instrumento> {
    const url = `${this.apiUrl}/${id}`;    
    return this.http.put<Instrumento>(url, instrumento);
  }

  deletar(id: number): Observable<void> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url);
  }  
}