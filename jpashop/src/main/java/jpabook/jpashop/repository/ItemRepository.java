package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    // 상품 등록
    public void save(Item item) {
        if (item.getId() == null) { // id가 null이다 -> 신규 등록
            em.persist(item);
        }
        else { // id가 null이 아니다 -> 기존 아이템 수정 정도로 이해
            em.merge(item);
        }
    }

    // 상품 단건 조회
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    // 상품 전체 조회
    public List<Item> findAll() {
        String jpql = "select i from Item i";
        return em.createQuery(jpql, Item.class).getResultList();
    }

}
