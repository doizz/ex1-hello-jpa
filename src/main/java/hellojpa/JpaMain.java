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

            Member member = em.find(Member.class, 150L);
            member.setName("AAAAA");

            em.detach(member);

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

/**
 * ###플러시
 * 영속성 컨텍스트의 변경내용을 데이터베이스에 반영
 *
 *  - 변경 감지
 *  - 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
 *  - 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송(등록,수정,삭제쿼리)
 *
 *  영속성 컨텍스트를 플러시하는 방법
 *  - em.flush() - 직접호출
 *  - 트랜잭션 커밋 - 플러시 자동호출
 *  - JPQL쿼리 실행 - 플러시 자동호출
 *
*/


/**
 * ### 준영속 상태
 *
 *  - 영속 -> 준영속
 *  - 영속 상태의 엔티티가 영속성 컨텍스트에서 분리
 *  - 영속성 컨텍스트가 제공하는 기능을 사용 못함
 *
 *  준영속 상태로 만드는 방법
 *  - em.detach(entity)
 *   특정 엔티티만 준영속 상태로 전환
 *   
 *  - em.clear()
 *   영속성 컨텍스트를 완전히 초기화
 *   
 *  - em.close()
 *   영속성 컨텍스트를 종료
 *  
 */