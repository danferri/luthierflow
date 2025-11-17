import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Cliente, ClienteService } from '../../services/cliente.service';

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
  
  excluirCliente(clienteId: number, clienteNome: string): void {    
    const confirmacao = window.confirm(`Tem certeza que deseja excluir o cliente "${clienteNome}" (ID: ${clienteId})? Esta ação não pode ser desfeita.`);
    
    if (confirmacao) {      
      this.clienteService.deletar(clienteId).subscribe({
        next: () => {          
          this.clientes = this.clientes.filter(c => c.id !== clienteId);
          console.log(`Cliente ${clienteId} excluído com sucesso.`);          
        },
        error: (erro) => {          
          console.error(`Erro ao excluir cliente ${clienteId}:`, erro);
          alert(`Erro ao excluir cliente: ${erro.error?.message || erro.message || 'Erro desconhecido.'}`);         
        }
      });
    }
  }
}