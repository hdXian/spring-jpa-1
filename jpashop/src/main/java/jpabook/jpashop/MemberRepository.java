package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

// based on JPA
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    // auto DI from spring boot
    @PersistenceContext
    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member); // set id by em
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

}
