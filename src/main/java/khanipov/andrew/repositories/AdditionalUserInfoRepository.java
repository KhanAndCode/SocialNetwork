package khanipov.andrew.repositories;

import khanipov.andrew.models.AdditionalUserInfo;
import khanipov.andrew.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalUserInfoRepository   extends JpaRepository<AdditionalUserInfo, Integer> {

}
