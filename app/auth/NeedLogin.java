package auth;

import play.mvc.Http;
import play.mvc.Security;

/**
 * Authentication example by Header.
 */
public class NeedLogin extends Security.Authenticator {

    /**
     * if null, not authrized.
     */
    @Override
    public String getUsername(Http.Context ctx) {

        // if you need request body, and content type is JSON, you get body As Json below.
        // ctx.request().body().asJson();

        // if "user_id" exits in Request Header, it is authenticated.
        String userId = ctx.request().getHeader("user_id");
        if (userId != null) {
            return userId; // ok
        } else {
            return null;
        }
    }

    @Override
    public play.mvc.Result onUnauthorized(Http.Context ctx) {
        return unauthorized("not login");
    }
}