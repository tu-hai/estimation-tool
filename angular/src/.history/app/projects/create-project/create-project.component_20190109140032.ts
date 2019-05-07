import { Component, ViewChild, Injector, Output, EventEmitter, ElementRef, OnInit } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { ProjectService } from '../../../shared/service-proxies/service-proxies';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.css']
})
export class CreateProjectComponent implements OnInit {
    @ViewChild('createCustumerModal') modal: ModalDirective;
    @ViewChild('modalContent') modalContent: ElementRef;

    active = false;

    constructor(
        private projectService: ProjectService
    ) { }

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

}