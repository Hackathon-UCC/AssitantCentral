package me.juan.assistant.persistence.entity;

import lombok.Getter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "campuses")
@Getter
public class Campus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "displayname")
    private String displayName;
    private String domain;
    private boolean active;
    private Timestamp registered_at;

}
