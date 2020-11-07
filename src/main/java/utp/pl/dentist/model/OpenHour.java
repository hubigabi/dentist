package utp.pl.dentist.model;

public enum OpenHour {
    OPENING_HOUR(9),
    CLOSING_HOUR(18);

    public final Integer hour;

    OpenHour(Integer hour) {
        this.hour = hour;
    }
}
