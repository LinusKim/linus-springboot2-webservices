package com.linus.study.book.bootonaws.service.PostsService;

import com.linus.study.book.bootonaws.domain.posts.Posts;
import com.linus.study.book.bootonaws.domain.posts.PostsRepository;
import com.linus.study.book.bootonaws.web.dto.PostResponseDto;
import com.linus.study.book.bootonaws.web.dto.PostsListResponseDto;
import com.linus.study.book.bootonaws.web.dto.PostsSaveRequestDto;
import com.linus.study.book.bootonaws.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto updateRequestDto) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        entity.update(updateRequestDto.getTitle(), updateRequestDto.getContent(), updateRequestDto.getAuthor());

        return entity.getId();
    }

    @Transactional
    public void delete(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        postsRepository.delete(entity);
    }
}
