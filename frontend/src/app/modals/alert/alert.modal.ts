import {Component, OnInit} from '@angular/core';
import {NgbModalRef} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-alert',
  templateUrl: './alert.modal.html',
  styleUrls: ['./alert.modal.css']
})
export class AlertModal implements OnInit {

  modalRef?: NgbModalRef
  title = ""
  body = ""

  close() {
    this.modalRef?.close()
  }

  constructor() {
  }

  ngOnInit(): void {
  }

}
