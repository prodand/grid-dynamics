import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from "@angular/forms";
import { HttpModule } from '@angular/http';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

import { RoutingModule } from "./app-routing.module";
import { ButtonModule, CalendarModule, DropdownModule, InputTextModule } from "primeng/primeng";

import { AppComp } from "./components/app.component";
import { MainPageComp } from "./pages/main-page.component";
import { UiGraphComp } from "./components/ui-graph.component";
import { ApiService } from "./services/api.service";

import { PriceIntervalComp } from "./components/price-interval.component";

import "rxjs/add/operator/toPromise";

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ButtonModule,
    CalendarModule,
    DropdownModule,
    InputTextModule,
    HttpModule,
    FormsModule,
    RoutingModule,
  ],
  declarations: [AppComp, MainPageComp, PriceIntervalComp, UiGraphComp],
  providers: [ApiService],
  bootstrap: [AppComp],
})
export class AppModule {
}
