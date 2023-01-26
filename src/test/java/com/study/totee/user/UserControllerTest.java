//package com.study.totee.user;
//
//import com.google.gson.Gson;
//import com.study.totee.api.controller.UserController;
//import com.study.totee.api.dto.user.UserInfoRequestDto;
//import com.study.totee.api.dto.user.UserInfoUpdateRequestDto;
//import com.study.totee.api.model.UserInfo;
//import com.study.totee.api.service.UserService;
//import com.study.totee.handler.RestApiExceptionHandler;
//import com.study.totee.api.model.User;
//import com.study.totee.type.ProviderType;
//import com.study.totee.type.RoleType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import javax.swing.*;
//
//import java.security.Principal;
//import java.time.LocalDateTime;
//
//import static com.study.totee.util.EntityFactory.userInfoRequestDto;
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//public class UserControllerTest {
//
//    @InjectMocks
//    private UserController target;
//
//    @Mock
//    private UserService userService;
//
//
//    private MockMvc mockMvc;
//    private Gson gson;
//
//    private final UserInfoRequestDto requestDto = new UserInfoRequestDto();
//    private final UserInfoUpdateRequestDto updateRequestDto = new UserInfoUpdateRequestDto();
//
//    @BeforeEach
//    public void init() {
//        mockMvc = MockMvcBuilders.standaloneSetup(target).build();
//        gson = new Gson();
//    }
//
//    @Test
//    public void 로그인한_유저정보조회() throws Exception {
//        // given
//
//
//        final String url = "/api/v1/info";
//        UserInfo userInfo = new UserInfo();
//        userInfo.setUserInfo(userInfoRequestDto(1), null);
//
//        User user = new User(
//                "user1",
//                "user1",
//                "user1",
//                "Y",
//                "user1",
//                ProviderType.NAVER,
//                RoleType.USER,
//                LocalDateTime.now(),
//                LocalDateTime.now(),
//                userInfo
//        );
//        userInfo.setUser(user);
//        Principal mockPrincipal = Mockito.mock(Principal.class);
//        Mockito.when(mockPrincipal.getName()).thenReturn("me");
//
//        given(userService.getUser(any(String.class))).willReturn(user);
//
//        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
//                        .principal(mockPrincipal)
//                        .accept(MediaType.APPLICATION_JSON);
//
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//        MockHttpServletResponse response = result.getResponse();
//        assertThat(response.getStatus()).isEqualTo(200);
//    }
//}
