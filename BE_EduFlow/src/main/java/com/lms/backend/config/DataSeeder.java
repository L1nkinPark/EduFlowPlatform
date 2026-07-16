package com.lms.backend.config;

import com.lms.backend.model.entity.*;
import com.lms.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @org.springframework.transaction.annotation.Transactional
    @Override
    public void run(String... args) throws Exception {
        // Fix existing courses with relative image paths
        try {
            courseRepository.findAll().forEach(course -> {
                boolean updated = false;
                if (course.getImage() != null && course.getImage().startsWith("globel/")) {
                    course.setImage("/img/" + course.getImage());
                    updated = true;
                }
                if (course.getThumbnail() != null && course.getThumbnail().startsWith("globel/")) {
                    course.setThumbnail("/img/" + course.getThumbnail());
                    updated = true;
                }
                if (updated) {
                    courseRepository.saveAndFlush(course);
                }
            });
        } catch (Exception ex) {
            System.err.println("Error fixing relative course image paths: " + ex.getMessage());
        }

        if (courseRepository.count() > 0) {
            System.out.println("Database already contains courses. Skipping seeder.");
            return;
        }

        System.out.println("Starting database seeding...");

        // 1. Create or Find Instructor Account
        Account instructor = accountRepository.findByUsername("instructor").orElse(null);
        if (instructor == null) {
            instructor = new Account();
            instructor.setUsername("instructor");
            instructor.setFullName("Lê Minh Hướng dẫn viên");
            instructor.setEmail("instructor@eduflow.com");
            instructor.setPassword(passwordEncoder.encode("123"));
            instructor.setRole("INSTRUCTOR");
            instructor.setStatus(true);
            instructor = accountRepository.save(instructor);
        }

        // 2. Create Categories & Subcategories
        Category designCat = new Category();
        designCat.setCategoryName("Design & Art");
        designCat.setCategoryDescription("Create stunning graphics and animations");
        designCat.setStatus(true);
        designCat = categoryRepository.save(designCat);

        SubCategory animSub = new SubCategory();
        animSub.setSubCategoryName("Animation");
        animSub.setSubCategoryDescription("2D and 3D animation courses");
        animSub.setCategory(designCat);
        animSub.setStatus(true);
        animSub = subCategoryRepository.save(animSub);

        Category techCat = new Category();
        techCat.setCategoryName("Technology");
        techCat.setCategoryDescription("Software engineering and coding bootcamp");
        techCat.setStatus(true);
        techCat = categoryRepository.save(techCat);

        SubCategory webSub = new SubCategory();
        webSub.setSubCategoryName("Web Development");
        webSub.setSubCategoryDescription("HTML, CSS, JS, React and Spring Boot");
        webSub.setCategory(techCat);
        webSub.setStatus(true);
        webSub = subCategoryRepository.save(webSub);

        Category langCat = new Category();
        langCat.setCategoryName("Languages");
        langCat.setCategoryDescription("Speak foreign languages fluently");
        langCat.setStatus(true);
        langCat = categoryRepository.save(langCat);

        SubCategory engSub = new SubCategory();
        engSub.setSubCategoryName("English Speaking");
        engSub.setSubCategoryDescription("Pronunciation, vocabulary, and grammar");
        engSub.setCategory(langCat);
        engSub.setStatus(true);
        engSub = subCategoryRepository.save(engSub);

        // 3. Create Course 1: Animation
        Course animationCourse = new Course();
        animationCourse.setCourseName("Bring Your Creations to Life (2D Animation)");
        animationCourse.setDescription("Học cách tạo chuyển động sinh động cho nhân vật 2D của bạn bằng các nguyên lý hoạt họa kinh điển. Khóa học thực tế từ cơ bản đến nâng cao.");
        animationCourse.setPrice(49.0);
        animationCourse.setStartDate(LocalDate.now());
        animationCourse.setEndDate(LocalDate.now().plusMonths(3));
        animationCourse.setStatus("ACTIVE");
        animationCourse.setSubCategory(animSub);
        animationCourse.setAccount(instructor);
        animationCourse.setImage("/img/globel/b1.jpg");
        animationCourse.setThumbnail("/img/globel/b1.jpg");

        // Chapters & Lessons for Course 1
        Chapter ch1 = new Chapter();
        ch1.setTitle("Chương 1: Giới thiệu và Thiết lập Workspace");
        ch1.setDescription("Làm quen với giao diện và cài đặt công cụ");
        ch1.setCourse(animationCourse);
        ch1.setStatus(true);

        Lesson l1 = new Lesson();
        l1.setTitle("Bài 1: Tổng quan về hoạt họa 2D");
        l1.setVideo("https://www.w3schools.com/html/mov_bbb.mp4");
        l1.setDuration(600);
        l1.setChapter(ch1);
        l1.setStatus(true);

        Lesson l2 = new Lesson();
        l2.setTitle("Bài 2: Thiết lập vẽ và timeline");
        l2.setVideo("https://www.w3schools.com/html/movie.mp4");
        l2.setDuration(900);
        l2.setChapter(ch1);
        l2.setStatus(true);

        ch1.setLessons(new ArrayList<>(Arrays.asList(l1, l2)));

        Chapter ch2 = new Chapter();
        ch2.setTitle("Chương 2: 12 Nguyên lý Hoạt họa Classic");
        ch2.setDescription("Tìm hiểu các kỹ thuật làm động nhân vật");
        ch2.setCourse(animationCourse);
        ch2.setStatus(true);

        Lesson l3 = new Lesson();
        l3.setTitle("Bài 3: Squash & Stretch (Co và Giãn)");
        l3.setVideo("https://www.w3schools.com/html/mov_bbb.mp4");
        l3.setDuration(1200);
        l3.setChapter(ch2);
        l3.setStatus(true);

        Lesson l4 = new Lesson();
        l4.setTitle("Bài 4: Timing & Spacing (Thời gian & Khoảng cách)");
        l4.setVideo("https://www.w3schools.com/html/movie.mp4");
        l4.setDuration(1500);
        l4.setChapter(ch2);
        l4.setStatus(true);

        ch2.setLessons(new ArrayList<>(Arrays.asList(l3, l4)));

        animationCourse.setChapters(new ArrayList<>(Arrays.asList(ch1, ch2)));
        courseRepository.save(animationCourse);

        // 4. Create Course 2: Web Dev
        Course webCourse = new Course();
        webCourse.setCourseName("Web Development Bootcamp: Zero to Hero");
        webCourse.setDescription("Trở thành lập trình viên Fullstack Web chuyên nghiệp. Học HTML, CSS, JavaScript, Spring Boot và ReactJS qua các dự án thực tế.");
        webCourse.setPrice(99.0);
        webCourse.setStartDate(LocalDate.now());
        webCourse.setEndDate(LocalDate.now().plusMonths(6));
        webCourse.setStatus("ACTIVE");
        webCourse.setSubCategory(webSub);
        webCourse.setAccount(instructor);
        webCourse.setImage("/img/globel/b2.jpg");
        webCourse.setThumbnail("/img/globel/b2.jpg");

        Chapter ch3 = new Chapter();
        ch3.setTitle("Chương 1: Khởi đầu với Frontend cơ bản");
        ch3.setDescription("Học giao diện trang web tĩnh");
        ch3.setCourse(webCourse);
        ch3.setStatus(true);

        Lesson l5 = new Lesson();
        l5.setTitle("Bài 1: HTML5 cấu trúc trang web");
        l5.setVideo("https://www.w3schools.com/html/mov_bbb.mp4");
        l5.setDuration(800);
        l5.setChapter(ch3);
        l5.setStatus(true);

        Lesson l6 = new Lesson();
        l6.setTitle("Bài 2: CSS3 tạo kiểu dáng và responsive");
        l6.setVideo("https://www.w3schools.com/html/movie.mp4");
        l6.setDuration(1100);
        l6.setChapter(ch3);
        l6.setStatus(true);

        ch3.setLessons(new ArrayList<>(Arrays.asList(l5, l6)));

        Chapter ch4 = new Chapter();
        ch4.setTitle("Chương 2: Lập trình Backend với Spring Boot");
        ch4.setDescription("Xây dựng APIs chất lượng");
        ch4.setCourse(webCourse);
        ch4.setStatus(true);

        Lesson l7 = new Lesson();
        l7.setTitle("Bài 3: Cài đặt và tạo dự án Spring Boot đầu tiên");
        l7.setVideo("https://www.w3schools.com/html/mov_bbb.mp4");
        l7.setDuration(1400);
        l7.setChapter(ch4);
        l7.setStatus(true);

        ch4.setLessons(new ArrayList<>(Arrays.asList(l7)));

        webCourse.setChapters(new ArrayList<>(Arrays.asList(ch3, ch4)));
        courseRepository.save(webCourse);

        // 5. Create Course 3: English Speaking
        Course engCourse = new Course();
        engCourse.setCourseName("Mastering English Speaking & Pronunciation");
        engCourse.setDescription("Luyện phát âm tiếng Anh chuẩn IPA và phản xạ giao tiếp tự nhiên trong công việc và cuộc sống hàng ngày.");
        engCourse.setPrice(29.0);
        engCourse.setStartDate(LocalDate.now());
        engCourse.setEndDate(LocalDate.now().plusMonths(2));
        engCourse.setStatus("ACTIVE");
        engCourse.setSubCategory(engSub);
        engCourse.setAccount(instructor);
        engCourse.setImage("/img/globel/b3.jpg");
        engCourse.setThumbnail("/img/globel/b3.jpg");

        Chapter ch5 = new Chapter();
        ch5.setTitle("Chương 1: Bảng ký tự phiên âm quốc tế IPA");
        ch5.setDescription("Chuẩn hóa từng nguyên âm phụ âm");
        ch5.setCourse(engCourse);
        ch5.setStatus(true);

        Lesson l8 = new Lesson();
        l8.setTitle("Bài 1: Phát âm các nguyên âm đơn và đôi");
        l8.setVideo("https://www.w3schools.com/html/mov_bbb.mp4");
        l8.setDuration(700);
        l8.setChapter(ch5);
        l8.setStatus(true);

        Lesson l9 = new Lesson();
        l9.setTitle("Bài 2: Phát âm các phụ âm đặc biệt");
        l9.setVideo("https://www.w3schools.com/html/movie.mp4");
        l9.setDuration(1000);
        l9.setChapter(ch5);
        l9.setStatus(true);

        ch5.setLessons(new ArrayList<>(Arrays.asList(l8, l9)));

        engCourse.setChapters(new ArrayList<>(Arrays.asList(ch5)));
        courseRepository.save(engCourse);

        System.out.println("Database seeding completed successfully!");
    }
}
