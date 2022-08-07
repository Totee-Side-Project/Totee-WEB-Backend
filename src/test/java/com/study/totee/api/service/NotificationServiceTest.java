//package com.study.totee.api.service;
//
//import com.study.totee.api.dto.notification.NotificationResponseDto;
//import com.study.totee.api.model.*;
//import com.study.totee.api.persistence.*;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//import static com.study.totee.api.util.EntityFactory.*;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@Slf4j
//@ExtendWith(MockitoExtension.class)
//public class NotificationServiceTest {
//
//    @InjectMocks
//    private NotificationService target;
//
//    @Mock
//    private NotificationRepository notificationRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PostRepository postRepository;
//
//    @Mock
//    private CommentRepository commentRepository;
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @Test
//    public void 알림목록조회(){
//        // given
//        User savedUserEntity = userRepository.save(user());
//        Category savedCategoryEntity = categoryRepository.save(category());
//        Post savedPostEntity = postRepository.save(post(savedUserEntity, savedCategoryEntity));
//        Notification savedNotificationEntity = notificationRepository.save(notification(savedUserEntity, savedPostEntity));
//
//        final User user = userRepository.findById("TEST");
//        final Post post = postRepository.findById(1L).get();
//        doReturn(Arrays.asList(notification(user, post))).when(notificationRepository).findAll();
//
//        // when
//        final List<NotificationResponseDto> result = target.notificationDtoList("TEST");
//
//        // then
//        assertThat(result.size()).isEqualTo(1);
//    }
//}
