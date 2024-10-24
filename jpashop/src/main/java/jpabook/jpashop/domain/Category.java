package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable( name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id") )
    private List<Item> items = new ArrayList<>(); // 이 카테고리에 속하는 상품이 뭐가 있는지

    @ManyToOne
    @JoinColumn(name = "parent_id") // 셀프 조인 개념 (이 Category에서 정한 parent_id FK값에 따라 상대 Category의 child 값이 영향받음)
    private Category parent; // 이 카테고리의 상위 카테고리 (하나)

    @OneToMany(mappedBy = "parent") // 셀프 조인 개념 (다른 Category들의 parent 값에 따라 구성됨)
    private List<Category> child; // 이 카테고리의 하위 카테고리 (여러개)

}
