package example.jpa.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    protected Member() {
    }

    public Member(String name) {
        this.name = name;
    }

    public void updateTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(getId(), member.getId()) && Objects.equals(getName(), member.getName()) && Objects.equals(getTeam(), member.getTeam());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getTeam());
    }
}
