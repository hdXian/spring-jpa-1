package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("상품 주문")
    void order() {
        // given
        Member member = generateMember();

        // when
        Book book = generateBook("시골 JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order findOrder = orderRepository.findOne(orderId);

        assertThat(findOrder.getOrderStatus())
                .as("상품 주문 시 상태는 ORDER")
                .isEqualTo(OrderStatus.ORDER);

        assertThat(findOrder.getOrderItems().size()).as("주문한 상품 수가 정확히 하나여야 한다.").isEqualTo(1);
        assertThat(findOrder.getTotalPrice()).as("주문 가격은 가격 * 수량이다.").isEqualTo(book.getPrice() * orderCount);
        assertThat(book.getStockQuantity()).as("주문 수량만큼 재고가 줄어들어야 한다.").isEqualTo(8);
    }

    @Test
    @DisplayName("상품 주문 (재고수량 초과)")
    void overStock() {
        // given
        Member member = generateMember();
        Book book = generateBook("시골 JPA", 10000, 10);

        int orderCount = 11; // 재고보다 많은 주문량

        // when
        assertThatThrownBy(() -> orderService.order(member.getId(), book.getId(), orderCount))
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage("need more stock");

        // then
    }

    @Test
    @DisplayName("주문취소")
    void cancel() {
        // given
        Member member = generateMember();
        Book book = generateBook("시골 JPA", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId); // 수량 2개짜리 주문을 취소

        // then
        Order findOrder = orderRepository.findOne(orderId);

        assertThat(findOrder.getOrderStatus())
                .as("상품의 주문 상태는 취소")
                .isEqualTo(OrderStatus.CANCEL);

        assertThat(book.getStockQuantity())
                .as("주문이 취소된 상품은 재고가 다시 증가해야 한다.")
                .isEqualTo(10);
    }

    private Book generateBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member generateMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "한강로", "123-123'"));
        em.persist(member);
        return member;
    }


}