package example.jpa.service;

import example.jpa.domain.Member;
import example.jpa.domain.Team;
import example.jpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void save(Member member) {
        memberRepository.save(member);
    }

    @Transactional
    public void deleteAll() {
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            memberRepository.delete(member);
        }
    }

    public List<String> findMembersTeamName() {
        System.out.println("============N+1=============");
        List<Member> members = memberRepository.findAll();
        System.out.println("============================");
        return members.stream()
                .map(x -> x.getTeam().getName())
                .collect(Collectors.toList());
    }

    public List<Member> findMembersTeamNameJoinFetch() {
        System.out.println("============N+1=============");
        return memberRepository.findAllJoinFetch();
    }
}
