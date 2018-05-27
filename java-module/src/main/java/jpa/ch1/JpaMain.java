package jpa.ch1;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
        workWithTransaction(emf);
    }

    private static void workWithTransaction(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            logic(em);
            transaction.commit();
        } catch (Exception ex) {
            System.err.println(ex);
            transaction.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void logic(EntityManager em) {

        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("지한");
        member.setAge(3);
        em.persist(member);
        member.setAge(20);
        Member findMember = em.find(Member.class, id);
        System.out.println(findMember);

        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        System.out.println(members);
    }

}
