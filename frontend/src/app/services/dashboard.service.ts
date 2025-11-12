import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DashboardStats {
  ordensEmAndamento: number;
  orcamentosParaAprovar: number;
  itensComEstoqueBaixo: number;
  totalFaturadoNoMes: number;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  
  private apiUrl = 'http://localhost:8080/dashboard/stats';

  constructor(private http: HttpClient) {}

  getStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(this.apiUrl);
  }
}