package org.example.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.AllArgsConstructor;
import org.example.model.Account;
import org.example.service.AccountService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;

import static org.example.CleverBankApplication.SERVER_BASE;

@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Override
    public Account withdrawCash(long accountId, BigDecimal cash) {
        final String message = """
                {
                    "accountId": "%s",
                    "cash": "%s"
                }
                """.formatted(
                accountId,
                cash
        );

        final HttpResponse<Account> response = Unirest.post(SERVER_BASE + "/api/v1/users/accounts/withdraw/")
                .header("Content-Type", "application/json")
                .body(message)
                .asObject(Account.class);

        if (response.isSuccess()) return response.getBody();

        throw new IllegalArgumentException("Deposit was not performed!");
    }

    @Override
    public Account addCash(long accountId, BigDecimal cash) {
        final String message = """
                {
                    "accountId": "%s",
                    "cash": "%s"
                }
                """.formatted(
                accountId,
                cash
        );

        final HttpResponse<Account> response = Unirest.post(SERVER_BASE + "/api/v1/users/accounts/deposit/")
                .header("Content-Type", "application/json")
                .body(message)
                .asObject(Account.class);

        if (response.isSuccess()) return response.getBody();

        throw new IllegalArgumentException("Transfer was not performed!");
    }

    @Override
    public void transfer(String source, String targer, BigDecimal cash) {
        final String message = """
                {
                    "number": "%s",
                    "numberRecipient": "%s",
                    "cash": "%s"
                }
                """.formatted(
                source,
                targer,
                cash
        );

        final HttpResponse<String> response = Unirest.post(SERVER_BASE + "/api/v1/users/accounts/transfer/")
                .header("Content-Type", "application/json")
                .body(message)
                .asString();

        if (response.isSuccess()) {
            String projectRoot = System.getProperty("user.dir");
            String targetFolderName = "check";
            File targetFolder = new File(projectRoot, targetFolderName);
            if (!targetFolder.exists()) {
                targetFolder.mkdirs();
            }
            String fileName = "check-%d.txt".formatted(Instant.now().toEpochMilli()); // Replace with your desired file name
            File outputFile = new File(targetFolder, fileName);

            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.append(response.getBody());
                return;
            } catch (IOException e) {
                throw new IllegalArgumentException();
            }
        }

        throw new IllegalArgumentException("Transfer was not performed!");
    }

    @Override
    public Account findById(long id) {
        final String message = """
                {
                    "accountId": "%d"
                }
                """.formatted(
                id
        );

        final HttpResponse<Account> response = Unirest.post(SERVER_BASE + "/api/v1/users/accounts/balance/")
                .header("Content-Type", "application/json")
                .body(message)
                .asObject(Account.class);

        if (response.isSuccess()) return response.getBody();

        throw new IllegalArgumentException("Transfer was not performed!");
    }
}
