package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JpaMain {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("Hellow");

            em.persist(member);
            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId()
            );
            System.out.println("findMember = " + findMember.getUsername());



            tx.commit();
        } catch(Exception e){
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();

    }

    private static void printMemberAndTeam(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team = " + team);


    }
}
/**
 * Chapter 2
 * ##단방향 연관관계
 *
 * 객체를 테이블에 맞추어 데이터 중심으로 모델링하면,
 * 협력관계를 만들 수 없다.
 *
 * - 테이블은 외래 키로 조인을 사용해서 연관된 테이블을 찾는다.
 * - 객체는 참조를 사용해서 연관된 객체를 찾는다.
 * - 테이블과 객체 사이에는 이런 큰 간격이 있다.
 *
 *
 * 객체와 테이블이 관계를 맺는 차이
 *
 * 객체 연관관계 = 2개
 * 회원 -> 팀 연관관계 1개 (단방향)
 * 팀 -> 회원 연관관계 1개 (단방향)
 *
 * 테이블 연관관계 = 1개
 * 회원 <-> 팀의 연관관계 1개(양방향)
 *
 * - 객체의 양방향 관계는 사실 양방향 관계가 아니라 서로 다른 단방향관계 2개다
 * - 객체를 양방향으로 참조하려면 단방향 연관관계를 2개 만들어야한다.
 *
 * - 테이블은 외래 키 하나로 두테이블의 연관관를 정리
 *
 * ## 연관관계의 주인
 *
 * ### 양방향 매핑 규칙
 *  - 객채의 두관계중 하나를 연관 관계의주인으로 지정
 *  - 연관관계의 주인많이 외래 키를 관리
 *  - 주인이 아닌쪽은 읽기만 가능
 *  - 주인은 mappedBy 속성 사용 X
 *  - 주인이 아니면 mappedBy 속성으로 주인 지정
 *

 *  ### 누구를 주인으로(mappedBy)?
 *  - 외래키가 있는 곳을 주인으로 정해라
 *  - Mmeber.team이 연관관계의 주인
 *
 * ### 양방향 매핑 정리
 * - 단방향 매핑만으로도 이미 연관관계 매핑은 완료
 * - 양방향 매핑은 반대 방향으로 조회(객체 그래프탐색) 기능이 추가된것 뿐
 * - JPQL에서 역방향으로 탐색할 일이 많음.
 * - 단방향 매핑을 잘 하고 양방향은 필요할 때 추가해도됨(테이블에 영향이 없음)
 *
 *
 *
 * # @MappedSuperclass
 * - 상속관계 매핑 X
 * - 엔티티X , 테이블 매핑 X
 * - 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공
 * - 조회, 검색 불가
 * - 직접 생성해서 사용할 일이 없으므로 추상클래스도 사용 권장
 *
 * - 테이블과 관계없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할
 * - 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을때 사용
 * - 참고 : @Entity 클래스는 엔티티나 @MappedSuperclass로 지정한 클래스만 상속가능
 *
 *
 * #프록시
 *  -프록시
 *   - 프록시 기초
 *     - em.find() vs em.getReference()
 *     - em.find() : 데이터베이스를 통해서 실제 엔티티 객체 조회
 *     - em.getreference(): 데이터베이스 조회를 미루는 가짜 (프록시) 엔티티객체 조회
 *   - 프록시 특징
 *     - 실제 클래스를 상속 받아서 만들어짐
 *     - 실제 클래스와 겉 모양이 같다.
 *     - 사용하는 입장에서는 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면됨
 *     - 프록시 객체는 실제 객체의 참조를 보관
 *     - 프록시 객체를 호출하면 프록시 객체는 실제 객체의 메소드호출
 *    - **프로시의 특징** 중요
 *      - 프록시 객체는 처음 사용할 떄 한 번만 초기화
 *      - 프록시 객체를 초기화 할 떄 , 프록시 객체가 실제 엔티티로 바뀌는것은 아님, 초기화되면 프록시 객체를 통해서 실제 엔티티에 접근가능
 *      - 프록시 객체는 원본 엔티티를 상속받음, 따라서 타입 체크시 주의해야함( ==비교 X , instacce of 로 체크해야함)
 *          - 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference() 를 호출해도 실제 엔티티 반환.
 *      - 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제 발생
 *    - 프록시 확인
 *      - 프록시 인스턴스의 초기화 여부
 *       -> PersistenceUnitUtil.isLoaded(Object Entity)
 *      - 프록시 클래스 확인 방법
 *       -> entity.getClass().getName() 출력
 *      - 프록시 강제 초기화
 *       -> org.hibernate.Hibernate.initiallize(entity);
 *      - 참고 : JPA표준은 강제 초기화 없음. , 강제 호출 member.getName();
 *
 *
 *  -즉시 로딩과 지연로딩
 *  -지연로딩 활용
 *  -영속성 전이 : CASCADE
 *  -고아 객체
 *  -영속성 전이 + 고아객체, 생명주기
 *  -실전예제 - 5. 연관관계 관리
 *
 *
 *
 * */


/**
 *
 * Chapter 1
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

/**
 * JPA 정리
 *
 * JPA에서 가장중요한 2가지
 * ★★★ 객체와 관계형 데이터베이스 매핑하기
 * ★★★ 영속성 컨텍스트
 * 영속성 컨텍스트의 이점
 * - 1차캐시
 * - 동일성보장
 * - 트랜잭션을 지원하는 쓰기지연
 * - 변경감지
 * - 지연로딩
 *
 */

