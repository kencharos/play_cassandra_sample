package services;

import models.LoginStatus;
import models.User;

import java.util.Optional;

/**
 *
 */
public interface AuthenticationService {

    public Optional<String> doLogin(String id, String password);

    public Optional<LoginStatus> isValidToken(String token);

}
