package example.jpa.service;

import example.jpa.domain.Member;
import example.jpa.domain.Team;
import example.jpa.repository.MemberRepository;
import example.jpa.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public void save(Team team) {
        teamRepository.save(team);
    }

    @Transactional
    public void deleteAll() {
        List<Team> teams = teamRepository.findAll();
        for (Team team : teams) {
            teamRepository.delete(team);
        }
    }

    public List<Team> CollectionJoinFetch() {
        System.out.println("==========OneToMany===========");
        return teamRepository.CollectionJoinFetch();
    }
}
