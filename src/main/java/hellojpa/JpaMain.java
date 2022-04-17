package hellojpa;

import org.hibernate.metamodel.internal.MapMember;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {


            Member member = new Member();
            member.setUsername("hello");
            member.setHomeAddress(new Address("city","street","zipcode"));


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
 * - 프록시와 즉시로딩 주의
 *  - 가급적 지연 로딩만 사용 (특히 실무에서)
 *  - 즉시 로딩을 적용하면 예상하지 못한 SQL이 발샐한다.
 *  - 즉시 로딩은 JPQL에서 N+1 문제를일으킨다.
 *  - @ManyToOne , @OneToOne 은기본이 즉시로딩 ->LASY로 설정
 *  - @OnetoMany , @ManyToMany는 기본이 지연로딩
 *
 * - 지연로딩 활용
 *  - Member와 Team은 자주 함께 사용 -> 즉시 로딩
 *  - Member와 Order는 가끔사용 -> 지연로딩
 *  - Order와 Product는 자주함께 사용 -> 즉시로
 *
 * - 지연로딩 활용 - 실무
 *  - 모든 연관관계에세 지연로딩을 사용해라(중요)
 *  - 실무에서 즉시 로딩을 사용하지마라 (중요)
 *  - 즉시로딩은 상상하지 못한 쿼리가 나간다.
 *
 *
 * # 영속성 전이 :CASCADE
 * - 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들고 싶을 때
 * - 예 ) 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장.
 *
 * - 양속성 전이 : CASCADE - 주의!!!!
 *  - 영속성 전이는 연관관계를 매핑하는 것과 아무 연관이 없음.
 *  - 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할뿐
 *
 * - CASCADE의 종류
 *  - ALL : 모두 적용
 *  - PERSIST : 영속
 *  - REMOVE : 삭제
 *  - MERGE : 병합
 *  - REFRESH : REFRESH
 *  - DETACH : DETACH
 *
 * # 고아 객체
 *  - 고아 객체 제거 : 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제
 *  - orphanRemoval = true
 *  - Parent parent1 = em.find(Parent.class, id);
 *    parent1.getChildren().remove(0);
 *    //자식 엔티티를 컬렉션에서 제거
 * # 고아 객체 - 주의!!!
 * - 참조가 제거된 엔티티는 다른 곳에서 참조하지 않은 고아 객체로 보고 삭제하는 기능
 * - 참조하는 곳이 하나일때 사용해야함.
 * - 특정엔티티가 개인 소유 할 때 사용
 * - @OneToOne , @OneToMany만 가능
 * - 참고 : 개념적으로 부모를 제거하면 자식은 고아가 된다 따라서
 *   고아 객체 제거기능을 활성화 하면, 부모를 제거할 때 자식도 함께 제거된다.
 *   이것은 CascadeType.REMOVE 처럼 동작한다.
 *
 * # 영속성 전이 + 고아객체, 생명주기
 *  - CascadeType.ALL + orphanRemovel= true
 *  - 스스로 생명주기를 관리하는 엔티티는 em.persist()로 영속화 , em.remove()로 제거
 *  - 두 옵션을 모두 활성화 하면 부모 엔티티를 통해 자식의 생명주기를 관리할 수 있음.
 *  - 도메인 주도 설계의 Aggregate Root 개념을 구현할때 유용
 *
 * # 기본값 타입
 *  - JPA의 데이터 타입 분류
 *
 *  - 엔티티 타입
 *   - @Entity로 정의하는 객체
 *   - 데이터가 변해도 식별자로 지속해서 추적 가능
 *   - 예) 회원 엔티티의 키나 나이 값을 변경해도 식별자로 인식 가능
 *
 *  - 값 타입
 *   - int, Integer, String 처럼 단순히 값으로 사용하는 자바 기본 타입이나 객체
 *   - 식별자가 없고 값만 있으므로 변경시 추적 불가
 *   - 예) 숫자 100을 200으로 변경하면 완전히 다른값으로 대체
 *
 *  - 값 타입 분류
 *   - 기본값 타입
 *    - 자바 기본 타입 (int, double)
 *    - 래퍼 클래스(Integer , Long)
 *    - String
 *
 *  - 임베디드 타입(emvedded type , 복합 값 타입)
 *    - 새로운 값 타입을 직접 정의할수 있음.
 *    - JPA는 임베디드 타입 이라함.
 *    - 주로 기본 값 타입을 모아서 만들어서 복합 값 타입이라고도 함.
 *    - int , String 과 같은 값 타입
 *  - 임베디드 타입의 장점
 *   - 재사용
 *   - 높은 응집도
 *   - Perid.isWork()처럼 해당 값 타입만 사용하는 의미 있는 메소드를 만들 수 있음.
 *   - 임베디드 타입을 포함한 모든 값 타입은, 값 타입을 소유한 엔티티에 생명주기를 의존함.
 *
 *  - 임베디드 타입과 테이블 매핑
 *   - 임베디드 타입은 엔티티의 값일 뿐이다.
 *   - 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다.
 *   - 객체와 테이블을 아주 세밀하게 매핑하는것이 가능
 *   - 잘 설계한 ORM 애플리케이션은매핑한 테이블 수보다 클래스의 수가 더 많음.
 *
 *
 *
 *
 *   - 컬렉션 값 타입 (collection value type)
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

