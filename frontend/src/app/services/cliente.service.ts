import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Instrumento } from './instrumento.service';

export interface Cliente {
  id: number;
  nome: string;
  email?: string;
  cpf: string;
  telefones: string[];
  cep?: string;
  rua?: string;
  cidade?: string;
  estado?: string;
}

@Injectable({
  providedIn: 'root'
})

export class ClienteService {
  
  private apiUrl = 'http://localhost:8080/clientes';

  constructor(private http: HttpClient) {}

  salvar(cliente: Partial<Cliente>): Observable<Cliente> {  
    return this.http.post<Cliente>(this.apiUrl, cliente);
  }
  
  listar(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<Cliente> {    
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<Cliente>(url);
  }

  atualizar(id: number, cliente: Partial<Cliente>): Observable<Cliente> {
    const url = `${this.apiUrl}/${id}`;    
    return this.http.put<Cliente>(url, cliente);
  }

  deletar(id: number): Observable<void> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url);
  }
  
  listarInstrumentosPorCliente(clienteId: number): Observable<Instrumento[]> {    
    const url = `${this.apiUrl}/${clienteId}/instrumentos`;
    return this.http.get<Instrumento[]>(url);
  }

}