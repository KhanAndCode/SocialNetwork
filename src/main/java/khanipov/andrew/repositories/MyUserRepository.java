package khanipov.andrew.repositories;

import khanipov.andrew.models.Record;
import khanipov.andrew.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface MyUserRepository extends JpaRepository<User, Integer> {
    public User findByMail(String mail);


    @Override
    List<User> findAll();

    @Query("select u from User as u where (u.name like :name or u.name like :surname) or (u.subname like :name or u.subname like :surname)")
    List<User> findByNameAndSurnameLike(@Param("surname") String surname, @Param("name") String name);


    @Query("select u from User as u where u.name like :nameAndSurname or u.subname like :nameAndSurname")
    List<User> findByNameOrSurnameLike(@Param("nameAndSurname") String surname);

    @Query("select u.records from User as u where u.id like :id")
    public List<Record> findRecordsUser(@Param("id") Integer id);

    @Query("SELECT distinct u.records FROM User u WHERE u.id IN :following")
    List<Record> findFollowingUserRecord(@Param("following") Collection<Integer> following, Pageable pageable);

//    @Query("SELECT distinct u.records FROM User u WHERE u.id IN :following")
//    List<Record> findFollowingUserRecord(@Param("following") Collection<Integer> following, Pageable pageable);

//    @Modifying(clearAutomatically=true, flushAutomatically=true)
//    @Query("update User.followers u set u = :followers where u.id=:id")
//    Integer updateFollowers(@Param("followers") Collection<User> followers, @Param("id") Integer id);


//    @Modifying(clearAutomatically=true, flushAutomatically=true)
//    @Query("update User u set u.following = :following where u.id=:id")
//    Integer updateFollowing(@Param("following") Collection<User> following,@Param("id") Integer id);

}
