package com.dmytrop.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dmytrop.dto.PriceInterval;
import com.dmytrop.dto.PriceItem;
import com.dmytrop.dto.PriceType;
import com.dmytrop.services.PriceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("api/price")
public class PriceResource {

  private PriceService priceService;

  @Autowired
  public PriceResource(PriceService priceService) {
    this.priceService = priceService;
  }

  @RequestMapping(method = RequestMethod.POST, value = "overlap",
      consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity overlapPrices(@RequestBody List<PriceItem> prices) {
    List<PriceItem> customPrices = new LinkedList<>();
    List<PriceItem> defaultPrices = new LinkedList<>();
    prices.stream()
        .filter(item -> item.getEnd() != null && item.getStart() != null && item.getType() != null)
        .forEach(item -> {
          if (item.getType() == PriceType.CUSTOM) {
            customPrices.add(item);
          } else {
            defaultPrices.add(item);
          }
        });
    List<PriceInterval> customTimeline = priceService.overlap(customPrices);
    List<PriceInterval> defaultTimeline = priceService.overlap(defaultPrices);
    List<PriceInterval> intervals = priceService.mergeTimeLines(customTimeline, defaultTimeline);
    return ResponseEntity.ok(intervals);
  }

}
