import {Component, OnInit} from '@angular/core';
import {HttpService} from "../Services/http.service";
import {User} from "../domain/User";
import {ModalService} from "../Services/modal.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {

  users: User[] = []
  filteredUsers: User[] = []
  query = ''
  pageNumber = 0

  constructor(private http: HttpService, private modal: ModalService, private router: Router) {
  }

  onSearch() {
    if (this.query == "") {
      this.pageNumber = 0
      this.http.getAllUsers(this.pageNumber, "blank")
        .then((users: User[]) => {
          this.users = users
          this.filteredUsers = this.users
        })
        .catch((error) => {
          console.log(error)
          this.modal.showAlertModal("Error:", error.error.toString())
        })
    } else {
      this.pageNumber = 0
      this.http.getAllUsers(this.pageNumber, this.query)
        .then((users: User[]) => {
          this.users = users
          this.filteredUsers = this.users
        })
        .catch((error) => {
          console.log(error)
          this.modal.showAlertModal("Error:", error.error.toString())
        })
    }
  }

  onPrev() {
    if (this.pageNumber != 0) {
      let temp = this.query;
      if (this.query == "")
        temp = "blank"
      this.http.getAllUsers(this.pageNumber--, temp)
        .then((users: User[]) => {
          this.users = users
          this.filteredUsers = this.users
        })
        .catch((error) => {
          console.log(error)
          this.modal.showAlertModal("Error:", error.error.toString())
        })
    } else {
      this.modal.showAlertModal("Error:", "You cannot go to a page less than 0")
    }
  }

  onNext() {
    if (this.users.length == 10) {
      let temp = this.query;
      if (this.query == "")
        temp = "blank"
      this.http.getAllUsers(this.pageNumber++, temp)
        .then((users: User[]) => {
          this.users = users
          this.filteredUsers = this.users
        })
        .catch((error) => {
          console.log(error)
          this.modal.showAlertModal("Error:", error.error.toString())
        })
    } else {
      this.modal.showAlertModal("Error:", "There are no more entries to this table")
    }
  }

  addFriend(user: User) {
    this.http.addFriend(localStorage.getItem("userId"), user.id, localStorage.getItem("jwtToken"))
      .catch((error) => {
        this.modal.showAlertModal("Error:", error.error)
      })
  }

  deleteAccount() {
    this.http.deleteAccount(localStorage.getItem("userId"), localStorage.getItem("jwtToken"))
      .then(() => {
        localStorage.clear()
        this.router.navigateByUrl("")
      })
      .catch((error) => {
        this.modal.showAlertModal("Error:", error.error)
      })
  }

  ngOnInit(): void {
    this.http.getAllUsers(0, "blank")
      .then((users: User[]) => {
        this.users = users
        this.filteredUsers = this.users
      })
      .catch((error) => {
        console.log(error)
        this.modal.showAlertModal("Error:", error.error.toString())
      })
  }

}
