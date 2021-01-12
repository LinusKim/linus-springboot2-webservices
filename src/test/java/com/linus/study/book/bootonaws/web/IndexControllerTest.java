package com.linus.study.book.bootonaws.web;

import com.linus.study.book.bootonaws.web.dto.PostsSaveRequestDto;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void indexPageLoading() {
        String body = this.restTemplate.getForObject("/", String.class);
        String expectedString = "Webservice starting with springboot";
        assertThat(body).contains(expectedString);
    }

    @Test
    public void postsSavePageLoading() {
        String body = restTemplate.getForObject("/posts/save", String.class);
        String expectedString = "게시글 등록";
        assertThat(body).contains(expectedString);
    }

    //테스트를 위한 Cold Start... initial data가 필요하다..
    @Test
    public void postsUpdatePageLoading() {
        Long postId = postsCreate();
        String body = restTemplate.getForObject("/posts/update/" + postId, String.class);
        String expectedString = "게시글 수정";
        assertThat(body).contains(expectedString);
    }

    private Long postsCreate() {
        String createPost = "/api/v1/posts";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject postJsonObject = new JSONObject();
        postJsonObject.put("title", "테스트 제목 입니다.");
        postJsonObject.put("author", "김희준");
        postJsonObject.put("content", "테스트 내용 입니다.");

        HttpEntity<String> request = new HttpEntity<String>(postJsonObject.toJSONString(), headers);
        String postResultString = restTemplate.postForObject(createPost, request, String.class);

        assertThat(postResultString).isNotNull();
        return Long.parseLong(postResultString);
    }
}
