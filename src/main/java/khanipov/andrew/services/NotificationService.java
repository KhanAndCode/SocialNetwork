package khanipov.andrew.services;

import khanipov.andrew.models.Notification;
import khanipov.andrew.models.User;
import khanipov.andrew.repositories.MyUserRepository;
import khanipov.andrew.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    MyUserRepository userRepository;
    public void notify(Integer userId, String message){
        User user_described = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findOne(userId);
        Notification notification = new Notification();
        notification.setRecord(message);
        notification.setUser(user);
        notification.setActed_user_id(user_described.getId());
        notification.setUser_name(user_described.getName());
        notification.setUser_subname(user_described.getSubname());
        user.getNotifications().add(notification);
        notificationRepository.saveAndFlush(notification);
        userRepository.saveAndFlush(user);

    }

}
