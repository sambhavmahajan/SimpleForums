package com.github.sambhavmahajan.simpleforums.controllers;

import com.github.sambhavmahajan.simpleforums.dto.*;
import com.github.sambhavmahajan.simpleforums.models.*;
import com.github.sambhavmahajan.simpleforums.repositories.PostRepository;
import com.github.sambhavmahajan.simpleforums.repositories.VoteRepository;
import com.github.sambhavmahajan.simpleforums.security.session.SessionManager;
import com.github.sambhavmahajan.simpleforums.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

@Controller
public class MainController {

    private final SessionManager sessionManager;
    private final CommunityService communityService;
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final PostRepository postRepository;
    private final VoteService voteService;
    private final VoteRepository voteRepository;

    public MainController(final SessionManager sessionManager, CommunityService communityService, PostService postService, UserService userService, CommentService commentService, PostRepository postRepository, VoteService voteService, VoteRepository voteRepository) {
        this.sessionManager = sessionManager;
        this.communityService = communityService;
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.postRepository = postRepository;
        this.voteService = voteService;
        this.voteRepository = voteRepository;
    }

    public boolean setCommonVariables(HttpSession session, Model model) {
        if(!sessionManager.isValidSession(session)) {
            model.addAttribute("logged", false);
            return false;
        }
        model.addAttribute("logged", true);
        model.addAttribute("name", sessionManager.getCurrentUser(session));
        model.addAttribute("usr", userService.getUserByUsername(sessionManager.getCurrentUser(session)));
        return true;
    }

    @RequestMapping(value = {"/home", "/"}, method = RequestMethod.GET)
    public String home(HttpSession session, Model model) {
        if(setCommonVariables(session, model)) {
            //Modify Later
            model.addAttribute("Posts", new ArrayList<Post>());
        }
        model.addAttribute("communities", communityService.getRandFour());
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        if(sessionManager.isValidSession(session)) {
            System.out.println(sessionManager.getCurrentUser(session) + " logged out " + LocalDateTime.now());
            session.invalidate();
        }
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        if(sessionManager.isValidSession(session)) {
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserLoginDTO usr, HttpSession session, Model model) {
        if(sessionManager.isValidSession(session)) {
            return "redirect:/home";
        }
        ForumUser auth = userService.getUserByUsername(usr.getUsername());
        if(auth != null && auth.getPassword().equals(usr.getPassword())) {
            sessionManager.createSession(session, usr.getUsername(), usr.getPassword());
            System.out.println(usr.getUsername() + " " + "logged in " + LocalDateTime.now());
            return "redirect:/home";
        }
        model.addAttribute("badlogin", true);
        System.out.println(usr.getUsername() + " tried to login " + LocalDateTime.now());
        return "login";
    }

    @GetMapping("/register")
    public String register(HttpSession session, Model model) {
        if(sessionManager.isValidSession(session)) {
            return "redirect:/home";
        }
        model.addAttribute("fail", 0);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserRegisterDTO user, HttpSession session, Model model) {
        if(sessionManager.isValidSession(session)) {
            return "redirect:/home";
        }
        if(userService.getUserByUsername(user.getUsername()) != null) {
            model.addAttribute("fail", 1);
        }else {
            ForumUser newUser = new ForumUser(user.getUsername(), user.getPassword(), user.getFirstName(), user.getPassword(), user.getBio(), "", user.getEmail());
            userService.saveUser(newUser);
            model.addAttribute("fail", -1);
        }
        System.out.println(user.getUsername() + " registered " + LocalDateTime.now());
        return "register";
    }

    @GetMapping("/user/{username}")
    public String user(@PathVariable String username, HttpSession session, Model model) {
        ForumUser usr = userService.getUserByUsername(username);
        if(usr != null) {
            System.out.println(usr.getUsername() + " " + "profile viewed " + LocalDateTime.now());
            model.addAttribute("fail", -1);
            model.addAttribute("title", usr.getUsername());
            model.addAttribute("user", usr);
            model.addAttribute("posts", postService.getAllPostByAuthor(usr));
            model.addAttribute("communities", communityService.getByMember(usr));
            model.addAttribute("logged", true);
        } else {
            model.addAttribute("fail", 1);
            model.addAttribute("title", username + " not found");
            model.addAttribute("editable",sessionManager.getCurrentUser(session) !=null &&  sessionManager.getCurrentUser(session).equals(username));
            model.addAttribute("logged", false);
        }
        return "userpage";
    }

    @GetMapping("/createCommunity")
    public String createCommunity(HttpSession session, Model model) {
        if(!sessionManager.isValidSession(session)) {
            return "redirect:/home";
        }
        model.addAttribute("username", sessionManager.getCurrentUser(session));
        model.addAttribute("logged", sessionManager.isValidSession(session));
        return "createCommunity";
    }

    @PostMapping("/createCommunity")
    public String createCommunity(@ModelAttribute CommunityDTO communityDTO, HttpSession session, Model model) {
        if(!sessionManager.isValidSession(session)) {
            return "redirect:/home";
        }
        Community community = communityService.getCommunityByName(communityDTO.getName());
        if(community != null) {
            model.addAttribute("fail", true);
            return "createCommunity";
        }
        ForumUser tmpUsr = userService.getUserByUsername(sessionManager.getCurrentUser(session));
        HashSet<ForumUser> members = new HashSet<>();
        community = new Community(communityDTO.getName(), communityDTO.getDescription(), 1, members, tmpUsr, new HashSet<Post>(), new ArrayList<>(Arrays.asList(communityDTO.getRules().split("\n"))));
        communityService.joinCommunity(community, tmpUsr);
        communityService.createCommunity(community);
        System.out.println(tmpUsr.getUsername() + " created " + community.getName() + " community " + LocalDateTime.now());
        return "redirect:/community/" + community.getName();
    }

    @GetMapping("/community/{communityName}")
    public String community(@PathVariable String communityName, HttpSession session, Model model) {
        Community community = communityService.getCommunityByName(communityName);
        if(community == null) {
            model.addAttribute("fail", true);
            return "community";
        }
        model.addAttribute("fail", false);
        ForumUser usr = userService.getUserByUsername(sessionManager.getCurrentUser(session));
        if(sessionManager.isValidSession(session)) {
            model.addAttribute("username", usr.getUsername());
            model.addAttribute("member", community.getMembers().contains(usr));
            model.addAttribute("logged", true);
            if(community.getOwner() == usr) {
                model.addAttribute("owner", true);
            }
            System.out.println(usr.getUsername() + " viewed " + communityName + " community " + LocalDateTime.now());
        } else {
            model.addAttribute("logged", false);
        }
        model.addAttribute("name", community.getName());
        model.addAttribute("description", community.getDescription());
        model.addAttribute("rules", community.getRules());
        model.addAttribute("count", community.getMemberCount());
        model.addAttribute("posts", community.getPosts());
        return "community";
    }

    @PostMapping("/leave/{name}")
    public String leaveCommunity(@PathVariable String name,HttpSession session, Model model){
        if(!sessionManager.isValidSession(session)) {
            return "redirect:/home";
        }
        Community community = communityService.getCommunityByName(name);
        if(community == null) {
            return "redirect:/home";
        }
        ForumUser usr = userService.getUserByUsername(sessionManager.getCurrentUser(session));
        if(community.getMembers().contains(usr) && !community.getOwner().equals(usr)) {
            System.out.println(usr.getUsername() + " left " + community.getName() + " community " + LocalDateTime.now());
            communityService.leaveCommunity(community, usr);
        }
        return "redirect:/community/" + community.getName();
    }

    @PostMapping("/join/{name}")
    public String joinCommunity(@PathVariable String name,HttpSession session, Model model){
        if(!sessionManager.isValidSession(session)) {
            return "redirect:/home";
        }
        Community community = communityService.getCommunityByName(name);
        if(community == null) {
            return "redirect:/home";
        }
        ForumUser usr = userService.getUserByUsername(sessionManager.getCurrentUser(session));
        if(!community.getMembers().contains(usr)) {
            System.out.println(usr.getUsername() + " joined " + community.getName() + " community " + LocalDateTime.now());
            communityService.joinCommunity(community, usr);
        }
        return "redirect:/community/" + community.getName();
    }
    @GetMapping("/editCommunity/{name}")
    public String editCommunity(@PathVariable String name, HttpSession session, Model model) {
        Community community = communityService.getCommunityByName(name);
        if(community == null) return "redirect:/home";
        String usrname = sessionManager.getCurrentUser(session);
        if(!(sessionManager.isValidSession(session) && community.getOwner().getUsername().equals(usrname))) {
            return "redirect:/home";
        }
        model.addAttribute("name", community.getName());
        model.addAttribute("description", community.getDescription());
        model.addAttribute("rules", community.getRules());
        model.addAttribute("posts",community.getPosts());
        model.addAttribute("logged", true);
        model.addAttribute("username", usrname);
        return "editCommunity";
    }
    @PostMapping("/editCommunity")
    public String editCommunity(@ModelAttribute CommunityDTO communityDTO,HttpSession session, Model model) {
        if(!sessionManager.isValidSession(session)) {
            return "redirect:/home";
        }
        Community community = communityService.getCommunityByName(communityDTO.getName());
        if(community == null || !sessionManager.getCurrentUser(session).equals(community.getOwner().getUsername())) {
            return "redirect:/home";
        }
        community.setDescription(communityDTO.getDescription());
        community.setRulesFromString(communityDTO.getRules());
        communityService.updateCommunity(community);
        System.out.println(community.getOwner().getUsername() + " modified " + communityDTO.getName() + " community " + LocalDateTime.now());
        return "redirect:/community/" + community.getName();
    }
    @GetMapping("/newPost/{communityName}")
    public String newPost(@PathVariable String communityName, HttpSession session, Model model) {
        if(!sessionManager.isValidSession(session)) {
            return "redirect:/home";
        }
        Community community = communityService.getCommunityByName(communityName);
        ForumUser usr = userService.getUserByUsername(sessionManager.getCurrentUser(session));
        if(!community.getMembers().contains(usr)) {
            return "redirect:/home";
        }
        model.addAttribute("community", community.getName());
        model.addAttribute("username", usr.getUsername());
        model.addAttribute("logged", true);
        return "createPost";
    }
    @PostMapping("/newPost/{communityName}")
    public String newPost(@ModelAttribute PostDTO postDTO, HttpSession session, Model model) {
        if(!sessionManager.isValidSession(session)) {
            return "redirect:/home";
        }
        Community community = communityService.getCommunityByName(postDTO.getCommunityName());
        ForumUser usr = userService.getUserByUsername(sessionManager.getCurrentUser(session));
        if(community == null || !community.getMembers().contains(usr) ) {
            return "redirect:/home";
        }
        Post post = new Post(postDTO.getTitle(), postDTO.getContent(), usr, LocalDateTime.now(), new ArrayList<Comment>(), new HashSet<Vote>(), community, 0, 0);
        postService.createPost(post);
        community.getPosts().add(post);
        communityService.updateCommunity(community);
        System.out.println(usr.getUsername() + " authored a post " + postDTO.getTitle() + " in community " + community.getName() + " " + LocalDateTime.now());
        return "redirect:/community/" + community.getName() + "/" + post.getId();
    }
    @GetMapping("/community/{name}/{id}")
    private String viewPost(@PathVariable String name, @PathVariable long id, HttpSession session, Model model) {
        Community community = communityService.getCommunityByName(name);
        if(community == null) {
            return "redirect:/home";
        }
        Post post = postService.getPostById(id);
        if(post == null || !community.getPosts().contains(post)) {
            return "redirect:/home";
        }
        if(sessionManager.isValidSession(session)) {
            setCommonVariables(session, model);
            model.addAttribute("community", community.getName());
            model.addAttribute("id", post.getId());
            model.addAttribute("logged", true);
            model.addAttribute("username", sessionManager.getCurrentUser(session));
            System.out.println(sessionManager.getCurrentUser(session) + " view post " + post.getId() + " " + LocalDateTime.now());
        } else {
            model.addAttribute("logged", false);
        }
        model.addAttribute("title", post.getTitle());
        model.addAttribute("author", post.getAuthor().getUsername());
        model.addAttribute("timeStamp", post.getTimeStamp().toString());
        model.addAttribute("content", post.getContent());
        model.addAttribute("comments", post.getComments());
        model.addAttribute("postLikes", post.getUpvotes());
        model.addAttribute("postDislikes", post.getDownvotes());
        return "viewPost";
    }
    @PostMapping("/community/{name}/{id}/comment")
    public String comment(@PathVariable String name, @PathVariable long id, @ModelAttribute CommentDTO commentDTO, HttpSession session, Model model) {
        if(!sessionManager.isValidSession(session)) {
            return "redirect:/login";
        }
        Community community = communityService.getCommunityByName(name);
        ForumUser usr = userService.getUserByUsername(sessionManager.getCurrentUser(session));
        Post post = postService.getPostById(id);
        if(community == null) {
            return "redirect:/home";
        }
        if(!community.getMembers().contains(usr)) {
            return "redirect:/home";
        }
        if(post == null || !community.getPosts().contains(post)) {
            return "redirect:/home";
        }
        Comment comment = new Comment(commentDTO.getContent(), post, usr, LocalDateTime.now(), new ArrayList<>(), new HashSet<>(), null, 0, 0);
        commentService.addComment(comment);
        post.getComments().add(comment);
        postService.updatePost(post);
        return "redirect:/community/" + community.getName() + "/" + post.getId();
    }
    @DeleteMapping("/community/{name}/{id}/{idComment}/delete")
    public String deleteComment(@PathVariable String name, @PathVariable long id,@PathVariable long idComment, HttpSession session, Model model) {
        if(!sessionManager.isValidSession(session)) {
            return "redirect:/login";
        }
        Community community = communityService.getCommunityByName(name);
        if(community == null) {
            return "redirect:/home";
        }
        ForumUser usr = userService.getUserByUsername(sessionManager.getCurrentUser(session));
        Post post = postService.getPostById(id);
        if(post == null || !community.getPosts().contains(post)) {
            return "redirect:/home";
        }
        Comment comment = commentService.getCommentById(idComment);
        if(comment == null || !post.getComments().contains(comment)) {
            return "redirect:/home";
        }
        post.getComments().remove(comment);
        postService.updatePost(post);
        commentService.remove(comment);
        return "redirect:/community/" + community.getName() + "/" + post.getId();
    }
    @PostMapping("/community/{name}/{postId}/{commentId}/{voteType}")
    public synchronized String VoteComment(@PathVariable String name, @PathVariable long postId, @PathVariable long commentId, @PathVariable String voteType,HttpSession session, Model model) {
        if(!voteType.equals("upvote") && !voteType.equals("downvote")) return "redirect:/login";
        if(!sessionManager.isValidSession(session)) {
            return "redirect:/login";
        }
        Community community = communityService.getCommunityByName(name);
        if(community == null) return "redirect:/home";
        Post post = postService.getPostById(postId);
        if(post == null || !community.getPosts().contains(post)) return "redirect:/home";
        Comment comment = commentService.getCommentById(commentId);
        if(comment == null || !post.getComments().contains(comment)) return "redirect:/home";
        ForumUser usr = userService.getUserByUsername(sessionManager.getCurrentUser(session));
        Vote vote = voteService.getVoteByVoterAndComment(usr, comment);
        if(voteType.equals("upvote")) {
            if(vote == null) {
                vote = new Vote(usr, true, comment, post);
                voteService.save(vote);
                comment.getVotes().add(vote);
                comment.setUpvotes(comment.getUpvotes() + 1);
                commentService.update(comment);
            } else if(vote.isUpvote()) {
                comment.getVotes().remove(vote);
                comment.setUpvotes(comment.getUpvotes() - 1);
                commentService.update(comment);
                voteService.delete(vote);
            } else {
                vote.setUpvote(true);
                voteService.save(vote);
                comment.setUpvotes(comment.getUpvotes() + 1);
                comment.setDownvotes(comment.getDownvotes() - 1);
                commentService.update(comment);
            }
        } else {
            if(vote == null) {
                vote = new Vote(usr, false, comment, post);
                voteService.save(vote);
                comment.getVotes().add(vote);
                comment.setDownvotes(comment.getDownvotes() + 1);
                commentService.update(comment);
            } else if(!vote.isUpvote()){
                comment.getVotes().remove(vote);
                comment.setDownvotes(comment.getDownvotes() - 1);
                commentService.update(comment);
                voteService.delete(vote);
            } else {
                vote.setUpvote(false);
                voteService.save(vote);
                comment.setDownvotes(comment.getDownvotes() + 1);
                comment.setUpvotes(comment.getUpvotes() - 1);
                commentService.update(comment);
            }
        }
        return "redirect:/community/" + name + "/" + post.getId();
    }
    @PostMapping("/community/{name}/{postId}/{voteType}")
    public synchronized String VotePost(@PathVariable String name, @PathVariable long postId, @PathVariable String voteType,HttpSession session, Model model) {
        if(!voteType.equals("upvote") && !voteType.equals("downvote")) return "redirect:/login";
        if(!sessionManager.isValidSession(session)) {
            return "redirect:/login";
        }
        Community community = communityService.getCommunityByName(name);
        if(community == null) return "redirect:/home";
        Post post = postService.getPostById(postId);
        if(post == null || !community.getPosts().contains(post)) return "redirect:/home";
        ForumUser usr = userService.getUserByUsername(sessionManager.getCurrentUser(session));
        Vote vote = voteService.getVoteByVoterAndPost(usr, post);
        if(voteType.equals("upvote")) {
            if(vote == null) {
                vote = new Vote(usr, true, null, post);
                voteService.save(vote);
                post.getVotes().add(vote);
                post.setUpvotes(post.getUpvotes() + 1);
                postService.updatePost(post);
            } else if(vote.isUpvote()) {
                post.getVotes().remove(vote);
                post.setUpvotes(post.getUpvotes() - 1);
                postService.updatePost(post);
                voteService.delete(vote);
            } else {
                vote.setUpvote(true);
                voteService.save(vote);
                post.setUpvotes(post.getUpvotes() + 1);
                post.setDownvotes(post.getDownvotes() - 1);
                postService.updatePost(post);
            }
        } else {
            if(vote == null) {
                vote = new Vote(usr, false, null, post);
                voteService.save(vote);
                post.getVotes().add(vote);
                post.setDownvotes(post.getDownvotes() + 1);
                postService.updatePost(post);
            } else if(!vote.isUpvote()){
                post.getVotes().remove(vote);
                post.setDownvotes(post.getDownvotes() - 1);
                postService.updatePost(post);
                voteService.delete(vote);
            } else {
                vote.setUpvote(false);
                voteService.save(vote);
                post.setDownvotes(post.getDownvotes() + 1);
                post.setUpvotes(post.getUpvotes() - 1);
                postService.updatePost(post);
            }
        }
        return "redirect:/community/" + name + "/" + post.getId();
    }
}
