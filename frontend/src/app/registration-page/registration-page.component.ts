import {Component} from '@angular/core';
import {HttpService} from "../Services/http.service";
import {ModalService} from "../Services/modal.service";
import {Router} from "@angular/router";
import {User} from "../domain/User";

@Component({
  selector: 'app-registration-page',
  templateUrl: './registration-page.component.html',
  styleUrls: ['./registration-page.component.css']
})
export class RegistrationPageComponent {

  username = ''
  password = ''
  passwordCheck = ''
  firstName = ''
  lastName = ''


  constructor(private http: HttpService, private modal: ModalService, private router: Router) {
  }

  onSubmit() {
    if (this.username.length > 0 && this.firstName.length > 0 && this.lastName.length > 0 && this.password.length >= 8) {
      if (this.password == this.passwordCheck) {
        this.http.register(this.username, this.password, this.firstName, this.lastName)
          .then((user: User) => {
            localStorage.setItem("userId", user.id)
            this.router.navigateByUrl("")
          })
          .catch((error) => {
            this.modal.showAlertModal("Error:", error.error.toString())
          })
      } else {
        this.modal.showAlertModal("Error:", "Password's do not match!")
      }
    } else {
      this.modal.showAlertModal("Error:", "Please fill in all fields and make sure your password is at least 8 characters long.")
    }
  }

}
