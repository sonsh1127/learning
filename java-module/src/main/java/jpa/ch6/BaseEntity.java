package jpa.ch6;

import java.util.Date;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity {

    private Date createDate;

    private Date lastModifiedDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
