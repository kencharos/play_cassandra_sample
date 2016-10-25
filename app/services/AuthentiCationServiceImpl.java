package services;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.LoginStatus;
import models.User;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JPAApi jpa;

    @Inject
    AuthenticationServiceImpl(JPAApi jpa) {
        this.jpa = jpa;
    }


    /**
     * TODO: this is example. This has no security. don't use in production code.
     */
    @Override
    public Optional<String> doLogin(String id, String password) {
        EntityManager em = jpa.em("default");

        // User user = em.find(User.class, id);
        // DUMMY user for test.
        User user = new User();
        user.setId("user");
        user.setPassword("user");

        if (user != null && password.equals(user.getPassword())) {

            String token = UUID.randomUUID().toString();


            // save login status. if this process is slow, use cache api.
            LoginStatus login = new LoginStatus();
            login.setToken(token);
            login.setLoginUser(user.getId());
            login.setExpires(after(new Date(System.currentTimeMillis()), 30));

            em.persist(login);

            return Optional.of(token);
        } else {
            return Optional.empty();
        }
    }


    @Override
    public Optional<LoginStatus> isValidToken(String token) {

        // if this process is slow, use cache api.
        EntityManager em = jpa.em("default");
        LoginStatus status = em.find(LoginStatus.class, token);

        if (status == null || status.getExpires().before(new Date(System.currentTimeMillis()))) {
            return Optional.empty();
        } else {
            return Optional.of(status);
        }
    }


    private Date after(Date date, int minuite) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, minuite);

        return cal.getTime();

    }
}
