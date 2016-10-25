package filters;

import akka.stream.Materializer;
import play.mvc.Filter;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;
import services.AuthenticationService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Function;


/**
 * apply http header authentication for all controller.
 *
 * and see Filters.java.
 *
 */
@Singleton
public class AuthenticationFilter extends Filter {

    private AuthenticationService auth;

    /**
     * @param mat This object is needed to handle streaming of requests
     * and responses.
     */
    @Inject
    public AuthenticationFilter(Materializer mat, AuthenticationService auth) {
        super(mat);
        this.auth  = auth;
    }

    @Override
    public CompletionStage<Result> apply(
        Function<RequestHeader, CompletionStage<Result>> next,
        RequestHeader requestHeader) {

        // excludes login path.
        if (requestHeader.uri().startsWith("/login") || requestHeader.uri().startsWith("/assets")) {
            return next.apply(requestHeader);
        }

        String token = requestHeader.getHeader("authorization");

        if (token == null) {
            return CompletableFuture.completedFuture(Results.unauthorized("need authorization token"));
        }

        if (!auth.isValidToken(token).isPresent()) {
            return CompletableFuture.completedFuture(Results.unauthorized("token invalid or expires"));
        }

        return next.apply(requestHeader);
    }

}
