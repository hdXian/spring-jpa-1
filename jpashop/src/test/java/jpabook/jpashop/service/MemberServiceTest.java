package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    // 회원 가입
    @Test
//    @Rollback(value = false)
    public void 회원가입() {
        // given
        Member member = new Member();
        member.setName("memberA");

        // when
        Long savedId = memberService.join(member);
        Member findMember = memberRepository.findOne(savedId);

        // then
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    // 중복 회원 예외
    @Test
    public void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("memberA");

        Member member2 = new Member();
        member2.setName("memberA");

        // when, then
        memberService.join(member1);
        Assertions.assertThatThrownBy(() -> memberService.join(member2))
                .isInstanceOf(IllegalStateException.class);
    }

}