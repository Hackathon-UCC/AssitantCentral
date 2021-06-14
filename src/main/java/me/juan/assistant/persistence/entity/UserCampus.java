package me.juan.assistant.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users_campuses")
@Getter
@NoArgsConstructor
public class UserCampus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer id_user;
    private Integer id_campuses;

    @ManyToOne
    @JoinColumn(name = "id_user", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_campuses", insertable = false, updatable = false)
    private Campus campus;

    public UserCampus(Integer id_user, Integer id_campuses) {
        this.id_user = id_user;
        this.id_campuses = id_campuses;
    }
}
