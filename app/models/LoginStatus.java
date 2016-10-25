package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "login_status", schema ="KunderaExamples@cassandra")
public class LoginStatus {

    @Id
    private String token;

    @Column
    private String loginUser;

    @Column
    private Date expires;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginStatus that = (LoginStatus) o;

        return token.equals(that.token);

    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }
}
