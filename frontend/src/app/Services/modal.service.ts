import {Injectable} from '@angular/core';
import {NgbModal, NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {AlertModal} from "../modals/alert/alert.modal";

@Injectable({
  providedIn: 'root'
})
export class ModalService {

  constructor(private modal: NgbModal) {
  }

  showAlertModal(title: string, body: string) {
    const modalRef = this.modal.open(AlertModal)
    const instance = modalRef.componentInstance as AlertModal
    instance.modalRef = modalRef
    instance.title = title
    instance.body = body
  }
}
