package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    // 주문 저장
    public void save(Order order) {
        em.persist(order);
    }

    // 주문 조회
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    // 주문 검색 (jpql을 문자열로 직접 작성)
    // 하드코딩 수준으로 번거롭고 쿼리 오탈자에 주의해야 한다.
    public List<Order> findAllByString(OrderSearch orderSearch) {

        String jpql = "select o from Order o join o.member m";
        boolean isFirstCond = true;

        // select o from Order o
        // join o.member m
        // where o.orderStatus = :status
        // and m.name like :name

        // 주문 상태 조건 추가
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCond) {
                jpql += " where";
                isFirstCond = false;
            } else {
                jpql += " and";
            }
            jpql += " o.orderStatus = :status";
        }

        // 회원 이름 조건 추가
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCond) {
                jpql += " where";
                isFirstCond = false;
            }
            else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query.setParameter("status", orderSearch.getOrderStatus());
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

    // JPA Criteria 이용
    // 코드 유지보수성이 좋지 않음. 사실상 사용하지 않는다.
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);

        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);

        return query.getResultList();
    }

}
