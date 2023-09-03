package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequestDto {

    private String number;
    private String numberRecipient;
    private BigDecimal cash;
}
