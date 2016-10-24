package filters;

import akka.stream.Materializer;
import play.mvc.Filter;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Function;


/**
 * apply http header authentication for all controller.
 */
@Singleton
public class AuthenticationFilter extends Filter {

    /**
     * @param mat This object is needed to handle streaming of requests
     * and responses.
     */
    @Inject
    public AuthenticationFilter(Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(
        Function<RequestHeader, CompletionStage<Result>> next,
        RequestHeader requestHeader) {

        System.out.println("debug call filter.");

        String userId = requestHeader.getHeader("user_id");

        if (userId == null) {
            return CompletableFuture.completedFuture(Results.unauthorized("need user_id header"));
        }

        return next.apply(requestHeader).thenApply(
            result -> result.withHeader("X-ExampleFilter", "foo")
        );
    }

}
