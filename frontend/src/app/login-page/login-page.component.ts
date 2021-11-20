import {Component} from '@angular/core';
import {HttpService} from "../Services/http.service";
import {ModalService} from "../Services/modal.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent {

  username = ''
  password = ''


  constructor(private http: HttpService, private modal: ModalService, private router: Router) {
  }

  get canSubmit() {
    return this.username.length > 0 && this.password.length >= 8
  }

  onSubmit() {
    if (this.username.length > 0 && this.password.length >= 8) {
      this.http.login(this.username, this.password)
        .then((jwt: string) => {
          localStorage.setItem("jwtToken", jwt)
          this.router.navigateByUrl("home")
        })
        .catch((error) => {
          console.log(error)
          this.modal.showAlertModal("Error:", "Username or password is wrong!")
        })
    } else {
      this.modal.showAlertModal("Error:", "Please fill in all fields and make sure your password is at least 8 characters long.")
    }
  }
}
