package controllers;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Person;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.PersonService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

/**
 */
@Singleton
public class DBTestController extends Controller {

    private PersonService service;

    @Inject
    public DBTestController(PersonService service) {
        this.service = service;
    }
    //@play.db.jpa.Transactional
    public Result allPersons() {
        List<Person> list = service.all();

        return ok(Json.toJson(list));
    }

    @BodyParser.Of(BodyParser.Json.class)
    //@play.db.jpa.Transactional
    public Result savePersons() {
        System.out.println("Accept controller on " + Thread.currentThread().getName());
        List<Person> inputs =extract(request().body().asJson());

        service.save(inputs);

        return ok("ok");
    }

    // need to run Reqest on other thread.
    @Inject HttpExecutionContext ec;

    public CompletionStage<Result> savePersonsAsync() throws IOException{

        System.out.println("Accept controller async on " + Thread.currentThread().getName());
        return CompletableFuture.supplyAsync(() -> {
            List<Person> inputs =extract(request().body().asJson());
            service.saveWithT(inputs);
            return ok("ok");
        }, ec.current()); // ec.current need to use request() in Function.
    }

    /*
      memo:
      Result を返した場合でも、Playは内部的に Promis<Result>に変換するので、単純にCompletionStageを返す場合は、Resultを返す場合と同じ。
      CompletetionStateを返す場合は、サービスの呼び出しを別スレッドにしたり、WebServiceなどの非同期計算を合成したりする場合に、
      そのまま非同期計算として返すことで、コントローラ内での処理結果待ちのブロックを防ぐ狙いがある。
      また、別スレッドプールで実行する場合、 @Transactionalは使用できない。
     */

    // need to lookup custom threadpool.
    @Inject ActorSystem akka;


    public CompletionStage<Result> savePersonsAsyncInCustomPool() throws IOException{
        System.out.println("Accept controller async custom on " + Thread.currentThread().getName());
        Executor myExecutor = akka.dispatchers().lookup("my-context");

        // when use custom executor, dont use request() in function.
        // or wrap executor in HttpExecutionContext
        return CompletableFuture.supplyAsync(() -> {
            List<Person> inputs = extract(request().body().asJson());
            service.saveWithT(inputs);
            return ok("ok");
        }, new HttpExecutionContext(myExecutor).current());
    }

    private List<Person> extract(JsonNode input) {
        List<Person> inputs = null;
        try {
            inputs = Json.mapper().readValue(input.traverse(),
                    new TypeReference<List<Person>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  inputs;
    }


}
