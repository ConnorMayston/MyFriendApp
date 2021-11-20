import {Component, OnInit} from '@angular/core';
import {User} from "../domain/User";
import {HttpService} from "../Services/http.service";
import {ModalService} from "../Services/modal.service";

@Component({
  selector: 'app-friend-list-page',
  templateUrl: './friend-list-page.component.html',
  styleUrls: ['./friend-list-page.component.css']
})
export class FriendListPageComponent implements OnInit {

  friends: User[] = []

  constructor(private http: HttpService, private modal: ModalService) {
  }

  deleteFriend(user: User) {
    this.http.removeFriend(localStorage.getItem("userId"), user.id, localStorage.getItem("jwtToken"))
      .then(() => {
        this.friends = this.friends.filter(friend => friend != user)
      })
      .catch((error) => {
        this.modal.showAlertModal("Error:", error.error.toString())
      })
  }

  ngOnInit(): void {
    this.http.getUserFriends(localStorage.getItem("userId"))
      .then((users: User[]) => {
        this.friends = users
      })
      .catch((error) => {
        this.modal.showAlertModal("Error:", error.error.toString())
      })
  }

}
