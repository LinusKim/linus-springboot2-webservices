package com.linus.study.book.bootonaws.config.auth;

import com.linus.study.book.bootonaws.config.auth.dto.OAuthAttributies;
import com.linus.study.book.bootonaws.config.auth.dto.SessionUser;
import com.linus.study.book.bootonaws.domain.user.User;
import com.linus.study.book.bootonaws.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributies attributies = OAuthAttributies.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        User user = saveOrUpdate(attributies);

        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributies.getAttributies(), attributies.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributies attributies) {
        User user = userRepository.findByEmail(attributies.getEmail())
                .map(entity -> entity.update(attributies.getName(), attributies.getPicture())).orElse(attributies.toEntity());

        return userRepository.save(user);
    }
}
