package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jpa.Person;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by kentaro.maeda on 2016/10/06.
 */
@Singleton
public class PersonService {

    private final JPAApi jpa;

    @Inject
    public PersonService(JPAApi jpa) {
        this.jpa = jpa;
    }

    public void save(List<Person> persons) {
        EntityManager em = jpa.em();
        for(Person p : persons) {

            if (p.getAge() < 0) {
                throw new RuntimeException();
            }

            em.persist(p);

        }
    }

    public List<Person> all() {

        EntityManager em = jpa.em();
        return em.createQuery("select p from Person p", Person.class).getResultList();
    }

}
