import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Persistence;


/**
 * Test using Cassandra java ds driver without kundera.
 */
public class RawDSDriverTest {

    @Before
    public void before() {
        // for cassandra initialization.
        Persistence.createEntityManagerFactory("cassandra");

    }

    @Test
    public void testLightWeightTransaction() {

        Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        Session session = cluster.connect("\"KunderaExamples\"");

        String query ="INSERT INTO PERSON (id, name, age) values('%s', '%s', %d) IF NOT EXISTS";
        ResultSet rs1 = session.execute(String.format(query, "1", "name", 23));
        Row row1 = rs1.one();

        assertThat(row1.getBool("[applied]"), is(true));

        // insert same key recode.

        ResultSet rs2 = session.execute(String.format(query, "1", "name2", 43));
        Row row2 = rs2.one();

        assertThat(row2.getBool("[applied]"), is(false));
        // if applied is false. row includes current state.
        assertThat(row2.getString("id"), is("1"));
        assertThat(row2.getString("name"), is("name"));
        assertThat(row2.getInt("age"), is(23));


        session.close();
        cluster.close();

    }

}
