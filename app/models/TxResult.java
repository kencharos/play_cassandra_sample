package models;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class TxResult {

    @Id
    public boolean applied;

    public boolean isApplied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }
}
