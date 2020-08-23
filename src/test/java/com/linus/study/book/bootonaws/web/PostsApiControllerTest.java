package com.linus.study.book.bootonaws.web;

import com.linus.study.book.bootonaws.domain.posts.Posts;
import com.linus.study.book.bootonaws.domain.posts.PostsRepository;
import com.linus.study.book.bootonaws.web.dto.PostResponseDto;
import com.linus.study.book.bootonaws.web.dto.PostsSaveRequestDto;
import com.linus.study.book.bootonaws.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() {
        postsRepository.deleteAll();
    }

    @Test
    public void test_savePost() {

        //Given
        String title = "Test Title";
        String content = "Test Content";
        String author = "저작자";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        //When
        String savePostsApiUrl = "http://localhost:" + port + "/api/v1/posts";
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(savePostsApiUrl, requestDto, Long.class);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> allPosts = postsRepository.findAll();
        assertThat(allPosts.get(0).getTitle()).isEqualTo(title);
        assertThat(allPosts.get(0).getContent()).isEqualTo(content);
        assertThat(allPosts.get(0).getAuthor()).isEqualTo(author);
    }

    @Test
    public void test_findPost() {

        Posts createdPost = createPost();

        String findPostsApiUrl = "http://localhost:" + port + "/api/v1/posts/" + createdPost.getId();
        ResponseEntity<PostResponseDto> responseEntity = restTemplate.getForEntity(findPostsApiUrl, PostResponseDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getId()).isEqualTo(createdPost.getId());
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(createdPost.getTitle());
        assertThat(responseEntity.getBody().getContent()).isEqualTo(createdPost.getContent());
        assertThat(responseEntity.getBody().getAuthor()).isEqualTo(createdPost.getAuthor());
    }

    @Test
    public void test_updatePost() {
        Posts createdPost = createPost();
        Long targetPostId = createdPost.getId();

        String title = "제목 수정";
        String content = "내용 수정";
        String author = "글쓴이 수정";

        PostsUpdateRequestDto updateRequestDto = PostsUpdateRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + targetPostId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(updateRequestDto);
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        Optional<Posts> postEntity = postsRepository.findById(targetPostId);
        assertThat(postEntity).isNotNull();
        assertThat(postEntity.orElse(null).getTitle()).isEqualTo(title);
        assertThat(postEntity.orElse(null).getContent()).isEqualTo(content);
        assertThat(postEntity.orElse(null).getAuthor()).isEqualTo(author);
    }

    private Posts createPost() {
        String title = "테스트 타이틀";
        String content = "글 내용 입니다.";
        String author = "작성자";

        Posts createdPost = postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build());

        return createdPost;
    }
}
