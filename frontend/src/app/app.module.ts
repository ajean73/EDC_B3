import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { App } from './app';
import { ClientManagementComponent } from './components/client-management/client-management.component';


@NgModule({
  imports: [BrowserModule, FormsModule, HttpClientModule, App, ClientManagementComponent],
  providers: [],
  bootstrap: [App]
})
export class AppModule {}
