package com.jyr.DailyLog.util;

public record YearWeek(int year, int week) implements Comparable<YearWeek> {
    @Override
    public int compareTo(YearWeek o) {
        int r = Integer.compare(this.year, o.year);
        if (r != 0) return r;
        return Integer.compare(this.week, o.week);
    }
}
