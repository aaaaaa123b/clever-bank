package org.example.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Account {
    private long id;
    private int userId;
    private int bankId;
    private BigDecimal balance;
    private String currency;
    private String number;
    private Date createdDate;

}
