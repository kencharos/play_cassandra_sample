package controllers;

import com.google.inject.Inject;
import models.User;
import play.libs.Json;
import play.mvc.*;
import services.AuthenticationService;



import views.html.*;

public class AuthenticationController extends Controller {

    private AuthenticationService auth;

    @Inject
    public AuthenticationController(AuthenticationService auth) {
        this.auth = auth;
    }

    public Result index() {
        return ok(login.render());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result login() {
        User user = Json.fromJson(request().body().asJson(), User.class);

        return auth.doLogin(user.getId(), user.getPassword())
                .map(token -> Results.ok(token))
                .orElse(Results.badRequest("invalid id or password"));

    }
}
