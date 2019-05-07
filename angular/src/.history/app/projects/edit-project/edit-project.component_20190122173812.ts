import { Component, ViewChild, ElementRef, Output, OnInit, EventEmitter } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { ProjectService } from '@shared/service-proxies/service-proxies';
import { Project } from './../../../models/project'

@Component({
  selector: 'app-edit-project',
  templateUrl: './edit-project.component.html',
  styleUrls: ['./edit-project.component.css']
})
export class EditProjectComponent implements OnInit {
  // tslint:disable-next-line:no-output-rename
  @Output('value') FVe = new EventEmitter();

  @ViewChild('editProjectModal') modal: ModalDirective;
  @ViewChild('modalContent') modalContent: ElementRef;

  project: Project = null;
  active = false;
  editForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private projectService: ProjectService
  ) {

  }
  ngOnInit() {
    this.editForm = this.formBuilder.group({
      id: [''],
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

  show(id: number): void {
    this.projectService.getProjectById(id).subscribe(data => {
      this.project = data;
    });
    this.active = true;
    this.modal.show();
  }

  close(): void {
    this.active = false;
    this.modal.hide();
  }

  onSubmit() {
    this.projectService.editProject(this.editForm.value)
      .subscribe(data => {
        this.FVe.emit(data);
        abp.notify.info('Modifiled Successfully');
        this.close();
      });
  }
}
