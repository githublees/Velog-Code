프로젝트를 하다보면 가장 자주 들리는 문제이지 않나 싶다.

대충 어떤 건지는 알고 있었지만, 최근 다시 공부를 하면서 내가 너무 수박 겉핥기 식으로 알고 있다는 생각에 자세히 정리해보고자 한다.

>  

---
## 1. N+1 쿼리 문제란?

설명하기에 앞서 하나의 팀은 여러 멤버를 가질 수 있고 멤버는 하나의 팀을 가진다고 했을 때, 아래와 같은 코드로 표현할 수 있다.

```java
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
}
```
---
```java
@Entity
@Getter
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    protected Team() {
    }

    public Team(String name) {
        this.name = name;
    }

    public void addMember(Member member) {
        members.add(member);
        member.updateTeam(this);
    }
}
```
---
```java
 public List<String> findMembersTeamName() {
        System.out.println("============N+1=============");
        List<Member> members = memberRepository.findAll();
        System.out.println("============================");
        return members.stream()
                .map(x -> x.getTeam().getName())
                .collect(Collectors.toList());
}
```
여기서 위와 같이 Member에 대한 모든 데이터를 찾고 Member.team의 name을 호출하게 된다면 어떻게 될까?

당연하게도 team에 대한 정보를 호출하는 순간 쿼리가 날려져 N+1 문제가 발생하게 된다.
![](https://velog.velcdn.com/images/githublees/post/d72d33f0-6308-4838-89ee-b5bc2bcfcb93/image.png)
이렇게 연관 관계가 설정된 엔티티를 조회할 경우에 조회된 데이터(N) 만큼 연관된 조회 쿼리가 추가로 발생하는 것을 N+1 쿼리 문제라 한다.

만약 10만 명의 Member마다 team이 전부 다른데 각 Member의 team을 조회하게 된다면 10만 개의 쿼리가 보내지게 된다.

---

## 2. 해결 방안

### 2-1. Join fetch
먼저 join fetch 기능으로 간단하게 해결할 수 있다.
```java
public List<Member> findAllJoinFetch() {
        return em.createQuery("select m from Member m join fetch m.team", Member.class)
                .getResultList();
}
```
가져오고 싶은 연관된 엔티티를 한번에 합쳐서 가져오도록 쿼리를 보내는 방식이다.
![](https://velog.velcdn.com/images/githublees/post/a606aa71-ae0e-49e6-a9b9-f0dda88612ab/image.png)

#### 주의사항
다대일 관계에서는 위와 같이 그냥 join fetch 를 하면 되지만 만약 일대다 관계, 혹은 컬렉션 패치 조인을 하게 된다면 데이터가 뻥튀기 되는 **카테시안 곱**이 발생하게 된다.

```java
public List<Team> CollectionJoinFetch() {
        return em.createQuery("select t from Team t join fetch t.members", Team.class)
                .getResultList();
}
```
| ID(PK) | NAME |
|:------:|:----:|
| 1 | 팀 A |
<br>

| ID(PK) | TEAM_ID(FK) | NAME |
|:------:|:-----------:|:----:|
| 1 | 1 | 멤버1 |
| 2 | 1 | 멤버2 |
<br>

| ID(PK) | TEAM_NAME | ID(PK) |TEAM_ID(FK) | MEMBER_NAME |
|:------:|:---------:|:------:|:----------:|:-----------:|
| 1 | 팀A | 1 | 1 | 멤버1 |
| 2 | 팀A | 2 | 1 | 멤버2 |

위와 같이 조회를 하게된다면 각 team 마다 team.members 만큼의 데이터가 추가로 생성되기에 조심해야한다.

#### 해결방안
_**1. distinct 추가하기**_
```java
public List<Team> CollectionJoinFetch() {
        return em.createQuery("select distinct t from Team t join fetch t.members", Team.class)
                .getResultList();
}
```
* JSQL의 distinct를 적용하게 되는데 SQL distinct와는 다르다.
   * SQL의 distinct는 row의 값이 모두 일치해야 제거
   * JPQL에서 distinct를 하면 SQL distinct 기능 + 중복 엔티티 제거하는 기능
*  Hibernate 6.0부터는 HQL(JPQL의 구현체)에 DISTINCT가 자동 적용
   * https://github.com/hibernate/hibernate-orm/blob/6.0/migration-guide.adoc#distinct
   
<br>

_**2. 일대다 필드의 타입을 SET 으로 선언하기**_

```java
@OneToMany(cascade = CascadeType.ALL)
private Set<Member> members = new LinkedHashSet<>();
```
이 때, Set은 순서가 보장되지 않기때문에 LinkedHashSet을 사용한다.

### 2-2. @EntityGraph
이 부분은 Spring Data JPA 를 아직 배우지 않았기에 추후 작성하도록 하겠습니다.
