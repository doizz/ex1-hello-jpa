package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Table(name= "실제테이블명") 으로 table명을 지정할수있다.
 */
@Entity
@Table
public class Member {

    @Id
    private Long id;
    private String name;
    private int age;
    
    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Member() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

/**
 *
 * 영속성 컨텍스트
 *  - JPA를 이해하는데 가장 중요한용어
 *  - "엔티티를 영구 저장하는 환경" 이라는 뜻
 *  - EntityManager.persist(entity) ->DB를 저장하는게아니라 entity를 영속성 컨텍스트에 저장하는것.
 *
 *=========================================================================================
 *  엔티티의 생명주기
 *  - 비영속(new/transient)
 *   -영속성컨텍스트와 전혀 관계가 없는 새로운상태
 *
 *  - 영속(managed)
 *   - 영속성 컨텍스트에 관리되는 상태
 *
 *  - 준영속 (detached)
 *   - 영속성 컨텍스트에 저장되었다가 분리된 상태
 *
 *  - 삭제(removed)
 *   -삭제된 상태
 *
 */

/**
 * 데이터 베이스 스키마 자동 생성
 *
 * - DDL을 애플리케이션 동작시점에 자동생성
 * - 테이블중심-> 객체중심
 * - 데이터베이스 방언을 활용해서 데이터베이스에 맞는 적절한 DDL 생성
 * - 이렇게 생성된 DDL은 개발에서만 사용
 * - 생성된 DDL은 운영서버에서는 사용하지 않거나 적절히 다듬은후 사용
 *
 * 옵션
 * 1. create - 기존테이블 삭제후 다시생성 (DROP + CREATE)
 * 2. create-drop - create와 같으나 종료시점에 테이블 DROP
 * 3. update - 변경분만 반영(운영 DB에는 사용 XXXX)
 * 4. validate 엔티티와 테이블이 정상 매핑되었는지만 확인
 * 5. none - 사용하지않음
 *
 */