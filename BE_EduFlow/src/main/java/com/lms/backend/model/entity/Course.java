package com.lms.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "courses", indexes = {
    @Index(name = "idx_courses_sub_category", columnList = "sub_category_id"),
    @Index(name = "idx_courses_account", columnList = "account_id")
})
public class Course implements Serializable {
//
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "course_id", unique = true, nullable = false)
    private String courseId;

    @Column(name ="course_name", nullable = false)
    private String courseName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @Column(name = "image")
    private String image;

    @Column(name = "thumbnail")
    private String thumbnail;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
