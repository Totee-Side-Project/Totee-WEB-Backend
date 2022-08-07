//package com.study.totee.api.persistence;
//
//import com.study.totee.api.model.*;
//import com.study.totee.type.PeriodType;
//import com.study.totee.type.ProviderType;
//import com.study.totee.type.RoleType;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static com.study.totee.api.util.EntityFactory.user;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@Transactional
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class NotificationRepositoryTest {
//
//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Test
//    @Order(1)
//    @Rollback(value = false)
//    public void 알림_생성() {
//        // given
//        LocalDateTime today = LocalDateTime.now();
//
//        final Category categoryEntity = Category.builder()
//                .categoryName("프로젝트")
//                .build();
//        categoryRepository.save(categoryEntity);
//
//        final User userEntity = User.builder()
//                .id("TEST")
//                .username("TEST")
//                .email("NO_MAIL")
//                .emailVerifiedYn("Y")
//                .profileImageUrl("NO_IMAGE")
//                .providerType(ProviderType.LOCAL)
//                .password("NO_PASSWORD")
//                .roleType(RoleType.USER)
//                .createdAt(today)
//                .modifiedAt(today)
//                .build();
//        userRepository.save(userEntity);
//
//        final Post postEntity = Post.builder()
//                .id(1L)
//                .title("TEST")
//                .content("TEST")
//                .user(user())
//                .period(PeriodType.ShortTerm)
//                .onlineOrOffline("ONLINE")
//                .status("Y")
//                .commentNum(0)
//                .view(0)
//                .likeNum(0)
//                .recruitNum("1~2명")
//                .contactMethod("노션")
//                .contactLink("https://www.naver.com")
//                .createdAt(today)
//                .modifiedAt(today)
//                .user(userEntity)
//                .category(categoryEntity)
//                .build();
//        postRepository.save(postEntity);
//
//        final Comment commentEntity = Comment.builder()
//                .id(1L)
//                .profileImageUrl(userEntity.getProfileImageUrl())
//                .post(postEntity)
//                .user(userEntity)
//                .content("TEST")
//                .build();
//        commentRepository.save(commentEntity);
//
//        final Notification notificationEntity = Notification.builder()
//                .id(1L)
//                .user(commentEntity.getPost().getUser())
//                .post(commentEntity.getPost())
//                .generator(commentEntity.getUser().getUsername())
//                .content(commentEntity.getUser().getUsername() + " 님이 댓글을 남겼습니다.")
//                .isRead("N")
//                .build();
//
//        // when
//        final Notification savedNotification = notificationRepository.save(notificationEntity);
//
//        // then
//        assertThat(savedNotification.getId()).isNotNull();
//        assertThat(savedNotification.getContent()).isEqualTo("TEST 님이 댓글을 남겼습니다.");
//        assertThat(savedNotification.getGenerator()).isEqualTo("TEST");
//        assertThat(savedNotification.getIsRead()).isEqualTo("N");
//        assertThat(savedNotification.getPost().getId()).isEqualTo(1L);
//        assertThat(savedNotification.getUser().getId()).isEqualTo("TEST");
//    }
//
//    @Test
//    @Order(2)
//    public void 알림_특정알림조회() {
//        // given
//        final User userEntity = userRepository.findById("TEST");
//        // when
//        final Notification notification = notificationRepository.findByIdAndUser(1L, userEntity);
//
//        // then
//        assertThat(notification.getId()).isEqualTo(1L);
//
//    }
//
//    @Test
//    @Order(3)
//    public void 알림_유저알림전체조회() {
//        // given
//        final User userEntity = userRepository.findById("TEST");
//        // when
//        final List<Notification> notificationList = notificationRepository.findAllByUser(userEntity);
//        // then
//        assertThat(notificationList.size()).isEqualTo(1);
//    }
//
//    @Test
//    @Order(4)
//    public void 알림_읽음처리() {
//        // given
//        final User userEntity = userRepository.findById("TEST");
//        // when
//        final Notification notification = notificationRepository.findByIdAndUser(1L, userEntity);
//        notification.setIsRead("Y");
//        notificationRepository.save(notification);
//        // then
//        assertThat(notification.getIsRead()).isEqualTo("Y");
//    }
//
//    @Test
//    @Order(5)
//    public void 알림_삭제(){
//        // given
//        final User userEntity = userRepository.findById("TEST");
//        final Notification notification = notificationRepository.findByIdAndUser(1L, userEntity);
//        // when
//        notificationRepository.delete(notification);
//        // then
//        assertThat(notificationRepository.findAllByUser(userEntity).size()).isEqualTo(0);
//    }
//
//    @Test
//    @Order(6)
//    @Rollback(value = false)
//    public void 초기화(){
//        this.commentRepository.deleteAll();
//        this.postRepository.deleteAll();
//        this.categoryRepository.deleteAll();
//        this.notificationRepository.deleteAll();
//        this.userRepository.deleteAll();
//
//        this.entityManager
//                .createNativeQuery("ALTER TABLE tb_post ALTER COLUMN `post_id` RESTART WITH 1")
//                .executeUpdate();
//        this.entityManager
//                .createNativeQuery("ALTER TABLE tb_comment ALTER COLUMN `comment_id` RESTART WITH 1")
//                .executeUpdate();
//        this.entityManager
//                .createNativeQuery("ALTER TABLE tb_user ALTER COLUMN `user_seq` RESTART WITH 1")
//                .executeUpdate();
//        this.entityManager
//                .createNativeQuery("ALTER TABLE tb_category ALTER COLUMN `category_id` RESTART WITH 1")
//                .executeUpdate();
//        this.entityManager
//                .createNativeQuery("ALTER TABLE tb_notification ALTER COLUMN `notification_id` RESTART WITH 1")
//                .executeUpdate();
//    }
//}