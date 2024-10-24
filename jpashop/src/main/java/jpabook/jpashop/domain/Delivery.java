package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery") // order 엔티티의 delivery 필드에 종속
    private Order order; // 어떤 주문에 대한 배송 정보인지

    @Embedded // 값 타입 -> 해당 클래스 필드들이 그냥 테이블 컬럼으로 들어감
    private Address address;

    @Enumerated(EnumType.STRING) // enum 전략 -> 문자열로 할 것. 기본이 ordinal인데, 숫자 밀리면 문제 생김
    private DeliveryStatus deliveryStatus; // ENUM [READY, COMP] (준비중, 배송중)

}
