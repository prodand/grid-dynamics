import { Component, OnInit } from '@angular/core';
import { PriceItem } from "../model/PriceItem";
import { ApiService } from "../services/api.service";
import { Interval } from "../model/Interval";

@Component({
  selector: 'main-page',
  templateUrl: 'html/pages/main-page.comp.html',
  styleUrls: ['css/pages/main-page.comp.css']
})

export class MainPageComp implements OnInit {

  intervals: PriceItem[] = [];
  overlappedIntervals: Interval[] = [];
  draftItem: PriceItem = new PriceItem();

  constructor(private api: ApiService) {
  }

  ngOnInit() {
    this.intervals = [this.draftItem];
  }

  addPriceInterval() {
    if (this.isItemInvalid(this.draftItem)) {
      return;
    }
    this.draftItem = new PriceItem();
    // Instead of tracking mutation we assign new instance to allow other components handle changes.
    const newIntervalsArray = this.intervals.slice();
    newIntervalsArray.push(this.draftItem);
    this.intervals = newIntervalsArray;
  }

  calculatePricesOverlaps() {
    this.api.overlapPrices(this.intervals)
        .then(result => {
          this.overlappedIntervals = result;
        })
  }

  private isItemInvalid(item: PriceItem) {
    return !item.amount || !item.start || !item.end || !item.type || item.start >= item.end;
  }
}