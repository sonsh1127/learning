package jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class HibernateTransactionTemplate {

    private EntityManagerFactory emf;

    public HibernateTransactionTemplate(String persistenceUnitName) {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    public void workWithTransaction(HibernateTransactionCallback callback){

        EntityManager entityManager = emf.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            callback.doInTransaction(entityManager);

            transaction.commit();

        } catch (Exception ex) {
            System.err.println(ex);
            transaction.rollback();
            throw ex;
        } finally {
            entityManager.close();
        }
        emf.close();
    }
}
