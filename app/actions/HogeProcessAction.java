package actions;

import annotations.HogeProcess;
import play.mvc.*;

import java.util.concurrent.CompletionStage;

/**
 * Created by kentaro.maeda on 2016/10/24.
 */
public class HogeProcessAction extends Action<HogeProcess> {
    public CompletionStage<Result> call(Http.Context ctx) {
        String annotainValue = configuration.value();

        System.out.println("HogeProcess before:" + annotainValue); // before_action

        // pass specific value to controller
        ctx.args.put("hoge_key", annotainValue);

        return delegate.call(ctx).thenApply(result -> {

            System.out.println("HogeProcess after:" + annotainValue); //after_action
            return result;
        });


    }
}
