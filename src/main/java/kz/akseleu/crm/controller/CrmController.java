package kz.akseleu.crm.controller;

import kz.akseleu.crm.model.Application;
import kz.akseleu.crm.service.ApplicationService;
import kz.akseleu.crm.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
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
    public String createPost(@RequestParam(name = "user_name") String userName,
                             @RequestParam(name = "course_id") int courseId,
                             @RequestParam(name = "commentary") String commentary,
                             @RequestParam(name = "phone") String phone,
                             @RequestParam(name = "handled", defaultValue = "false") boolean handled) {

        Application application = Application.builder()
                .user_name(userName)
                .course_id(courseId)
                .commentary(commentary)
                .phone(phone)
                .handled(handled)
                .build();

        applicationService.createApplication(application);
        return "redirect:/";
    }

    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable Long id) {
        Application application = applicationService.getApplicationById(id);
        model.addAttribute("app", application);
        model.addAttribute("courses", courseService.getAllCourses());
        return "details";
    }

    @PostMapping("/details/{id}")
    public String updateApp(@PathVariable Long id,
                            @RequestParam(name = "user_name") String userName,
                            @RequestParam(name = "course_id") int courseId,
                            @RequestParam(name = "commentary", required = false) String commentary,
                            @RequestParam(name = "phone") String phone,
                            @RequestParam(name = "handled", defaultValue = "false") boolean handled) {

        Application application = applicationService.getApplicationById(id);
        if (application == null) return "redirect:/";

        application.setUser_name(userName);
        application.setCourse_id(courseId);
        application.setCommentary(commentary);
        application.setPhone(phone);
        application.setHandled(handled);

        applicationService.updateApplication(application);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteApp(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return "redirect:/";
    }
}