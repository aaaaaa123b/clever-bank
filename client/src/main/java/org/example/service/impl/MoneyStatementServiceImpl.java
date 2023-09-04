package org.example.service.impl;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.AllArgsConstructor;
import org.example.service.MoneyStatementService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;

import static org.example.CleverBankApplication.SERVER_BASE;

@AllArgsConstructor
public class MoneyStatementServiceImpl implements MoneyStatementService {
    /**
     * Create extract
     *
     * @param accountNumber number of account object
     */
    @Override
    public void createStatement(String accountNumber) {
        final String message = """
                {
                    "number": "%s"
                }
                """.formatted(
                accountNumber
        );

        final HttpResponse<byte[]> response = Unirest.post(SERVER_BASE + "/api/v1/users/accounts/money-statement/")
                .header("Content-Type", "application/json")
                .body(message)
                .asBytes();

        if (response.isSuccess()) {
            String projectRoot = System.getProperty("user.dir");
            String targetFolderName = "money-statement";
            File targetFolder = new File(projectRoot, targetFolderName);
            if (!targetFolder.exists()) {
                targetFolder.mkdirs();
            }
            String fileName = "money-statement-%d.pdf".formatted(Instant.now().toEpochMilli());
            File outputFile = new File(targetFolder, fileName);

            try (FileOutputStream writer = new FileOutputStream(outputFile)) {
                writer.write(response.getBody());
                return;
            } catch (IOException e) {
                throw new IllegalArgumentException();
            }
        }

        throw new IllegalArgumentException("Transfer was not performed!");
    }
    }

