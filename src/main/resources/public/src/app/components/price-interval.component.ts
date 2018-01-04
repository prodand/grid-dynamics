import { Component, Input, OnInit } from '@angular/core';
import { PriceItem } from "../model/PriceItem";

@Component({
  selector: 'price-interval',
  templateUrl: 'html/components/price-interval.comp.html',
  styleUrls: ['css/components/price-interval.comp.css']
})

export class PriceIntervalComp implements OnInit {

  @Input()
  intervalData: PriceItem;
  @Input()
  editMode: boolean;
  @Input()
  isNew: boolean = true;
  types: { value: string, label: string }[] = [
    {value: null, label: "Select Type..."},
    {value: "DEFAULT", label: "Default"},
    {value: "CUSTOM", label: "Custom"}
  ];

  constructor() {
  }

  ngOnInit() {
    this.intervalData = this.intervalData || new PriceItem();
    this.intervalData.type = 'DEFAULT';
    this.editMode = !this.intervalData.amount && !this.intervalData.start &&
        !this.intervalData.end;
  }

  edit() {
    this.editMode = true;
  }

  save() {
    this.editMode = false;
  }
}