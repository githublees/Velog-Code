package example.jpa.repository;

import example.jpa.domain.Member;
import example.jpa.domain.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findAllJoinFetch() {
        return em.createQuery("select m from Member m join fetch m.team", Member.class)
                .getResultList();
    }

    public void delete(Member member) {
        em.remove(member);
    }
}
