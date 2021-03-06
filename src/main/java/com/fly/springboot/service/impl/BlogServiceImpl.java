package com.fly.springboot.service.impl;

import com.fly.springboot.entity.Blog;
import com.fly.springboot.entity.Comment;
import com.fly.springboot.entity.User;
import com.fly.springboot.repository.BlogRepository;
import com.fly.springboot.service.interfaces.BlogService;
import com.fly.springboot.service.interfaces.UserService;
import com.fly.springboot.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author fly
 * @date 2018/5/21 0:31
 * @description
 **/
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public void removeBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.getOne(id);
    }

    @Override
    public Page<Blog> listBlogsByTitleBVote(User user, String title, Pageable pageable) {
        title = (title == null? "%%" : "%" + title + "%");
        String tags = title;
        return blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title,
                user,tags, user, pageable);
    }

    @Override
    public Page<Blog> listBlogsByTitleBVoteAndSort(User user, String title, Pageable pageable) {
        title = (title == null? "%%" : "%" + title + "%");
        return blogRepository.findByUserAndAndTitleLike(user, title, pageable);
    }

    @Override
    public void readingIncrease(Long id) {
        Blog blog = blogRepository.findById(id).get();
        blog.setReadSize(blog.getReadSize() + 1);
        this.saveBlog(blog);
    }

    @Transactional
    @Override
    public Blog createComment(Long blogId, String commentContent) {
        Blog blog = blogRepository.getOne(blogId);
        User user = userService.getUserByUsername(SecurityUtils.getUser().getUsername());
        Comment comment = new Comment(user, commentContent);
        blog.addComment(comment);
        return saveBlog(blog);
    }

    @Transactional
    @Override
    public void removeComment(Long blogId, Long commentId) {
        Blog blog = blogRepository.getOne(blogId);
        blog.removeComment(commentId);
        saveBlog(blog);
    }
}
