package jpa.ch6;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
public class Album extends Item {

    private String artest;
    private String etc;

    public String getArtest() {
        return artest;
    }

    public void setArtest(String artest) {
        this.artest = artest;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }
}
