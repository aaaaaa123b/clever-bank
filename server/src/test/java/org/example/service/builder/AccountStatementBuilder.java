package org.example.service.builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.Account;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
public class AccountStatementBuilder {

    private String bankName = "bank1";
    private String firstName = "b";
    private String lastName = "c";
    private String patronymic = "d";
    private String number="123456";
    private String currency="BYN";
    private Date createdDate = Date.valueOf("2023-10-29");
    private LocalTime startDate = LocalTime.ofSecondOfDay(LocalTime.now().getHour());
    private LocalDate endDate = LocalDate.now();
    private LocalDate nowDate = LocalDate.now();
    private LocalTime nowTime = LocalTime.now();
    private BigDecimal balance = BigDecimal.valueOf(500);

//    public AccountStatementBuilder buildStatement(){
//        Account account = new Account();
//        account.setCreatedDate(createdDate);
//        account.setBankId(1);
//        account.setUserId(1);
//
//        return new AccountStatementBuilder(bankName,firstName,lastName,patronymic,number,currency,createdDate,startDate,endDate,nowDate,nowTime,balance);
//    }


}
