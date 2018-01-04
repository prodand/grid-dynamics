import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import { Interval } from "../model/Interval";

declare var fabric: any;

@Component({
  selector: 'ui-graph',
  templateUrl: 'html/components/ui-graph.comp.html',
  styleUrls: ['css/components/ui-graph.comp.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class UiGraphComp implements OnChanges, AfterViewInit {

  @Input()
  intervals: Interval[] = [];
  @Input()
  canvasId: string;
  @ViewChild('wrapper') canvasWrapper;
  private fb: any;
  private canvas: any;

  constructor() {
  }

  ngAfterViewInit(): void {
    this.fb = fabric;
    this.canvas = new this.fb.Canvas(this.canvasId);
    this.canvas.setWidth(this.canvasWrapper.nativeElement.offsetWidth);
    this.redrawIntervals();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.fb !== undefined) {
      this.redrawIntervals();
    }
  }

  redrawIntervals() {
    let i = 0;
    const delta = 19;
    let max = new Date(0);
    let min = new Date();
    min.setDate(min.getDate() + 1);
    this.intervals.filter(item => !!item.start && !!item.end).forEach(interval => {
      if (interval.start < min) {
        min = interval.start;
      }
      if (interval.end > max) {
        max = interval.end;
      }
    });
    const minMinutes = this.calculateMinutesInDate(min);
    const deltaMinutes = this.calculateMinutesInDate(max) - minMinutes;
    this.canvas.setWidth(Math.max(this.canvasWrapper.nativeElement.offsetWidth, deltaMinutes));
    this.canvas.clear();
    this.intervals.filter(item => !!item.start && !!item.end).forEach(item => {
      this.drawLine(i * 38 + delta, minMinutes, item);
      i++;
    });
  }

  drawLine(height: number, xDelta: number, item: Interval) {
    this.canvas.add(new this.fb.Text(item.amount.toFixed(2), {
      fontSize: 12,
      left: 0,
      top: height,
    }));
    const fromX = this.calculateMinutesInDate(item.start) - xDelta;
    const toX = this.calculateMinutesInDate(item.end) - xDelta;
    this.canvas.add(new this.fb.Line([fromX, height, toX, height], {
      fill: '#dd1c15',
      stroke: '#dd1c15',
      hasControls: false,
      hasBorders: false,
      lockMovementX: true,
      lockMovementY: true,
      selectable: false,
      hoverCursor: 'default'
    }));
  }

  calculateMinutesInDate(date: Date) {
    return date.getHours() * 60 + date.getMinutes();
  }
}