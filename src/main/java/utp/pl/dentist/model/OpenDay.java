package utp.pl.dentist.model;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

public enum OpenDay {

    OPEN_DAYS(DayOfWeek.MONDAY.getValue(), DayOfWeek.TUESDAY.getValue(),
            DayOfWeek.WEDNESDAY.getValue(), DayOfWeek.THURSDAY.getValue(),
            DayOfWeek.FRIDAY.getValue()),

    CLOSED_DAYS(DayOfWeek.SATURDAY.getValue(), DayOfWeek.SUNDAY.getValue());

    public final List<Integer> days;

    OpenDay(Integer... days) {
        this.days = Arrays.asList(days);
    }
}
