import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Cliente, ClienteService } from '../../services/cliente.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-cliente-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
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