package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class MoneyStatementRequestDto {

    String number;
    String start;
    String end;

}
