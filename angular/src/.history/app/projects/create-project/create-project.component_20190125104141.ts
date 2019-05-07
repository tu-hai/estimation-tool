import { Component, ViewChild, Injector, Output, EventEmitter, ElementRef, OnInit } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { ProjectService } from '../../../shared/service-proxies/service-proxies';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { DATE } from 'ngx-bootstrap/chronos/units/constants';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.css']
})
export class CreateProjectComponent implements OnInit {
  @ViewChild('createProjectModal') modal: ModalDirective;
  @ViewChild('modalContent') modalContent: ElementRef;
  // tslint:disable-next-line:no-output-rename
  @Output('value') FV = new EventEmitter();

  active = false;
  addForm: FormGroup;
  currentDate = new Date();
  ds = new Date();  // date start
  de = new Date(); // date end

  constructor(
    private formBuilder: FormBuilder,
    private projectService: ProjectService) {
  }
  ngOnInit() {
    this.addForm = this.formBuilder.group({
      name: [''],
      dateStrart: [''],
      dateEnd: [''],
      sizeId: new FormGroup( {
        id:  new FormControl ('')
      }),
      projecTypeId: new FormGroup( {
        id:  new FormControl ('')
      }),
    });
  }
  show(): void {
    this.active = true;
    this.modal.show();
  }
  close(): void {
    this.active = false;
    this.modal.hide();
    console.log('nono', this.addForm.value)
  }
  onSubmit() {
    if (this.de < this.ds) {
      abp.notify.error('Date End not invalid');
    } else {
      this.projectService.createProject(this.addForm.value)
      .subscribe(data => {
        this.FV.emit(data.sizeId.nameSize);
        abp.notify.info('Added Successfully');
        this.close();
      });
    }
  }
}
