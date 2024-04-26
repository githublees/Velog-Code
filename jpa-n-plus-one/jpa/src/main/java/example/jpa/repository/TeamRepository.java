package example.jpa.repository;

import example.jpa.domain.Team;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamRepository {

    private final EntityManager em;

    public void save(Team team) {
        em.persist(team);
    }

    public List<Team> findAll() {
        return em.createQuery("select t from Team t", Team.class)
                .getResultList();
    }

    public List<Team> CollectionJoinFetch() {
        return em.createQuery("select t from Team t join fetch t.members", Team.class)
                .getResultList();
    }

    public void delete(Team team) {
        em.remove(team);
    }
}
