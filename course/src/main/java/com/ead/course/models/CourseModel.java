package com.ead.course.models;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "TB_COURSES")
public class CourseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID courseId;
    @Column(nullable = false, unique = true, length = 150)
    private String name;
    @Column(nullable = false, length = 250, columnDefinition = "TEXT")
    private String description;
    @Column
    private String imageUrl;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationDate;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime lastUpdateDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;
    @Column(nullable = false)
    private UUID userInstructor;

      /*

        EAGER : Load data ... ManyToOne Default (Eager)
        LAZY  : Only load data when use the data.  ( Recommended) .. OneToMany Default ( Lazy )

        SELECT -> 1 Query to load Course and another Query to load EACH Course Module ( BAD WAY =[ ) .... Use the predefined FetchType.
        JOIN ->  1 Query to load all Course and Modules data. ( Large SQL in one transaction )... Ignore Lazy... All Query's will be EAGER.
        SUBSELECT -> 1 Query to bring the Course Data  and 1 Query to  load Each course Module ( But is a "Clean Way") Use the predefined FetchType.
        Without FetchMode -> JPA DEFAULT = JOIN ( Using EAGER ) But ... if specifying the FetchType ... will be the specified.

     */

    // Specify how the access of serialization will be... /GET All Courses => Ignore the Modules
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<ModuleModel> modules;
}
