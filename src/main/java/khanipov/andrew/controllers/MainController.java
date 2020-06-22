package khanipov.andrew.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import khanipov.andrew.models.AdditionalUserInfo;
import khanipov.andrew.models.Record;
import khanipov.andrew.models.User;
import khanipov.andrew.repositories.MyUserRepository;
import khanipov.andrew.repositories.RecordRepository;
import khanipov.andrew.services.IdService;
import khanipov.andrew.services.NotificationService;
import khanipov.andrew.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/")
public class MainController {


    Gson gson = new Gson();
    @Autowired
    MyUserRepository myUserRepository;

    @Autowired
    UserService service;
    @Autowired
    RecordRepository recordRepository;

    @Autowired
    NotificationService notificationService;

    @ResponseBody
    @RequestMapping(value = "/addpost", method = RequestMethod.POST, consumes = "application/json")
    public String addPost(@RequestBody Record record) {
        System.out.println(record);
        service.saveRecord(record);
        return "200";
    }

    @ResponseBody
    @RequestMapping(value = "/relation", method = RequestMethod.POST, consumes = "application/json")
    public String relation(@RequestBody IdService idService) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User following = myUserRepository.findOne(idService.getId());
        if (user.getFollowing().contains(following)) {
            service.unfollow(idService.getId());

        } else {
            service.follow(idService.getId());
        }

        return "200";
    }

    @ResponseBody
    @RequestMapping(value = "/getuser", method = RequestMethod.POST, consumes = "application/json")
    public User getUser(@RequestBody IdService idService) {
        System.out.println(idService.getId());
        User user = recordRepository.findOne(idService.getId()).getUser();


        return user;
    }

    @RequestMapping(value = "/search")
    public String search(@RequestParam("search") String query, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(query);
        model.addAttribute("i", user);
        if (query.equals("")) {
            model.addAttribute("users", myUserRepository.findAll());
        } else {
            model.addAttribute("users", service.search(query));
        }


        return "search";
    }

    @ResponseBody
    @RequestMapping(value = "/getposts", method = RequestMethod.POST, consumes = "application/json")
    public List<Record> getAllPosts(@RequestBody IdService idService) {
        return service.getPosts(idService.getId());
    }


    @RequestMapping(value = "/user")
    public String getUser(@RequestParam("userId") Integer id, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("i", user);
        System.out.println(myUserRepository.findOne(id).getRecords());
        model.addAttribute("user", myUserRepository.findOne(id));
        model.addAttribute("userRecords", myUserRepository.findOne(id).getRecords());
//        model.addAttribute("myInfo", user.getUserInfo());
        return "user";
    }

    @RequestMapping(value = "/followers")
    public String followers(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("i", user);
        model.addAttribute("users", myUserRepository.findOne(user.getId()).getFollowers());
        return "followers";
    }

    @RequestMapping(value = "/following")
    public String following(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("i", user);
        model.addAttribute("users", myUserRepository.findOne(user.getId()).getFollowing());
        return "following";
    }


    @RequestMapping("/")
    public String mainPage(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("info", new AdditionalUserInfo());
        model.addAttribute("i", user);
        return "main";
    }

    @RequestMapping("/settings")
    public String settings(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("i", user);
        model.addAttribute("info", new AdditionalUserInfo());
        return "settings";
    }

    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public String settingsPost(@ModelAttribute("info") AdditionalUserInfo userInfo) {
        service.saveAdditionalInfo(userInfo);
        return "redirect:/";
    }

    @RequestMapping("/notifications")
    public String notification(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("i", user);
        model.addAttribute("notifications", user.getNotifications());
        return "notifications";
    }

}
