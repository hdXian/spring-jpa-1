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

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성 메서드 - Order 엔티티 생성에 대한 전체 로직 처리
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();

        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(OrderStatus.ORDER); // 최초 주문 상태는 ORDER로 초기화
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    // 비즈니스 로직 (도메인 주도 설계)
    public void cancel() { // 주문 취소
        // 이미 배송 완료된 상품은 취소 불가 -> 예외 발생
        if (delivery.getDeliveryStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("배송 완료된 상품은 취소가 불가능합니다.");
        }

        // 주문 상태를 취소로 바꾸고, 모든 주문 상품에 대한 재고를 원복.
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }

    }

    // 조회 로직
    public int getTotalPrice() { // 전체 주문 가격 조회
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
