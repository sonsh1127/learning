package jpa.ch5;

import java.util.List;
import jpa.HibernateTransactionTemplate;
import org.junit.Test;

public class JpaMain {


    @Test
    public void testSave() {

        HibernateTransactionTemplate htt = new HibernateTransactionTemplate("jpa");
        htt.workWithTransaction(
                em -> {
                    Team team1 = new Team("team1", "팀1");
                    em.persist(team1);

                    Member member = new Member("member1", "회원");
                    member.setTeam(team1);
                    em.persist(member);

                    Member member2 = new Member("member2", "회원2");
                    member2.setTeam(team1);
                    em.persist(member2);
                }
        );
    }

    @Test
    public void biDirection() {
        HibernateTransactionTemplate htt = new HibernateTransactionTemplate("jpa");
        htt.workWithTransaction(
                em -> {
                    Team team = em.find(Team.class, "team1");
                    List<Member> members = team.getMembers();
                    members.forEach(System.out::println);
                }
        );
    }

}
