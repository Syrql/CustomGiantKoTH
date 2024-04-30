package fr.syrql.giantkoth.utils;

import java.util.concurrent.TimeUnit;

public class NumberUtils {

    public static int parseSeconds(String value) {
        if (isInteger(value)) return Math.abs(Integer.parseInt(value));
        if (value.equalsIgnoreCase("0s")) return 0;

        value = value.toLowerCase();
        int seconds = 0;

        for (TimeFormat format : TimeFormat.values()) {
            if (!value.contains(format.getTimeChar())) continue;

            String[] split = value.split(format.getTimeChar());
            if (!isInteger(split[0])) continue;

            seconds += (int) (Math.abs(Integer.parseInt(split[0])) * format.getSeconds());
            if (split.length > 1) value = split[1];
        }

        return seconds == 0 ? -1 : seconds;
    }

    public static boolean isInteger(String value) {
        try {

            if (Integer.parseInt(value) < 0) return false;

            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    enum TimeFormat {

        DAY("d", TimeUnit.DAYS.toSeconds(1L)),
        HOUR("h", TimeUnit.HOURS.toSeconds(1L)),
        MINUTE("m", TimeUnit.MINUTES.toSeconds(1L)),
        SECOND("s", 1L);

        private final String timeChar;
        private final long seconds;

        TimeFormat(String timeChar, long seconds) {
            this.timeChar = timeChar;
            this.seconds = seconds;
        }

        public String getTimeChar() {
            return timeChar;
        }

        public long getSeconds() {
            return seconds;
        }
    }
}


