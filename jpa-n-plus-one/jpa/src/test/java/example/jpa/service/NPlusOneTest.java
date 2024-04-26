package example.jpa.service;

import example.jpa.domain.Member;
import example.jpa.domain.Team;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class NPlusOneTest {

    @Autowired private TeamService teamService;
    @Autowired private MemberService memberService;
    @Autowired EntityManager em;

    @AfterEach
    private void afterEach() {
        memberService.deleteAll();
        teamService.deleteAll();
    }

    @BeforeEach
    private void beforeEach() {
        for (int i = 0; i < 10; i++) {
            Team team = new Team("팀"+i);
            Member member1 = new Member("Lee" + i);
            Member member2 = new Member("Kim" + i);

            team.addMember(member1);
            team.addMember(member2);

            teamService.save(team);
        }
    }

    @Test
    public void N_plus_1_쿼리_발생() throws Exception {
        //given
        List<String> teamNames = memberService.findMembersTeamName();

        //when
        System.out.println("============================");

        //then
        assertThat(teamNames.size()).isEqualTo(20);
    }

    @Test
    public void Join_Fetch() throws Exception {
        //given
        List<Member> members = memberService.findMembersTeamNameJoinFetch();

        //when
        for (Member member : members) {
            System.out.println("member = " + member.getName() + " | team = " + member.getTeam().getName());
        }
        System.out.println("============================");

        //then
        assertThat(members.size()).isEqualTo(20);
    }

    @Test
    public void Collection_join_fetch() throws Exception {
        //given
        List<Team> teams = teamService.CollectionJoinFetch();

        //when
        System.out.println("하이버네이트 6 이상 부터는 distinct 자동 적용");
        System.out.println("============================");

        //then
        assertThat(teams.size()).isEqualTo(10);
    }
}