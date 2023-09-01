package org.example.enumeration;

import java.util.Arrays;

public enum Login {
    ENTRANCE, REGISTRATION;

    public static Login byOrdinal(int ordinal) {

        return Arrays.stream(Login.values())
                .filter(operation -> operation.ordinal() == ordinal - 1)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unexpected value: " + ordinal));
    }
}
