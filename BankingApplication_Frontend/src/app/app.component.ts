import {  HttpClientModule } from '@angular/common/http';
import { AfterViewInit, Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
declare var $: any; 

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements AfterViewInit {
  title = 'BankingApplication';
  ngAfterViewInit() {
    // Initialize Bootstrap tooltips
    $(function () {
      $('[data-bs-toggle="tooltip"]').tooltip();
    });
  }
}
