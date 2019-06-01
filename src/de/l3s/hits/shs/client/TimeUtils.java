package de.l3s.hits.shs.client;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtils {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static int getTimestampWithoutMillis(long timestamp) {
        return Math.toIntExact(timestamp / 1000);
    }

    public static Date getDate(long timestamp) {
        return new Date(timestamp);
    }

    public static LocalDate getLocalDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String getFormattedDate(long timestamp) {
        return dateTimeFormatter.format(getLocalDate(timestamp));
    }
}
