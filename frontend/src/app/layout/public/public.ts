import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-public-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './public.html',
  styleUrls: ['./public.scss']
})
export class PublicComponent {}