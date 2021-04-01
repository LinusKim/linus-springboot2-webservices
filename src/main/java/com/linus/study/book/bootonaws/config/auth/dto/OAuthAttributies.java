package com.linus.study.book.bootonaws.config.auth.dto;

import com.linus.study.book.bootonaws.domain.user.Role;
import com.linus.study.book.bootonaws.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributies {

    private Map<String, Object> attributies;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder

    public OAuthAttributies(Map<String, Object> attributies, String nameAttributeKey, String name, String email, String picture) {
        this.attributies = attributies;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributies of(String registrationId, String userNameAttributeName, Map<String, Object> attributies) {
        return ofGoogle(userNameAttributeName, attributies);
    }

    private static OAuthAttributies ofGoogle(String userNameAttributeName, Map<String, Object> attributies) {
        return OAuthAttributies.builder()
                .name((String) attributies.get("name"))
                .email((String) attributies.get("email"))
                .picture((String) attributies.get("picture"))
                .attributies(attributies)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }
}
