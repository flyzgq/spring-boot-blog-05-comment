package com.fly.springboot.repository;

import com.fly.springboot.entity.Blog;
import com.fly.springboot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fly
 * @date 2018/5/21 0:10
 * @description
 **/
public interface BlogRepository extends JpaRepository<Blog, Long> {

    /**
     * 根据用户名、博客标题分页查询博客列表
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> findByUserAndAndTitleLike(User user, String title, Pageable pageable);

    /**
     * 根据用户名、博客查询博客列表(时间逆序)
     * @param title
     * @param user
     * @param tags
     * @param user2
     * @param pageable
     * @return
     */
    Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title,
               User user, String tags, User user2, Pageable pageable);


}
