package com.ll.restByTdd.domain.post.post.Repository;

import com.ll.restByTdd.domain.post.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllByOrderByIdDesc();
}