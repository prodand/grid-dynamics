package com.dmytrop.services;

import com.google.common.collect.ImmutableList;

import com.dmytrop.dto.PriceInterval;
import com.dmytrop.dto.PriceItem;
import com.dmytrop.dto.PriceType;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

@Service
public class PriceService {

  /**
   * Creates schedule not overlapping schedule from provided prices intervals.
   *
   * @param prices Unordered prices time intervals.
   * @return Resulted schedule.
   */
  public List<PriceInterval> overlap(List<PriceItem> prices) {
    if (prices.isEmpty()) {
      return ImmutableList.of();
    }
    if (prices.size() == 1) {
      PriceItem item = prices.get(0);
      return ImmutableList.of(new PriceInterval(item.getAmount(), item.getStart(), item.getEnd()));
    }
    TreeSet<PriceItem> timeLine = new TreeSet<>(Comparator.comparing(PriceItem::getStart));
    timeLine.addAll(prices);

    List<PriceInterval> result = new ArrayList<>();
    Deque<PriceItem> stack = new LinkedList<>();

    Iterator<PriceItem> it = timeLine.iterator();
    PriceItem current;
    PriceItem next = it.next();

    while (next != null || stack.size() > 0) {
      current = next;
      next = nextOrNull(it);
      while (next != null) {
        if (current.getEnd().before(next.getStart())) {
          result.add(new PriceInterval(current.getAmount(), current.getStart(), current.getEnd()));
          break;
        }
        stack.push(current);
        result.add(new PriceInterval(current.getAmount(), current.getStart(), next.getStart()));
        current = next;
        next = nextOrNull(it);
      }

      if (next == null) {
        result.add(new PriceInterval(current.getAmount(), current.getStart(), current.getEnd()));
      }

      while (stack.size() > 0) {
        PriceItem previous = stack.pop();
        if (current.getEnd().after(previous.getEnd())) {
          continue;
        }
        if (previous.getEnd().after(current.getEnd()) && next != null &&
            previous.getEnd().after(next.getStart())) {
          result.add(new PriceInterval(previous.getAmount(), current.getEnd(), next.getStart()));
          stack.push(previous);
          break;
        }
        if (previous.getEnd().after(current.getEnd())) {
          result.add(new PriceInterval(previous.getAmount(), current.getEnd(), previous.getEnd()));
          current = previous;
        }
      }
    }
    return result;
  }

  /**
   * Alternative implementation with one common loop.
   * @param prices Unordered prices time intervals.
   * @return Resulted schedule.
   */
  public List<PriceInterval> overlap_commonLoop(List<PriceItem> prices) {
    if (prices.isEmpty()) {
      return ImmutableList.of();
    }
    if (prices.size() == 1) {
      PriceItem item = prices.get(0);
      return ImmutableList.of(new PriceInterval(item.getAmount(), item.getStart(), item.getEnd()));
    }
    TreeSet<PriceItem> timeLine = new TreeSet<>(Comparator.comparing(PriceItem::getStart));
    timeLine.addAll(prices);

    List<PriceInterval> result = new ArrayList<>();
    Deque<PriceItem> stack = new LinkedList<>();

    Iterator<PriceItem> it = timeLine.iterator();
    PriceItem current = it.next(), next = it.next();
    PriceItem previous = null, top = null;

    while (next != null) {
      // Current overlaps next
      if (current.getEnd().compareTo(next.getStart()) >= 0) {
        stack.push(current);
        result.add(new PriceInterval(current.getAmount(), current.getStart(), next.getStart()));
        current = next;
        next = nextOrNull(it);
      } else if (previous == null && stack.size() == 0) {
        result.add(new PriceInterval(current.getAmount(), current.getStart(), current.getEnd()));
        current = next;
        next = nextOrNull(it);
      } else {
        if (previous == null) { // case 1: No overlaps, but some has unchecked ENDs of previous intervals
          result.add(new PriceInterval(current.getAmount(), current.getStart(), current.getEnd()));
          top = current;
          previous = stack.pop();
        } else if (previous.getEnd().after(top.getEnd()) && previous.getEnd().after(next.getStart())) {
          // case 2: one of previous intervals has END date later then 'next' start date
          result.add(new PriceInterval(previous.getAmount(), top.getEnd(), next.getStart()));
          stack.push(previous);
          current = next;
          next = nextOrNull(it);
          top = null;
          previous = null;
        } else if (stack.size() == 0) { // case 3: All previous intervals ENDs checked
          result.add(new PriceInterval(previous.getAmount(), top.getEnd(), previous.getEnd()));
          current = next;
          next = nextOrNull(it);
          top = null;
          previous = null;
        } else if (top.getEnd().compareTo(previous.getEnd()) >= 0) {
          // case 4: skipping cause END is overlapped
          previous = stack.pop();
        } else { // previous.getEnd().after(top.getEnd())
          // case 5: END between top and next
          result.add(new PriceInterval(previous.getAmount(), top.getEnd(), previous.getEnd()));
          top = previous;
          previous = stack.pop();
        }
      }
    }

    result.add(new PriceInterval(current.getAmount(), current.getStart(), current.getEnd()));

    while (stack.size() > 0) {
      previous = stack.pop();
      if (previous.getEnd().after(current.getEnd())) {
        result.add(new PriceInterval(previous.getAmount(), current.getEnd(), previous.getEnd()));
        current = previous;
      }
    }
    return result;
  }

  private PriceItem nextOrNull(Iterator<PriceItem> it) {
    return it.hasNext() ? it.next() : null;
  }

  /**
   * @param customTimeline Intervals with {@link PriceType#CUSTOM} prices
   * @param defaultTimeline Intervals with {@link PriceType#DEFAULT} prices
   */
  public List<PriceInterval> mergeTimeLines(List<PriceInterval> customTimeline,
      List<PriceInterval> defaultTimeline) {
    if (customTimeline.size() == 0) {
      return defaultTimeline;
    }
    if (defaultTimeline.size() == 0) {
      return customTimeline;
    }
    // for reading with O(1) complexity
    customTimeline = new ArrayList<>(customTimeline);
    defaultTimeline = new ArrayList<>(defaultTimeline);
    List<PriceInterval> result = new ArrayList<>(customTimeline.size() + defaultTimeline.size());
    int i = 0, j = 0;
    PriceInterval a;
    PriceInterval b;
    while (i < customTimeline.size() && j < defaultTimeline.size()) {
      a = customTimeline.get(i);
      b = defaultTimeline.get(j);
      if (a.getStart().compareTo(b.getStart()) <= 0) {
        result.add(a);
        i++;
      } else if (b.getEnd().before(a.getStart())) {
        result.add(b);
        j++;
      } else {
        PriceInterval last = result.size() > 0 ? result.get(result.size() - 1) : null;
        Date bStart =
            last != null && b.getStart().before(last.getEnd()) ? last.getEnd() : b.getStart();
        Date bEnd = b.getEnd().after(a.getStart()) ? a.getStart() : b.getEnd();
        result.add(new PriceInterval(b.getAmount(), bStart, bEnd));
        if (a.getEnd().before(b.getEnd())) {
          result.add(a);
          i++;
        } else {
          j++;
        }
      }
    }

    while (i < customTimeline.size()) {
      result.add(customTimeline.get(i++));
    }

    while (j < defaultTimeline.size()) {
      b = defaultTimeline.get(j);
      PriceInterval last = result.size() > 0 ? result.get(result.size() - 1) : null;
      if (last == null) {
        result.add(b);
      } else if (b.getEnd().after(last.getEnd())) {
        Date bStart =
            b.getStart().before(last.getEnd()) ? last.getEnd() : b.getStart();
        result.add(new PriceInterval(b.getAmount(), bStart, b.getEnd()));
      }
      j++;
    }
    return result;
  }

}
