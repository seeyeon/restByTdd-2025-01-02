package com.ll.restByTdd.domain.post.post.entity;

import com.ll.restByTdd.domain.member.member.entity.Member;
import com.ll.restByTdd.domain.post.comment.entity.PostComment;
import com.ll.restByTdd.global.exceptions.ServiceException;
import com.ll.restByTdd.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseTime {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;


    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "post", cascade =  {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private List<PostComment> comments = new ArrayList<>();

    public PostComment addComment(Member author, String content) {
        PostComment comment = PostComment.builder()
                .post(this)
                .author(author)
                .content(content)
                .build();

        comments.add(comment);

        return comment;
    }

    public  List<PostComment> getCommentsByOrderByIdDesc() {
        return comments.reversed();
    }

    public Optional<PostComment> getCommentById(long commentId){
        return comments.stream()
                .filter(comment -> comment.getId() == commentId)
                .findFirst();
    }

    public void removeComment(PostComment postComment) {
        comments.remove(postComment);
    }

    public void checkActorCanDelete(Member actor) {
        if(actor == null)
            throw new ServiceException("403-1","로그인 후 이용 가능합니다.");

        if(actor.isAdmin()) return;

        if(actor.equals(author)) return;

        throw new ServiceException("403-2","작성자만 글을 삭제할 권한이 있습니다.");
    }

    public void checkActorCanModify(Member actor) {

        if(actor == null)
            throw new ServiceException("403-1","로그인 후 이용 가능합니다.");

        if(actor.isAdmin()) return;

        if(actor.equals(author)) return;

        throw new ServiceException("403-2","작성자만 글을 수정 할 권한이 있습니다.");
    }
}