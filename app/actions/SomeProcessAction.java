package actions;

import annotations.HogeProcess;
import annotations.SomeProcess;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Created by kentaro.maeda on 2016/10/24.
 */
public class SomeProcessAction extends Action<SomeProcess> {
    public CompletionStage<Result> call(Http.Context ctx) {

        System.out.println("SomeProcess before:");// before_action


        return delegate.call(ctx).thenApply(result -> {

            System.out.println("SomeProcess after:"); //after_action
            return result;
        });

    }
}
