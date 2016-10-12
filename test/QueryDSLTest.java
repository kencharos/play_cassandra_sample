

import com.impetus.kundera.KunderaException;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import models.Person;
import models.QPerson;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class QueryDSLTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    @Before
    public void before() {
        emf = Persistence.createEntityManagerFactory("cassandra");
        em = emf.createEntityManager();

        em.persist(p("1", "n1", 19));
        em.persist(p("2", "n2", 28));
        em.persist(p("3", "n3", 56));

        em.clear();
        em.close();

        em = emf.createEntityManager();

    }

    private Person p (String id, String name, int age) {
        Person p = new Person();
        p.setId(id);
        p.setName(name);
        p.setAge(age);
        return p;
    }

    @Test
    public void testSelecAll() {

        JPQLQuery query = new JPAQuery(em);
        List<Person> res = query.from(QPerson.person).list(QPerson.person);
        assertThat(res.size(), is(3));
    }

    @Test
    public void testSelecById() {
        QPerson p = QPerson.person;
        JPQLQuery query = new JPAQuery(em);
        Person res = query.from(p)
            .where(p.id.eq("2"))
            .uniqueResult(p);
        assertThat(res.getName(), is("n2"));
        assertThat(res.getAge(), is(28));
    }

    @Test
    public void testSelecByAgeRange() {
        QPerson p = QPerson.person;
        JPQLQuery query = new JPAQuery(em);
        List<Person> res = query.from(p)
                .where(p.age.between(26, 90))
                .list(p);
        assertThat(res.size(), is(2));
    }


    @Test(expected = KunderaException.class)
    public void testSelecByLike_thisRaiseException_because_likeNeedSecondaryIndex() {
        QPerson p = QPerson.person;
        JPQLQuery query = new JPAQuery(em);
        List<Person> res = query.from(p)
                .where(p.name.like("n%"))
                .list(p);
    }

}
