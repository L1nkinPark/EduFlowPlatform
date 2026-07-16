package com.lms.backend.service.impl;

import com.lms.backend.model.entity.Course;
import com.lms.backend.model.request.CourseRequest;
import com.lms.backend.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setCourseId("course1");
        course.setCourseName("Java Programming");
        course.setPrice(100.0);
        course.setStatus("Active");
    }

    @Test
    void testGetAllCourses() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> page = new PageImpl<>(Collections.singletonList(course));
        when(courseRepository.findAll(pageable)).thenReturn(page);

        Page<Course> result = courseService.getAllCourses(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(course, result.getContent().get(0));
        verify(courseRepository, times(1)).findAll(pageable);
    }

    @Test
    void testSearchCourse() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> page = new PageImpl<>(Collections.singletonList(course));
        when(courseRepository.searchCourse(pageable, "Java")).thenReturn(page);

        Page<Course> result = courseService.searchCourse(pageable, "Java");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(courseRepository, times(1)).searchCourse(pageable, "Java");
    }

    @Test
    void testGetCourseById_Found() {
        when(courseRepository.findById("course1")).thenReturn(Optional.of(course));

        Course result = courseService.getCourseById("course1");

        assertNotNull(result);
        assertEquals("course1", result.getCourseId());
        verify(courseRepository, times(1)).findById("course1");
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.findById("course2")).thenReturn(Optional.empty());

        Course result = courseService.getCourseById("course2");

        assertNull(result);
        verify(courseRepository, times(1)).findById("course2");
    }

    @Test
    void testSaveCourse_New() {
        CourseRequest request = new CourseRequest();
        request.setCourseName("New Course");
        request.setPrice(150.0);
        request.setStatus("Active");

        when(courseRepository.save(any(Course.class))).thenAnswer(inv -> inv.getArgument(0));

        Course result = courseService.saveCourse(request, null);

        assertNotNull(result);
        assertEquals("New Course", result.getCourseName());
        assertEquals(150.0, result.getPrice());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testSaveCourse_Update() {
        CourseRequest request = new CourseRequest();
        request.setCourseId("course1");
        request.setCourseName("Updated Course");
        request.setPrice(120.0);
        request.setStatus("Active");

        when(courseRepository.findById("course1")).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenAnswer(inv -> inv.getArgument(0));

        Course result = courseService.saveCourse(request, null);

        assertNotNull(result);
        assertEquals("course1", result.getCourseId());
        assertEquals("Updated Course", result.getCourseName());
        assertEquals(120.0, result.getPrice());
        verify(courseRepository, times(1)).findById("course1");
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testDeleteById_Success() {
        when(courseRepository.findById("course1")).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).deleteById("course1");

        boolean result = courseService.deleteById("course1");

        assertTrue(result);
        verify(courseRepository, times(1)).findById("course1");
        verify(courseRepository, times(1)).deleteById("course1");
    }

    @Test
    void testDeleteById_NotFound() {
        when(courseRepository.findById("course2")).thenReturn(Optional.empty());

        boolean result = courseService.deleteById("course2");

        assertFalse(result);
        verify(courseRepository, times(1)).findById("course2");
        verify(courseRepository, never()).deleteById(anyString());
    }
}
