import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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

  // TODO: Adicionar métodos deletar() aqui futuramente
}