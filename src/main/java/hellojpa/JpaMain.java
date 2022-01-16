package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //비영속
            Member member = new Member();
            member.setId(200L);
            member.setName("HelloJpa");

            //영속 상태
            em.persist(member);
            System.out.println("member = " + member);
            tx.commit();
        } catch(Exception e){
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();

    }
}

/**
 * em.persist()은 실행시 DB가 저장되는게 아니라 영속성컨텍스트에서 쓰기지연소에 저장되고
 * tx.commit()이 실행될때 쓰기지연소에있는 insert문이 실행된다.
 *
 * 수정은 entity의 set으로 변경된 사유가 있을경우 tx.commit() 실행시 변경된 히스토리를 파악하여 update 쿼리를 날린다.
 */