package com.linus.study.book.bootonaws.web.dto;

import com.linus.study.book.bootonaws.domain.posts.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

    public PostResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author  = entity.getAuthor();
        this.createDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }

    @Override
    public String toString() {
        return "PostResponseDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", createDate=" + createDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}
