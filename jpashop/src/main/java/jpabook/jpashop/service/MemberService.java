package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional // readonly = false (default)
    public Long join(Member member) {

        checkDuplicateName(member);

        memberRepository.save(member);
        return member.getId();
    }

    // 회원 가입이 동시에 들어올 경우 동시에 중복 검증을 통과할 가능성이 있음. -> DB에서 name 필드에 대해 unique 지정을 하는 것이 안전함.
    private void checkDuplicateName(Member member) {
        List<Member> result = memberRepository.findByName(member.getName());
        // 해당 이름을 가진 회원이 이미 존재하면 예외 발생
        if (!result.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 조회
    public Member findOne(Long id) {
        return memberRepository.fineOne(id);
    }

    // 전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }


}
