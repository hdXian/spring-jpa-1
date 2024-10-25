package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext // DI by spring boot
    private EntityManager em;

//    @PersistenceUnit // able to DI EntityManagerFactory

    // 회원 등록
    public void save(Member member) {
        em.persist(member);
    }

    // 회원 조회
    public Member fineOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        String jpql = "select m from Member m";
        return em.createQuery(jpql, Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        String jpql = "select m from Member m where m.name = :name";
        return em.createQuery(jpql, Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
