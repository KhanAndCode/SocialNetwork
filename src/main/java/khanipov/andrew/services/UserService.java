package khanipov.andrew.services;

import khanipov.andrew.models.AdditionalUserInfo;
import khanipov.andrew.models.Record;
import khanipov.andrew.models.User;
import khanipov.andrew.repositories.AdditionalUserInfoRepository;
import khanipov.andrew.repositories.MyUserRepository;
import khanipov.andrew.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService {

    @Autowired
    MyUserRepository userRepository;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    AdditionalUserInfoRepository additionalUserInfoRepository;
    @Transactional
    public void saveRecord(Record record) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Record savedRecord = new Record();
        savedRecord.setRecord(record.getRecord());
        savedRecord.setDislikes(record.getDislikes());
        savedRecord.setLikes(record.getLikes());
        savedRecord.setUser(user);
        savedRecord.setTimeStamp(new Timestamp(System.currentTimeMillis()));
        recordRepository.saveAndFlush(savedRecord);
        System.out.println(savedRecord);
        System.out.println(user.getRecords().add(savedRecord));
        System.out.println(user.getRecords());
    }

    public void saveAdditionalInfo(AdditionalUserInfo additionalUserInfo){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AdditionalUserInfo additionalUserInfo1 = user.getUserInfo();
        additionalUserInfo1.setCity(additionalUserInfo.getCity());
        additionalUserInfo1.setCountry(additionalUserInfo.getCountry());
        additionalUserInfoRepository.saveAndFlush(additionalUserInfo1);
        userRepository.saveAndFlush(user);
    }

    public List<Record> getPosts(Integer page){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Integer> followingId = new HashSet<>();
        Set <User> following = user.getFollowing();
        for (User value : following) {
            followingId.add(value.getId());
        }
        followingId.add(user.getId());
        if (followingId.isEmpty()) {
            return new ArrayList<>();
        }
        return recordRepository.findFollowingUserRecord(followingId, new PageRequest(page, 4, new Sort(Sort.Direction.DESC, "timeStamp")));

    }

    @Transactional
    public void follow(Integer id) {
        User i = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User followingUser = userRepository.findOne(id);
        i.getFollowing().add(followingUser);
        followingUser.getFollowers().add(i);
        notificationService.notify(id, "became your follower!");
//
//        userRepository.updateFollowers(followingUser.getFollowers(), id);
//        userRepository.updateFollowing(i.getFollowing(), i.getId());

        userRepository.saveAndFlush(followingUser);
        userRepository.saveAndFlush(i);


    }

    @Transactional
    public void unfollow(Integer id) {
        User i = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User followingUser = userRepository.findOne(id);
        i.getFollowing().remove(followingUser);
        followingUser.getFollowers().remove(i);
        notificationService.notify(id, "unfollowed.");
//        userRepository.updateFollowing(i.getFollowing(), i.getId());
//        userRepository.updateFollowers(followingUser.getFollowers(), id);

        userRepository.saveAndFlush(followingUser);
        userRepository.saveAndFlush(i);

    }



    public List<User> search(String query) {
        if (query.contains(" ")) {
            String[] splited_query = query.split(" ");
            String name = '%' + splited_query[0] + '%';
            String subname = '%' + splited_query[0] + '%';
            return userRepository.findByNameAndSurnameLike(name, subname);
        } else
            query = '%' + query + '%';
        return userRepository.findByNameOrSurnameLike(query);
    }
}
