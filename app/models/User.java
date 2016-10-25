package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 */

@Entity
@Table(name = "user", schema ="KunderaExamples@cassandra")
public class User {


    @Id
    private String id;


    // TODO : must encrypt in production.
    @Column
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
