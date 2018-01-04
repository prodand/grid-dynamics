package com.dmytrop.services;

import com.google.common.collect.ImmutableList;

import com.dmytrop.dto.PriceInterval;
import com.dmytrop.dto.PriceItem;
import com.dmytrop.dto.PriceType;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PriceServicesTest {

  private PriceService priceService = new PriceService();

  @Test
  public void overlap_simpleOverlap() {
    List<PriceItem> data = ImmutableList.<PriceItem>builder()
        .add(
            new PriceItem()
                .setAmount(2.0)
                .setStart(createDate(12))
                .setEnd(createDate(15))
                .setType(PriceType.DEFAULT)
        )
        .add(
            new PriceItem()
                .setAmount(3.0)
                .setStart(createDate(13))
                .setEnd(createDate(16))
                .setType(PriceType.DEFAULT)
        )
        .add(
            new PriceItem()
                .setAmount(4.0)
                .setStart(createDate(14))
                .setEnd(createDate(17))
                .setType(PriceType.DEFAULT)
        )
        .build();
    List<PriceInterval> result = priceService.overlap(data);

    assertEquals(
        new PriceInterval(2.0, createDate(12), createDate(13)),
        result.get(0));
    assertEquals(
        new PriceInterval(3.0, createDate(13), createDate(14)),
        result.get(1));
    assertEquals(
        new PriceInterval(4.0, createDate(14), createDate(17)),
        result.get(2));
  }


  @Test
  public void overlap_emptyOrSingleListInput() {
    List<PriceItem> data = ImmutableList.of();
    List<PriceInterval> result = priceService.overlap(data);

    assertEquals(0, result.size());

    data = ImmutableList.of(
        new PriceItem()
            .setAmount(2.0)
            .setStart(createDate(12))
            .setEnd(createDate(18))
            .setType(PriceType.DEFAULT)
    );
    result = priceService.overlap(data);

    assertEquals(1, result.size());

    assertEquals(
        new PriceInterval(2.0, createDate(12), createDate(18)),
        result.get(0));
  }

  @Test
  public void overlap_oneIntervalIncludesAnother() {
    List<PriceItem> data = ImmutableList.<PriceItem>builder()
        .add(
            new PriceItem()
                .setAmount(2.0)
                .setStart(createDate(12))
                .setEnd(createDate(18))
                .setType(PriceType.DEFAULT)
        )
        .add(
            new PriceItem()
                .setAmount(3.0)
                .setStart(createDate(13))
                .setEnd(createDate(17))
                .setType(PriceType.DEFAULT)
        )
        .add(
            new PriceItem()
                .setAmount(4.0)
                .setStart(createDate(14))
                .setEnd(createDate(16))
                .setType(PriceType.DEFAULT)
        )
        .build();
    List<PriceInterval> result = priceService.overlap(data);

    assertEquals(
        new PriceInterval(2.0, createDate(12), createDate(13)),
        result.get(0));
    assertEquals(
        new PriceInterval(3.0, createDate(13), createDate(14)),
        result.get(1));
    assertEquals(
        new PriceInterval(4.0, createDate(14), createDate(16)),
        result.get(2));
    assertEquals(
        new PriceInterval(3.0, createDate(16), createDate(17)),
        result.get(3));
    assertEquals(
        new PriceInterval(2.0, createDate(17), createDate(18)),
        result.get(4));
  }

  @Test
  public void overlap_oneOverlapWithTwo() {
    List<PriceItem> data = ImmutableList.<PriceItem>builder()
        .add(
            new PriceItem()
                .setAmount(2.0)
                .setStart(createDate(12))
                .setEnd(createDate(19))
                .setType(PriceType.DEFAULT)
        )
        .add(
            new PriceItem()
                .setAmount(3.0)
                .setStart(createDate(13))
                .setEnd(createDate(16))
                .setType(PriceType.DEFAULT)
        )
        .add(
            new PriceItem()
                .setAmount(4.0)
                .setStart(createDate(14))
                .setEnd(createDate(15))
                .setType(PriceType.DEFAULT)
        )
        .add(
            new PriceItem()
                .setAmount(5.0)
                .setStart(createDate(17))
                .setEnd(createDate(20))
                .setType(PriceType.DEFAULT)
        )
        .build();
    List<PriceInterval> result = priceService.overlap(data);

    assertEquals(6, result.size());

    assertEquals(
        new PriceInterval(2.0, createDate(12), createDate(13)),
        result.get(0));
    assertEquals(
        new PriceInterval(3.0, createDate(13), createDate(14)),
        result.get(1));
    assertEquals(
        new PriceInterval(4.0, createDate(14), createDate(15)),
        result.get(2));
    assertEquals(
        new PriceInterval(3.0, createDate(15), createDate(16)),
        result.get(3));
    assertEquals(
        new PriceInterval(2.0, createDate(16), createDate(17)),
        result.get(4));
    assertEquals(
        new PriceInterval(5.0, createDate(17), createDate(20)),
        result.get(5));
  }

  @Test
  public void overlap_firstLogest() {
    List<PriceItem> data = ImmutableList.<PriceItem>builder()
        .add(
            new PriceItem()
                .setAmount(2.0)
                .setStart(createDate(12))
                .setEnd(createDate(21))
                .setType(PriceType.DEFAULT)
        )
        .add(
            new PriceItem()
                .setAmount(3.0)
                .setStart(createDate(13))
                .setEnd(createDate(16))
                .setType(PriceType.DEFAULT)
        )
        .add(
            new PriceItem()
                .setAmount(4.0)
                .setStart(createDate(14))
                .setEnd(createDate(15))
                .setType(PriceType.DEFAULT)
        )
        .add(
            new PriceItem()
                .setAmount(5.0)
                .setStart(createDate(17))
                .setEnd(createDate(20))
                .setType(PriceType.DEFAULT)
        )
        .build();
    List<PriceInterval> result = priceService.overlap(data);

    assertEquals(7, result.size());

    assertEquals(
        new PriceInterval(2.0, createDate(12), createDate(13)),
        result.get(0));
    assertEquals(
        new PriceInterval(3.0, createDate(13), createDate(14)),
        result.get(1));
    assertEquals(
        new PriceInterval(4.0, createDate(14), createDate(15)),
        result.get(2));
    assertEquals(
        new PriceInterval(3.0, createDate(15), createDate(16)),
        result.get(3));
    assertEquals(
        new PriceInterval(2.0, createDate(16), createDate(17)),
        result.get(4));
    assertEquals(
        new PriceInterval(5.0, createDate(17), createDate(20)),
        result.get(5));
    assertEquals(
        new PriceInterval(2.0, createDate(20), createDate(21)),
        result.get(6));
  }

  @Test
  public void overlap_withMinutes() {
    List<PriceItem> data = ImmutableList.<PriceItem>builder()
        .add(
            new PriceItem()
                .setAmount(2.0)
                .setStart(createDate(2, 0))
                .setEnd(createDate(3, 10))
                .setType(PriceType.DEFAULT)
        )
        .add(
            new PriceItem()
                .setAmount(3.0)
                .setStart(createDate(2, 40))
                .setEnd(createDate(4))
                .setType(PriceType.DEFAULT)
        )
        .add(
            new PriceItem()
                .setAmount(4.0)
                .setStart(createDate(2, 10))
                .setEnd(createDate(3, 55))
                .setType(PriceType.DEFAULT)
        )
        .add(
            new PriceItem()
                .setAmount(5.0)
                .setStart(createDate(2, 15))
                .setEnd(createDate(3, 55))
                .setType(PriceType.DEFAULT)
        )
        .build();
    List<PriceInterval> result = priceService.overlap(data);

    assertEquals(4, result.size());

    assertEquals(
        new PriceInterval(2.0, createDate(2), createDate(2, 10)),
        result.get(0));
    assertEquals(
        new PriceInterval(4.0, createDate(2, 10), createDate(2, 15)),
        result.get(1));
    assertEquals(
        new PriceInterval(5.0, createDate(2, 15), createDate(2, 40)),
        result.get(2));
    assertEquals(
        new PriceInterval(3.0, createDate(2, 40), createDate(4)),
        result.get(3));
  }

  @Test
  public void mergeTimeLines_defaultsWithSomeCustoms() {
    PriceInterval custom1 = new PriceInterval(2.0, createDate(2), createDate(3));
    PriceInterval custom2 = new PriceInterval(3.0, createDate(4), createDate(5));
    List<PriceInterval> custom = ImmutableList.<PriceInterval>builder()
        .add(custom1)
        .add(custom2)
        .build();

    PriceInterval def1 = new PriceInterval(4.0, createDate(1), createDate(2));
    PriceInterval def2 = new PriceInterval(5.0, createDate(3), createDate(4));
    List<PriceInterval> def = ImmutableList.<PriceInterval>builder()
        .add(def1)
        .add(def2)
        .build();

    List<PriceInterval> result = priceService.mergeTimeLines(custom, def);

    assertEquals(4, result.size());

    assertEquals(def1, result.get(0));
    assertEquals(custom1, result.get(1));
    assertEquals(def2, result.get(2));
    assertEquals(custom2, result.get(3));
  }

  @Test
  public void mergeTimeLines_fillGapsWithDefaults() {
    PriceInterval custom1 = new PriceInterval(2.0, createDate(1), createDate(4));
    PriceInterval custom2 = new PriceInterval(3.0, createDate(5), createDate(7));
    PriceInterval custom3 = new PriceInterval(6.0, createDate(8), createDate(22));
    List<PriceInterval> custom = ImmutableList.<PriceInterval>builder()
        .add(custom1)
        .add(custom2)
        .add(custom3)
        .build();

    PriceInterval def1 = new PriceInterval(4.0, createDate(2), createDate(6));
    PriceInterval def2 = new PriceInterval(5.0, createDate(6), createDate(21));
    List<PriceInterval> def = ImmutableList.<PriceInterval>builder()
        .add(def1)
        .add(def2)
        .build();

    List<PriceInterval> result = priceService.mergeTimeLines(custom, def);

    assertEquals(5, result.size());

    assertEquals(custom1, result.get(0));
    assertEquals(new PriceInterval(4.0, createDate(4), createDate(5)), result.get(1));
    assertEquals(custom2, result.get(2));
    assertEquals(new PriceInterval(5.0, createDate(7), createDate(8)), result.get(3));
    assertEquals(custom3, result.get(4));
  }

  @Test
  public void mergeTimeLines_oneCustom() {
    PriceInterval custom1 = new PriceInterval(2.0, createDate(2, 10), createDate(3, 50));
    List<PriceInterval> custom = ImmutableList.<PriceInterval>builder()
        .add(custom1)
        .build();

    PriceInterval def1 = new PriceInterval(4.0, createDate(2), createDate(2, 10));
    PriceInterval def2 = new PriceInterval(5.0, createDate(2, 10), createDate(2, 15));
    PriceInterval def3 = new PriceInterval(6.0, createDate(2, 15), createDate(2, 40));
    PriceInterval def4 = new PriceInterval(7.0, createDate(2, 40), createDate(4));
    List<PriceInterval> def = ImmutableList.<PriceInterval>builder()
        .add(def1)
        .add(def2)
        .add(def3)
        .add(def4)
        .build();

    List<PriceInterval> result = priceService.mergeTimeLines(custom, def);

    assertEquals(3, result.size());

    assertEquals(new PriceInterval(4.0, createDate(2), createDate(2, 10)), result.get(0));
    assertEquals(custom1,result.get(1));
    assertEquals(new PriceInterval(7.0, createDate(3, 50), createDate(4)), result.get(2));
  }

  @Test
  public void mergeTimeLines_defaultOnlyCustomEmpty() {
    List<PriceInterval> custom = ImmutableList.of();

    PriceInterval def1 = new PriceInterval(4.0, createDate(2), createDate(6));
    PriceInterval def2 = new PriceInterval(5.0, createDate(6), createDate(21));
    List<PriceInterval> def = ImmutableList.<PriceInterval>builder()
        .add(def1)
        .add(def2)
        .build();

    List<PriceInterval> result = priceService.mergeTimeLines(custom, def);

    assertEquals(2, result.size());

    assertEquals(new PriceInterval(4.0, createDate(2), createDate(6)), result.get(0));
    assertEquals(new PriceInterval(5.0, createDate(6), createDate(21)), result.get(1));
  }

  private Date createDate(int hour) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2017, Calendar.DECEMBER, 2, hour, 0, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  private Date createDate(int hour, int minutes) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2017, Calendar.DECEMBER, 2, hour, minutes, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }
}
