package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // 값 타입 -> 해당 클래스 필드들이 그냥 테이블 컬럼으로 들어감
    private Address address;

    @OneToMany(mappedBy = "member") // 연관관계 주도권이 없음. order에 의해 설정된 값들이 거울처럼 들어감.
    private List<Order> orders = new ArrayList<>();

}
