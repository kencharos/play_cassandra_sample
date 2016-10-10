import models.Person;
import models.TxResult;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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


        em.flush();
        tx.commit();
        em.close();


        System.out.println(String.format(query, id, name, age) + " res;" + i);
        em = emf.createEntityManager();
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
