package com.study.totee.api.controller;

import com.study.totee.api.model.UserRefreshToken;
import com.study.totee.api.dto.auth.AuthReqModel;
import com.study.totee.api.persistence.UserRefreshTokenRepository;
import com.study.totee.common.ApiResponse;
import com.study.totee.api.dto.SignUpDTO;
import com.study.totee.api.model.UserEntity;
import com.study.totee.api.model.UserInfoEntity;
import com.study.totee.config.properties.AppProperties;
import com.study.totee.oauth.entity.ProviderType;
import com.study.totee.oauth.entity.RoleType;
import com.study.totee.oauth.entity.UserPrincipal;
import com.study.totee.oauth.token.AuthToken;
import com.study.totee.oauth.token.AuthTokenProvider;
import com.study.totee.api.service.UserService;
import com.study.totee.utils.CookieUtil;
import com.study.totee.utils.HeaderUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    // private final TokenProvider tokenProvider;
    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

//    @ApiOperation(value = "회원가입", notes = "유저의 정보로 회원가입합니다.")
//    @PostMapping("/signup")
//    public ApiResponse registerUser(@RequestBody SignUpDTO signUpDTO){
//        // 요청을 이용해 저장할 사용자 만들기
//        LocalDateTime now = LocalDateTime.now();
//
//        UserEntity userEntity = UserEntity.builder()
//                .id(signUpDTO.getId())
//                .email(signUpDTO.getEmail())
//                .username(signUpDTO.getUsername())
//                .password(passwordEncoder.encode(signUpDTO.getPassword()))
//                .providerType(ProviderType.LOCAL)
//                .roleType(RoleType.USER)
//                .emailVerifiedYn("N")
//                .profileImageUrl("www")
//                .createdAt(now)
//                .modifiedAt(now)
//                .build();
//
//        UserInfoEntity userInfoEntity = UserInfoEntity.builder()
//                .major(signUpDTO.getMajor())
//                .grade(signUpDTO.getGrade())
//                .user(userEntity)
//                .build();
//
//        userEntity.setUserInfo(userInfoEntity);
//
//        // 서비스를 이용해 리포지토리에 사용자 저장
//        userService.create(userEntity, userInfoEntity);
//
//        return ApiResponse.success("message" , "SUCCESS");
//    }

//    @ApiOperation(value = "로그인", notes = "이메일과 비밀번호로 로그인합니다.")
//    @PostMapping("/signin")
//    public ApiResponse authenticate(@RequestBody SignInDTO signInDTO) {
//        UserEntity user = userService.getByCredentials(
//                signInDTO.getEmail(),
//                signInDTO.getPassword(),
//                passwordEncoder);
//
//        if(user != null) {
//            // 토큰 생성
//            final String token = tokenProvider.create(user);
//
//            final SignInDTO responseSignInDTO = SignInDTO.builder()
//                    .email(user.getEmail())
//                    .username(user.getUsername())
//                    .token(token)
//                    .build();
//            return ApiResponse.success("data", responseSignInDTO);
//        } else {
//            return ApiResponse.fail("message","Login failed");
//        }
//    }

    @PostMapping("/login")
    public ApiResponse login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody AuthReqModel authReqModel
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authReqModel.getId(),
                        authReqModel.getPassword()
                )
        );

        String userId = authReqModel.getId();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Date now = new Date();
        AuthToken accessToken = tokenProvider.createAuthToken(
                userId,
                ((UserPrincipal) authentication.getPrincipal()).getRoleType().getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        // userId refresh token 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userId);
        if (userRefreshToken == null) {
            // 없는 경우 새로 등록
            userRefreshToken = new UserRefreshToken(userId, refreshToken.getToken());
            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
        } else {
            // DB에 refresh 토큰 업데이트
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        return ApiResponse.success("token", accessToken.getToken());
    }

    @GetMapping("/refresh")
    public ApiResponse refreshToken (HttpServletRequest request, HttpServletResponse response) {
        // access token 확인
        String accessToken = HeaderUtil.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
        if (!authToken.validate()) {
            return ApiResponse.invalidAccessToken();
        }

        // expired access token 인지 확인
        Claims claims = authToken.getExpiredTokenClaims();
        if (claims == null) {
            return ApiResponse.notExpiredTokenYet();
        }

        String userId = claims.getSubject();
        RoleType roleType = RoleType.of(claims.get("role", String.class));

        // refresh token
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        if (authRefreshToken.validate()) {
            return ApiResponse.invalidRefreshToken();
        }

        // userId refresh token 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
        if (userRefreshToken == null) {
            return ApiResponse.invalidRefreshToken();
        }

        Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(
                userId,
                roleType.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
        if (validTime <= THREE_DAYS_MSEC) {
            // refresh 토큰 설정
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            authRefreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );

            // DB에 refresh 토큰 업데이트
            userRefreshToken.setRefreshToken(authRefreshToken.getToken());

            int cookieMaxAge = (int) refreshTokenExpiry / 60;
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
        }

        return ApiResponse.success("token", newAccessToken.getToken());
    }

}
