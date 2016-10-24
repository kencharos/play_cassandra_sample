import play.*;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http.*;
import play.mvc.*;

import javax.inject.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/*
 * Example custom HttpErrorHandler.
 *
 * Notice. ErrorHandler must define at root package.
 */
@Singleton
public class ErrorHandler extends DefaultHttpErrorHandler {

    @Inject
    public ErrorHandler(Configuration configuration, Environment environment,
                        OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(configuration, environment, sourceMapper, routes);
    }

    @Override
    protected CompletionStage<Result> onNotFound(RequestHeader request, String message) {
        System.out.println("call handler");
        return CompletableFuture.completedFuture(Results.redirect("/"));
    }
}