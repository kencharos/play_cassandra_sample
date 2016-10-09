package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import interceptor.ServiceWithTransaction;
import models.Person;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.util.List;

@Singleton
public class PersonServiceImpl implements PersonService{

    private JPAApi jpa;
    @Inject
    public PersonServiceImpl(JPAApi jpa) {
        this.jpa = jpa;
    }

    public void save(List<Person> persons) {

        EntityManager em = jpa.em();
        System.out.println("start  on " + Thread.currentThread().getName() );
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(Person p : persons) {

            if (p.getAge() < 0) {
                em.getTransaction().rollback();
                throw new RuntimeException();
            }

            em.persist(p);

        }
        System.out.println("end service");
    }

    @ServiceWithTransaction
    public void saveWithT(List<Person> persons) {
            save(persons);

    }

    public List<Person> all() {

        return jpa.em().createQuery("select p from Person p", Person.class).getResultList();
    }

}
