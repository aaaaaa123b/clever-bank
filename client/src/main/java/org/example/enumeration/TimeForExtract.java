package org.example.enumeration;

import java.util.Arrays;

public enum TimeForExtract {
    MONTH,YEAR,ALL;

    public static TimeForExtract byOrdinal(int ordinal) {

        return Arrays.stream(TimeForExtract.values())
                .filter(timeForExtract -> timeForExtract.ordinal() == ordinal - 1)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unexpected value: " + ordinal));
    }
}
