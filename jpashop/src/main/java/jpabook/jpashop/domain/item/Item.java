package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 싱글 테이블 전략
@DiscriminatorColumn(name = "dtype") // 싱글 테이블 전략을 쓸 경우 각 하위 클래스들을 구분할 컬럼 지정
@Getter @Setter
public class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items") // Category 클래스의 items 필드에 의해 매핑
    List<Category> categories = new ArrayList<>(); // 이 상품이 어느 카테고리에 속하는지

    // add business logic
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int tmp = this.stockQuantity - quantity;

        if(tmp < 0) {
            throw new NotEnoughStockException("need more stock");
        }

        this.stockQuantity = tmp;
    }

}
