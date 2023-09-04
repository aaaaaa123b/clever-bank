package org.example.service.impl;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.AllArgsConstructor;
import org.example.service.AccountStatementService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;

import static org.example.CleverBankApplication.SERVER_BASE;


@AllArgsConstructor
public class AccountStatementServiceImpl implements AccountStatementService {

    /**
     * Create extraxt.
     *
     * @param accountNumber number of account
     * @param startDate the date on which the statement begins
     * @param endDate the date on which the statement ends
     */
    @Override
    public void createExtract(String accountNumber, LocalDate startDate, LocalDate endDate) {

        final String message = """
                {
                    "number": "%s",
                    "startDate": "%s",
                    "endDate": "%s"
                }
                """.formatted(
                accountNumber,
                startDate.toString(),
                endDate.toString()
        );

        final HttpResponse<byte[]> response = Unirest.post(SERVER_BASE + "/api/v1/users/accounts/extract/")
                .header("Content-Type", "application/json")
                .body(message)
                .asBytes();

        if (response.isSuccess()) {
            String projectRoot = System.getProperty("user.dir");
            String targetFolderName = "account-statement";
            File targetFolder = new File(projectRoot, targetFolderName);
            if (!targetFolder.exists()) {
                targetFolder.mkdirs();
            }
            String fileName = "extract-%d.pdf".formatted(Instant.now().toEpochMilli());
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
