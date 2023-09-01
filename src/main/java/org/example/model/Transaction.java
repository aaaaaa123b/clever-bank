package org.example.model;

import lombok.Data;
import org.example.enumeration.TransactionType;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

@Data
public class Transaction {
    private int id;
    private Account senderAccount;
    private Account recipientAccount;
    private BigDecimal amount;
    private Time time;
    private Date date;
    private TransactionType type;

}
