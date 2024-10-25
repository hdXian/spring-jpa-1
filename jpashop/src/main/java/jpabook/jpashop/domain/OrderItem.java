package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@Getter @Setter
public class OrderItem {
    // 어떤 상품(Item)을 몇 개(count) 주문했는지, 그게 어떤 주문(Order)에 포함된건지 저장

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    // 생성 메서드 - 엔티티 생성에 대한 전체 로직을 처리
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();

        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);

        return orderItem;
    }

    // 비즈니스 로직
    // 주문 취소
    public void cancel() {
        this.getItem().addStock(this.count);
    }

    public int getTotalPrice() {
        return this.getOrderPrice() * this.getCount();
    }

}
