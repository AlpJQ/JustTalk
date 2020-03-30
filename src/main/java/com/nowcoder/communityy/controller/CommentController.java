package com.nowcoder.communityy.controller;

import com.nowcoder.communityy.entity.Comment;
import com.nowcoder.communityy.service.CommentService;
import com.nowcoder.communityy.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        // hostHolder.getUser().getId()拿到当前用户的id
        comment.setUserId(hostHolder.getUser().getId());// 如果用户没有登陆就会报异常
        comment.setStatus(0);// 0表示有效的
        comment.setCreateTime(new Date());
        commentService.addComment(comment) ;

        return "redirect:/discuss/detail/" + discussPostId;
    }

}

