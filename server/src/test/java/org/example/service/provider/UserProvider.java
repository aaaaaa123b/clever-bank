package org.example.service.provider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProvider {

    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;

    public User createExampleUser() {
        User user = new User();
        user.setId(1L);
        user.setLogin("abc");
        user.setFirstName("A");
        user.setLastName("B");
        user.setPatronymic("C");
        return user;
    }

}


