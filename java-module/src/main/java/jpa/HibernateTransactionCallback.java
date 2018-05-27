package jpa;

import javax.persistence.EntityManager;

public interface HibernateTransactionCallback {

    void doInTransaction(EntityManager em);

}
