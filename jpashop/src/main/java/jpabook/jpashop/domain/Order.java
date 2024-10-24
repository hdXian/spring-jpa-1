package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // member_id 컬럼 기준으로 member, order 테이블 조인
    private Member member; // FK member_id

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id") // delivery_id 컬럼 기준으로 member, delivery 테이블 조인 (joinColumn을 박았다 -> 연관관계의 주인이다.)
    private Delivery delivery; // FK delivery_id

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) // enum 전략 -> 문자열로 할 것. 기본이 ordinal인데, 숫자 밀리면 문제 생김
    private OrderStatus orderStatus; // ENUM [ORDER, CANCEL] (주문, 취소)

    // 연관관계 메서드 (양방향 연관관계에 있는 엔티티들에 대한 업데이트를 한번에 수행하도록 하는 편의 메서드)
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItems(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

}
