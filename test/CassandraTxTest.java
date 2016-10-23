import models.Person;
import models.TxResult;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kentaro.maeda on 2016/10/10.
 */
public class CassandraTxTest {


    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("cassandra");

    private void insertOne(String id, String name, int age) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        Person p = new Person();
        p.setId(id);
        p.setAge(age);
        p.setName(name);
        em.persist(p);

        em.flush();
        tx.commit();
        em.close();
    }

    private void insertOneLWTx(String id, String name, int age) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        String query ="INSERT INTO PERSON (id, name, age) values('%s', '%s', %d) IF NOT EXISTS";
        int i = em.createNativeQuery(String.format(query, id, name, age), TxResult.class)
                .executeUpdate();

        // memo return value is always 0, so that I don't know CQL is success or fail.



        System.out.println(String.format(query, id, name, age) + " res;" + i);
        Person p = em.find(Person.class, id);
        if (p.getAge() == age && p.getName().equals(name)) {
            System.out.println("  this cql is success");
        } else {

            System.out.println("  this cql is fail");
        }


    }

    @Test
    public void parallelInsert() {

        ExecutorService es = Executors.newFixedThreadPool(10);

        // not happen UniquKeyConsistaint vaiolation in Cassandra.
        // need to use light weight transaction.
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> insertOne("1","n1",10), es),
                CompletableFuture.runAsync(() -> insertOne("1","n2",20), es),
                CompletableFuture.runAsync(() -> insertOne("1","n3",30), es),
                CompletableFuture.runAsync(() -> insertOne("1","n4",40), es)
        ).join();
    }

    @Test
    public void testSelectAfterInsertInTransaction() {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Person p = new Person();
        p.setId("id");
        p.setName("name");
        p.setAge(78);

        em.persist(p);

        em.flush();
        em.clear();
        List<Person> ps = em.createQuery("select p from Person p where p.id = :id")
                .setParameter("id", "id").getResultList();

        assertThat("When transaction begin, result is 0, When not transaction, result is 1", ps.size(), is(0));

        tx.commit();
        em.close();

    }

    @Test
    public  void lightWeightTransaction() {


        ExecutorService es = Executors.newFixedThreadPool(10);

        // not happen UniquKeyConsistaint vaiolation in Cassandra.
        // need to use light weight transaction.
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> insertOneLWTx("1","n1",10), es),
                CompletableFuture.runAsync(() -> insertOneLWTx("1","n2",20), es),
                CompletableFuture.runAsync(() -> insertOneLWTx("1","n3",30), es),
                CompletableFuture.runAsync(() -> insertOneLWTx("1","n4",40), es)
        ).join();
    }
}
