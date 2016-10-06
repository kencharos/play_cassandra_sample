import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.okhttp.*;
import com.thoughtworks.selenium.webdriven.commands.Uncheck;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import static play.test.Helpers.*;
import static org.junit.Assert.*;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ApplicationTest {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    String post(String url, int id){
        RequestBody body = RequestBody.create(JSON, "[{\"name\":\"name\", \"age\":29, \"id\":\""+ id +"\"}]");
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String res = response.body().string();
            System.out.println("GOT:" + res);
            return res;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    public void testParallel() {
        String url = "http://localhost:9000/persons/async2";
        int id = 500000;
        ExecutorService es = Executors.newFixedThreadPool(10);
        CompletableFuture.allOf(
                CompletableFuture.supplyAsync(() -> post(url, id+0), es),
                CompletableFuture.supplyAsync(() -> post(url, id+1),es),
                CompletableFuture.supplyAsync(() -> post(url, id+2),es),
                CompletableFuture.supplyAsync(() -> post(url, id+3),es),
                CompletableFuture.supplyAsync(() -> post(url, id+4),es)
        ).join();


    }


}
