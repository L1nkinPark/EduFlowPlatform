package com.lms.backend.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sub_categories")
public class SubCategory implements Serializable {

	@Id
	@Column(name = "sub_category_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long subCategoryId;

	@NotBlank(message = "Sub Category name could not be blank")
	@Column(name = "sub_category_name")
	private String subCategoryName;
	
	@Column(name = "sub_category_description")
	private String subCategoryDescription;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	@NotNull(message = "message could not ne null")
	private Category category;
	
    @Column(name = "status")
    private boolean status;

	@OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Course> courses = new ArrayList<>();
}
