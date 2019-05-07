import { Component, ViewChild, Injector, Output, EventEmitter, ElementRef, OnInit } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
@Component({
  selector: 'app-edit-customers',
  templateUrl: './edit-customers.component.html',
  styleUrls: ['./edit-customers.component.css']
})
export class EditCustomersComponent implements OnInit {
  @ViewChild('editCustumerModal') modal: ModalDirective;
  @ViewChild('modalContent') modalContent: ElementRef;

  active = false;

    ngOnInit() {
    }

    show(): void {
     this.active = true;
     this.modal.show();
    }
    close(): void {
     this.active = false;
     this.modal.hide();
    }
    onShown(): void {
     $.AdminBSB.input.activate($(this.modalContent.nativeElement));
    }
    save(): void {}

}
