package com.github.sambhavmahajan.simpleforums.repositories;

import com.github.sambhavmahajan.simpleforums.models.ForumUser;
import com.github.sambhavmahajan.simpleforums.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findAllByPostId(long id);
    List<Vote> findAllByCommentId(long id);
    List<Vote> findAllByVoter(ForumUser user);
    Vote findByVoter_IdAndComment_Id(long voterId, long commentId);
    Vote findByVoter_idAndPost_Id(long id, long id1);
}
