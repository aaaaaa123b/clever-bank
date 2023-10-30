package org.example.service.provider;

import lombok.experimental.UtilityClass;
import org.example.model.User;

@UtilityClass
public class UserProvider {

    public static User createExampleUser() {
        User user = new User();
        user.setId(1L);
        user.setLogin("abc");
        user.setFirstName("A");
        user.setLastName("B");
        user.setPatronymic("C");
        return user;
    }

}


