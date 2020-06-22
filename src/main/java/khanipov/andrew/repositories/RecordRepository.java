package khanipov.andrew.repositories;

import khanipov.andrew.models.Record;
import khanipov.andrew.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {

    @Query("SELECT distinct r  FROM Record r WHERE  r.user.id IN :following")
    List<Record> findFollowingUserRecord(@Param("following") Collection<Integer> following, Pageable pageable);
}
