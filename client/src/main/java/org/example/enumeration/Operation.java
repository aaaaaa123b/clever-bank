package org.example.enumeration;

import java.util.Arrays;

public enum Operation {
    BALANCE, DEPOSIT, WITHDRAW, TRANSFER, EXTRACT,STATEMENT;


    public static Operation byOrdinal(int ordinal) {

        return Arrays.stream(Operation.values())
                .filter(operation -> operation.ordinal() == ordinal - 1)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unexpected value: " + ordinal));
    }
}
