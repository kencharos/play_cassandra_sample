package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jpa.Person;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.util.List;

public interface PersonService {

    /*
    memo;
    There is 2 way to bind a implemention.
    1. Use @ImplmenetBy to interface. (Simple)
    2. Wriete bind() to Module#configure. (Flexible)
     */

    void save(List<Person> persons) ;


    void saveWithT(List<Person> persons) ;

    List<Person> all();

}
