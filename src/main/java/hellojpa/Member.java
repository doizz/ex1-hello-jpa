package hellojpa;

import javax.persistence.*;
import java.util.Date;

/**
 * @Table(name= "실제테이블명") 으로 table명을 지정할수있다.
 */
@Entity
@Table
public class Member {

    @Id
    private Long id;

    @Column(name="name")
    private String username;

    private int age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
 * ★★★운영 장비에는 절대 create, create-drop, update 사용하면 안된다.
 * 개발초기 단계에는 create 또는 update
 * 테스트 서버는 update 또는 validate
 * 스테이징과 운영서버는 validate 또는 none
 *
 * 매핑 어노테이션 정리
 *
 * @Column - 컬럼매핑
 *  → name - 필드와 매핑할 테이블의 컬럼 이름
 *  → insertable, updatable - 등록 ,변경 가능 여부(false일때는 값이 절대변하지않는다.)
 *  → nullable(DDL) - null값의 허용여부를 설정한다. false로 설정하면 DDL 생성시에 not null 제약조건이 붙는다.
 *  → unique(DDL) - @Table의 uniqueConstraints와 같지만 한 컬럼에 간단히 유니크 제약조건을 걸때 사용.
 *  → columnDefinition - 데이터베이스 컬럼정보를 직접 줄수있다. ex) varchar(100) default 'EMPTY'
 *  → length(DDL) - 문자길이제약조건, String 타입에만 사용한다.
 *  → precision, scale(DDL) - BigDecimal 타입에 사용한다. 정밀한 소수를 다룰때사용
 *
 * @Temporal - 날짜타입 매핑
 * @Eumerated - enum 타입 매핑
 * @Lob - BLOB, CLOB 매핑
 * @Transient - 특정필드를 컬럼에 제외
 */