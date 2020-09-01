import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { SuncrafterinaSharedModule } from 'app/shared/shared.module';
import { SuncrafterinaCoreModule } from 'app/core/core.module';
import { SuncrafterinaAppRoutingModule } from './app-routing.module';
import { SuncrafterinaHomeModule } from './home/home.module';
import { SuncrafterinaEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    SuncrafterinaSharedModule,
    SuncrafterinaCoreModule,
    SuncrafterinaHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    SuncrafterinaEntityModule,
    SuncrafterinaAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class SuncrafterinaAppModule {}
