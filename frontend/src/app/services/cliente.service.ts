import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Cliente {
  id: number;
  nome: string;
  email?: string;
  cpf: string;
  telefone: string;
  // Add outros campos se necessário (cep, rua, etc.)
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

  // TODO: Adicionar métodos salvar(), atualizar(), deletar() aqui futuramente
}