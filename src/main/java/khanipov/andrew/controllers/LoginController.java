package khanipov.andrew.controllers;

import khanipov.andrew.models.AdditionalUserInfo;
import khanipov.andrew.models.User;
import khanipov.andrew.repositories.AdditionalUserInfoRepository;
import khanipov.andrew.repositories.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    MyUserRepository myUserRepository;

    @Autowired
    AdditionalUserInfoRepository additionalUserInfoRepository;


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "log_reg";
    }

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public String login(@ModelAttribute("user") User user , Model model) {
//        return "log_reg";
//    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@Valid @ModelAttribute("user") User user , BindingResult errors, Model model) {
        if (errors.hasErrors()){
            return "log_reg";
        }
        if (myUserRepository.findByMail(user.getMail()) != null){
            errors.rejectValue("mail", "error.emailExists",  "Ok");
            return "log_reg";
        }
        user.setRole("ROLE_USER");
        myUserRepository.saveAndFlush(user);
        AdditionalUserInfo additionalUserInfo = new AdditionalUserInfo();
        additionalUserInfo.setUser(user);

        user.setUserInfo(additionalUserInfo);
        additionalUserInfoRepository.saveAndFlush(additionalUserInfo);
        myUserRepository.saveAndFlush(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return "redirect:/";
    }


}
