package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ExctractRequestDto {

    String number;
    String startDate;
    String endDate;
}
