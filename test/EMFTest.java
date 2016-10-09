import models.Person;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Created by kentaro.maeda on 2016/10/09.
 */
public class EMFTest {

    @Test
    public void testemf() {
       // Map<String, String> props = new HashMap<>();
       // props.put(CassandraConstants.CQL_VERSION, CassandraConstants.CQL_VERSION_3_0);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cassandra");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx =  em.getTransaction();
        tx.begin();

        Person p = new Person();
        p.setId("2");
        p.setAge(23);
        p.setName("name");

        em.persist(p);

       em.flush();
        em.clear();

        tx.commit();
        tx.begin();
        Person p2 = em.createQuery("select p from Person p", Person.class).getSingleResult();

        System.out.println(p2.getName());

        em.close();
       // emf.close();

    }
}
