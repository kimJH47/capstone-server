package capstone.server.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@NoArgsConstructor
@Entity
@Getter
public class User {

    @GeneratedValue
    @Id
    @Column(name = "user_id")
    private Long id;


    /**
     * 일대일관계 매핑 트레이드오프
     * 외래키를 어디다 두고 관리를 해야하는가..
     * 주체 : User 대상 :Profile 일 때
     *
     * User 에 외래키를 두면 JPA 매핑편함, User 만 조회해도 프로필 확인가능
     * 하지만 Profile 에 null 허(DB 입장에서는 곤란)
     *
     * Profile 에 외래키를 두면
     * 주테이블에서 대상테이들 관계를 일대다로 변경해도 쉽게 변경가능(pk를 alter 하면됨)하고 구조유지
     * 하지만 프록시 기능의 한계로 지연로딩으로 설정해도 항상 즉시 로딩(유저을 조회할때 profile 값의 유무를 확인하려면 profile 에서 user 유무를 조회해야한다)
     * 양방향 매핑을 무조건 해야함
     *
     * 결론
     * 주 테이블에 외래키 관리 하기로함(일대일 단방향), 유저와 프로필 간의 관계가 일대다로 될 가능성은 적다고 생각했고 null 에대해서는 프론트단이나 서비스로직에서 무조건적으로 프로필값이
     * 같이 채워지게 하면 된다고 생각.
    */
    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column
    private String nickName;

    @Builder
    public User(Profile profile, String email, String nickName) {
        this.profile = profile;
        this.email = email;
        this.nickName = nickName;
    }
}
