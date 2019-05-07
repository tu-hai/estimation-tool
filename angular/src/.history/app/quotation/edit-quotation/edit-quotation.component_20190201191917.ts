
import { Component, OnInit, ViewChild, ElementRef, EventEmitter, Output} from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { appModuleAnimation } from '@shared/animations/routerTransition';
import { QuotationService} from '@shared/service-proxies/service-proxies';
import { Validators, FormBuilder, FormGroup, FormControl, FormArray, NgForm } from '@angular/forms';
import {EditQuotation} from './../../../models/quotation.model'
import { from } from 'rxjs';


@Component({
  selector: 'app-edit-quotation',
  templateUrl: './edit-quotation.component.html',
  styleUrls: ['./edit-quotation.component.css']
})
export class EditQuotationComponent implements OnInit {
  active = false;
  workItemID: number;
  editForm:  FormGroup;
  listworkItem: FormArray;
  quoctationById: any;
  nhubui: any;
  get formData(): any { return this.editForm.get('wordItemDTOList'); }

  @ViewChild('editQuotationModel') modal: ModalDirective;
  @ViewChild('modalContent') modalContent: ElementRef;

  // tslint:disable-next-line:no-output-rename
  @Output('value') FV = new EventEmitter();


  constructor(
    public quotationservice: QuotationService,
    private formBuilder:     FormBuilder,
    ) {}

  ngOnInit() {
    this.editForm = this.formBuilder.group({
      id: [''],
      'wordItemDTOList': new FormArray([
          new FormGroup({
          'no': new FormControl(''),
          'codingErffort': new FormControl(''),
          'indayte': new FormControl(''),
          'assumptionNote': new FormControl(''),
          'assumptionConten': new FormControl(''),
          'taskName': new FormControl(''),
        })
      ]),
    })
  }

  delWorkItem(id: number): void {
    abp.message.confirm(
        'Delete Item of Quotation',
        (result: boolean) => {
            if (result) {
                this.quotationservice.delWorkItem(id).subscribe(data => {
                  // this.quoctationById = this.quoctationById.wordItemDTOList.filter(u => u.no !== id);
                  this.quoctationById = this.nhubui.filter(u => u.no !== id);
                  abp.notify.info('Deleted');
                })
            }
        }
    );
 }

  show(id: number): void {
    this.quotationservice.getQuoctationById(id).subscribe(data => {
      this.quoctationById = data
      this.nhubui = data.wordItemDTOList;
      this.quoctationById = Array.of(this.quoctationById);
    });
    this.active = true;
    this.modal.show();
  }

  close(): void {
    this.active = false;
    this.modal.hide();
  }

  onSubmit() {
    this.quotationservice.editQuotation(this.editForm.value)
      .subscribe(data => {
          this.FV.emit();
          abp.notify.info('Edit Successfully');
          this.close();
      });
  }

}
