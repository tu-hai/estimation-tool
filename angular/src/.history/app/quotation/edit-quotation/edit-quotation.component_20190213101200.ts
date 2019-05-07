
import { Component, OnInit, ViewChild, ElementRef, EventEmitter, Output} from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { QuotationService} from '@shared/service-proxies/service-proxies';
import { FormBuilder, FormGroup, FormControl, FormArray, AbstractControl } from '@angular/forms';

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

  @ViewChild('editQuotationModel') modal: ModalDirective;
  @ViewChild('modalContent') modalContent: ElementRef;

  // tslint:disable-next-line:no-output-rename
  @Output('value') FV = new EventEmitter();

  constructor(
    public quotationservice: QuotationService,
    private formBuilder:     FormBuilder,
    ) {}

  ngOnInit() {
    this.editForm = this.buildEditForm();
  }

  get wordItemsForm() {
    return this.editForm.get('wordItemDTOList') as FormArray
  }

 buildEditForm(data = { id: '', wordItemDTOList: [] } ): FormGroup {
    const wordItemDTOListForm = new FormArray(data.wordItemDTOList.map(item => this.buildWordItemForm(item)));
    return this.formBuilder.group({
      'id': new FormControl(data.id),
      'wordItemDTOList': wordItemDTOListForm
    });
 }

 buildWordItemForm(item: any): FormGroup {
   return new FormGroup({
    'no': new FormControl(item.no || ''),
    'codingErffort': new FormControl(item.codingErffort || ''),
    'indayte': new FormControl(item.indayte || ''),
    'assumptionNote': new FormControl(item.assumptionNote || ''),
    'assumptionConten': new FormControl(item.assumptionConten || ''),
    'taskName': new FormControl(item.taskName || ''),
   });
 }

  show(id: number): void {
    this.quotationservice.getQuoctationById(id).subscribe(data => {
      this.editForm = this.buildEditForm(data)
      this.active = true;
      this.modal.show();
    });
  }

  delWorkItem(): void {
  const id = this.editForm.get('no').value
    abp.message.confirm(
        'Delete Item of Quotation',
        (result: boolean) => {
            if (result) {
                this.quotationservice.delWorkItem(id).subscribe(data => {
                  const index = this.wordItemsForm.controls.findIndex(control => {
                    return control.value.id === id;
                  });
                  this.wordItemsForm.removeAt(index);
                  abp.notify.info('Deleted');
                })
            }
        }
    );
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
