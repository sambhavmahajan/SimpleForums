package com.github.sambhavmahajan.simpleforums.services;

import com.github.sambhavmahajan.simpleforums.models.Community;
import com.github.sambhavmahajan.simpleforums.models.ForumUser;
import com.github.sambhavmahajan.simpleforums.repositories.CommunityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CommunityService {
    private final CommunityRepository communityRepository;

    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }
    public Community getCommunityById(long id) {
        return communityRepository.findById(id).get();
    }
    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    @Transactional
    public void createCommunity(Community community) {
        communityRepository.save(community);
    }

    @Transactional
    public void updateCommunity(Community community) {
        communityRepository.save(community);
    }
    public List<Community> searchCommunities(String query) {
        return communityRepository.findAllByNameLike("%" + query + "%");
    }

    @Transactional
    public void joinCommunity(Community community, ForumUser user) {
        Set<ForumUser> members = community.getMembers();
        members.add(user);
        community.setMembers(members);
        community.setMemberCount(members.size());
        communityRepository.save(community);
    }

    @Transactional
    public void leaveCommunity(Community community, ForumUser user) {
        Set<ForumUser> members = community.getMembers();
        members.remove(user);
        community.setMembers(members);
        community.setMemberCount(members.size());
        communityRepository.save(community);
    }
    public List<Community> getRandFour() {
        return (new HashSet<>(communityRepository.findRandomFour())).stream().toList();
    }
    public List<Community> getByMember(ForumUser user) {
        return communityRepository.findAllByMembers_Id(user.getId());
    }
    public Community getCommunityByName(String name) {
        return communityRepository.findCommunityByName(name).orElse(null);
    }
}
