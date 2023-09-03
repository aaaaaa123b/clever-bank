package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawRequestDto {
    private long accountId;
    private BigDecimal cash;
}
