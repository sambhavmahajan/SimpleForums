package com.github.sambhavmahajan.simpleforums.repositories;

import com.github.sambhavmahajan.simpleforums.models.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findAllByNameLike(String name);
    Optional<Community> findCommunityByName(String name);
    Optional<Community> findById(Long id);
    @Query(value = "SELECT * FROM COMMUNITY ORDER BY RANDOM() LIMIT 4", nativeQuery = true)
    List<Community> findRandomFour();
    List<Community> findAllByMembers_Id(Long memberId);
}