package kz.akseleu.crm.controller;

import kz.akseleu.crm.model.Application;
import kz.akseleu.crm.model.Course;
import kz.akseleu.crm.service.ApplicationService;
import kz.akseleu.crm.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CrmController {

    private final ApplicationService applicationService;
    private final CourseService courseService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("CRM", applicationService.getAllApplication());
        return "home";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "create";
    }

    @PostMapping("/create")
    public String createPost(@RequestParam("user_name") String userName,
                             @RequestParam("course_id") Long courseId,
                             @RequestParam(name = "commentary", required = false) String commentary,
                             @RequestParam("phone") String phone,
                             @RequestParam(name = "handled", defaultValue = "false") boolean handled) {

        Course course = courseService.getById(courseId);
        if (course == null) return "redirect:/create";

        Application application = Application.builder()
                .user_name(userName)
                .course(course)
                .commentary(commentary)
                .phone(phone)
                .handled(handled)
                .build();

        applicationService.save(application);
        return "redirect:/";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        Application application = applicationService.getById(id);
        if (application == null) return "redirect:/";

        model.addAttribute("app", application);
        model.addAttribute("courses", courseService.getAllCourses());
        return "details";
    }

    @PostMapping("/details/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam("user_name") String userName,
                         @RequestParam("course_id") Long courseId,
                         @RequestParam(name = "commentary", required = false) String commentary,
                         @RequestParam("phone") String phone,
                         @RequestParam(name = "handled", defaultValue = "false") boolean handled) {

        Application application = applicationService.getById(id);
        if (application == null) return "redirect:/";

        Course course = courseService.getById(courseId);
        if (course == null) return "redirect:/details/" + id;

        application.setUser_name(userName);
        application.setCourse(course);
        application.setCommentary(commentary);
        application.setPhone(phone);
        application.setHandled(handled);

        applicationService.save(application);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        applicationService.deleteById(id);
        return "redirect:/";
    }
}