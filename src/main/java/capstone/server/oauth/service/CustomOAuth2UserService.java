package capstone.server.oauth.service;


import capstone.server.domain.User;
import capstone.server.oauth.entity.ProviderType;
import capstone.server.oauth.entity.RoleType;
import capstone.server.oauth.entity.UserPrincipal;
import capstone.server.oauth.exception.OAuthProviderMissMatchException;
import capstone.server.oauth.info.OAuth2UserInfo;
import capstone.server.oauth.info.OAuth2UserInfoFactory;
import capstone.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {

        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration()
                                                                    .getRegistrationId()
                                                                    .toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        // -> null check / optional.get() ??????
//        User savedUser = userRepository.findByUserId(userInfo.getId())
//                                       .get();
        Optional<User> OsavedUser = userRepository.findByUserId(userInfo.getId());

        if (!OsavedUser.isEmpty()) {
            return UserPrincipal.create(OsavedUser.filter(user1 -> user1.getProviderType() != providerType)
                                                  .map(user1 -> updateUser(user1, userInfo))
                                                  .orElseThrow(() -> new OAuthProviderMissMatchException("Looks like you're signed up with " + providerType +
                                                          " account. Please use your " + OsavedUser.get()
                                                                                                   .getProviderType() + " account to login.")), user.getAttributes());

        }
        User savedUser = createUser(userInfo, providerType);
        return UserPrincipal.create(savedUser, user.getAttributes());

    }

    private User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        LocalDateTime now = LocalDateTime.now();
        //????????? ??????
//        User user = new User(
//                userInfo.getId(),
//                userInfo.getName(),
//                userInfo.getEmail(),
//                "Y",
//                userInfo.getImageUrl(),
//                providerType,
//                RoleType.USER,
//                now,
//                now
//        );
        User user = User.builder()
                        .userId(userInfo.getId())
                        .username(userInfo.getName())
                        .email(userInfo.getEmail())
                        .emailVerifiedYn("Y")
                        .profileImageUrl(userInfo.getImageUrl())
                        .providerType(providerType)
                        .roleType(RoleType.USER)
                        .build();

        return userRepository.saveAndFlush(user);
    }

    private User updateUser(User user, OAuth2UserInfo userInfo) {
        if (userInfo.getName() != null && !user.getUsername()
                                               .equals(userInfo.getName())) {
            user.changeName(userInfo.getName());
        }
        if (userInfo.getImageUrl() != null && !user.getProfileImageUrl()
                                                   .equals(userInfo.getImageUrl())) {
            user.changeProfileImageUrl(userInfo.getImageUrl());
        }

        return user;
    }
}
