import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {HomePageComponent} from "./home-page/home-page.component";
import {LoginPageComponent} from "./login-page/login-page.component";
import {RegistrationPageComponent} from "./registration-page/registration-page.component";
import {FriendListPageComponent} from "./friend-list-page/friend-list-page.component";

const routes: Routes = [
  {path: "", component: LoginPageComponent},
  {path: "home", component: HomePageComponent},
  {path: "register", component: RegistrationPageComponent},
  {path: "friendList", component: FriendListPageComponent}
]


@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {


}
