package controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jpa.Person;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.PersonService;

import java.io.IOException;
import java.util.List;

/**
 */
@Singleton
public class DBTestController extends Controller {

    private PersonService service;

    @Inject
    public DBTestController(PersonService service) {
        this.service = service;
    }
    @play.db.jpa.Transactional
    public Result allPersons() {

        List<Person> list = service.all();

        return ok(Json.toJson(list));
    }

    @BodyParser.Of(BodyParser.Json.class)
    @play.db.jpa.Transactional
    public Result savePersons() throws IOException{

        List<Person> inputs = Json.mapper().readValue(request().body().asJson().traverse(), new TypeReference<List<Person>>() {});

        service.save(inputs);

        return ok("ok");
    }



}
