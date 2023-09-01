package org.example.context;

import lombok.Getter;
import lombok.Setter;
import org.example.enumeration.ApplicationState;
import org.example.enumeration.Operation;
import org.example.model.User;

@Getter
@Setter
public class ApplicationContext {

    private User currentUser;
    private Operation currentOperation;
    private ApplicationState state = ApplicationState.USER_AUTHENTICATION;
}
