package org.example.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.AllArgsConstructor;
import org.example.model.User;
import org.example.service.UserService;

import static org.example.CleverBankApplication.SERVER_BASE;

@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final ObjectMapper objectMapper;

    /**
     * Login.
     *
     * @param id user id
     * @return user object.
     */
    @Override
    public User login(Long id) {
        final String message = """
                {
                    "userId": %d
                }
                """.formatted(id);

        final HttpResponse<User> response = Unirest.post(SERVER_BASE + "/api/v1/auth/login/")
                .header("Content-Type", "application/json")
                .body(message)
                .asObject(User.class);

        if (response.isSuccess()) return response.getBody();

        throw new IllegalArgumentException("User does not exist!");
    }

    /**
     * Add user.
     *
     * @param firstName user firstname
     * @param lastName user lastname
     * @param patronymic user patronymic
     * @param login user login
     */
    @Override
    public void addUser(String firstName, String lastName, String patronymic, String login) {
        final String message = """
                {
                    "firstName": "%s",
                    "lastName": "%s",
                    "patronymic": "%s",
                    "login": "%s"
                }
                """.formatted(
                firstName,
                lastName,
                patronymic,
                login
        );

        final HttpResponse<User> response = Unirest.post(SERVER_BASE + "/api/v1/auth/signup/")
                .header("Content-Type", "application/json")
                .body(message)
                .asObject(User.class);

        if (response.isSuccess()) return;

        throw new IllegalArgumentException("User was not created!");
    }
}
