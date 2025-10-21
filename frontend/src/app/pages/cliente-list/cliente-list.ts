import { Component, OnInit, Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface Cliente {
  id: number;
  nome: string;
  email?: string;
  cpf: String;
}

import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ClienteService {
  constructor(private http: HttpClient) {}

  listar(): Observable<Cliente[]> {
  const apiUrl = 'http://localhost:8080/clientes'; 
  return this.http.get<Cliente[]>(apiUrl);
  }
}

@Component({
  selector: 'app-cliente-list',
  standalone: true,
  imports: [CommonModule], 
  templateUrl: './cliente-list.html',
  styleUrls: ['./cliente-list.scss']
})
export class ClienteListComponent implements OnInit {
  
  clientes: Cliente[] = [];
  
  constructor(private clienteService: ClienteService) {}
  
  ngOnInit(): void {
    this.buscarClientes();
  }
  
  buscarClientes(): void {
    this.clienteService.listar() 
      .subscribe({ 
        next: (dados) => {          
          this.clientes = dados;
        },
        error: (erro) => {

          console.error('Erro ao buscar clientes:', erro);
          
        }
      });
  }
}