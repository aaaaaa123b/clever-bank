package org.example.dto.request;

import lombok.Data;

@Data
public class UserRequestDto {

    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
}
