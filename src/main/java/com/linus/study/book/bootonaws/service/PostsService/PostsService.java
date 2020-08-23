package com.linus.study.book.bootonaws.service.PostsService;

import com.linus.study.book.bootonaws.domain.posts.Posts;
import com.linus.study.book.bootonaws.domain.posts.PostsRepository;
import com.linus.study.book.bootonaws.web.dto.PostResponseDto;
import com.linus.study.book.bootonaws.web.dto.PostsSaveRequestDto;
import com.linus.study.book.bootonaws.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    public PostResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        return new PostResponseDto(entity);
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto updateRequestDto) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        entity.update(updateRequestDto.getTitle(), updateRequestDto.getContent(), updateRequestDto.getAuthor());

        return entity.getId();
    }
}
