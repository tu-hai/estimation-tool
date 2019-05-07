import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormArray } from '@angular/forms';

@Component({
  selector: 'app-demo',
  templateUrl: './demo.component.html',
  styleUrls: ['./demo.component.css']
})
export class DemoComponent implements OnInit {

  testForm: FormGroup;
  indexSelected = 0;

  get formData() { return this.testForm.get('array'); }

  ngOnInit() {

    // Create our form group, and feed it the form control,
    // in this case just a workout array
    this.testForm = new FormGroup({
      'array': new FormArray([
        // Instantiate one
        new FormGroup({
          'taskname': new FormControl(''),
          'codingEffort': new FormControl('')
        })
      ])
    });

  }

  addTask() {
    (<FormArray>this.testForm.get('array')).push(
      new FormGroup({
        'taskname': new FormControl(''),
        'codingEffort': new FormControl(''),
      })
    )
  }

  removeTask(index: number) {
    (<FormArray>this.testForm.get('array')).removeAt(index);
  }

  onSubmit() {
    console.log((<FormArray>this.testForm.get('array')).value);
  }



}
