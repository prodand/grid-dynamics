import { Injectable } from "@angular/core";
import { Http } from "@angular/http";
import { Router } from "@angular/router";
import { Interval } from "../model/Interval";
import { PriceItem } from "../model/PriceItem";

@Injectable()
export class ApiService {

  static API_BASE: string = "api/";

  constructor(private http: Http, private router: Router) {
  }

  overlapPrices(prices: PriceItem[]): Promise<Interval[]> {
    return this.http.post(ApiService.API_BASE + "/price/overlap", prices)
        .toPromise()
        .then(resp => resp.json() as Interval[])
        .then(list => list.map(
            interval => {
              const value = new Interval();
              value.amount = interval.amount;
              value.start = new Date(interval.start);
              value.end = new Date(interval.end);
              return value;
            }
        ));
  }

}