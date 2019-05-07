
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
  quoctationById: any;

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
      'id': new FormControl(''),
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
                  this.quoctationById.wordItemDTOList = this.quoctationById.wordItemDTOList.filter(u => {
                    return u.no !== id;
                  });
                  abp.notify.info('Deleted');
                })
            }
        }
    );
 }

  show(id: number): void {
    this.quotationservice.getQuoctationById(id).subscribe(data => {
      console.log('quotation data', data);
      this.quoctationById = data
      this.active = true;
      this.modal.show();
    });
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
