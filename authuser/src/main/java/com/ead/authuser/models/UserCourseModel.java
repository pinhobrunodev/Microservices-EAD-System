package com.ead.authuser.models;


import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_USERS_COURSES")
public class UserCourseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id")
    private UserModel user;
    @Column(nullable = false)
    private UUID courseId;



}
