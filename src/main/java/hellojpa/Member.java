package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Member {

    @Id
    private Long id;
    private String name;

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

