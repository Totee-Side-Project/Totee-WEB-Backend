//package com.study.totee.api.persistence;
//
//import com.study.totee.api.model.*;
//import com.study.totee.exption.BadRequestException;
//import com.study.totee.exption.ErrorCode;
//import com.study.totee.type.PeriodType;
//import com.study.totee.type.ProviderType;
//import com.study.totee.type.RoleType;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static com.study.totee.api.util.EntityFactory.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@Transactional
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class CommentRepositoryTest {
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Test
//    @Order(1)
//    @Rollback(value = false)
//    public void 댓글_등록() {
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
//        // when
//        final Comment savedCommentEntity = commentRepository.save(commentEntity);
//
//        // then
//        assertThat(savedCommentEntity.getId()).isGreaterThan(0);
//        assertThat(savedCommentEntity.getContent()).isEqualTo("TEST");
//        assertThat(savedCommentEntity.getProfileImageUrl()).isEqualTo(userEntity.getProfileImageUrl());
//        assertThat(savedCommentEntity.getPost().getId()).isEqualTo(postEntity.getId());
//        assertThat(savedCommentEntity.getUser().getId()).isEqualTo(userEntity.getId());
//    }
//
//    @Test
//    @Order(2)
//    public void 댓글_조회() {
//        // given
//
//        // when
//        final Comment foundCommentEntity = commentRepository.findById(1L).orElse(null);
//
//        // then
//        assertThat(foundCommentEntity).isNotNull();
//    }
//
//    @Test
//    @Order(3)
//    public void 댓글_내댓글조회() {
//        // given
//        final User userEntity = userRepository.findById("TEST");
//
//        // when
//        final Comment foundCommentEntity = commentRepository.findByIdAndUser(1L, userEntity);
//
//        // then
//        assertThat(foundCommentEntity.getId()).isEqualTo(1L);
//    }
//
//    @Test
//    @Order(4)
//    public void 댓글_포스트아이디로조회() {
//        // given
//        final Post postEntity = postRepository.findById(1L).orElseThrow(
//                () -> new BadRequestException(ErrorCode.NO_POST_ERROR));
//
//        // when
//        final List<Comment> foundCommentEntity = commentRepository.findCommentEntityByPost_Id(postEntity.getId());
//
//        // then
//        assertThat(foundCommentEntity.size()).isEqualTo(1);
//    }
//
//    @Test
//    @Order(5)
//    @Rollback(value = false)
//    public void 댓글_수정(){
//        // given
//        final Comment commentEntity = commentRepository.findById(1L).orElseThrow(
//                () -> new BadRequestException(ErrorCode.NO_COMMENT_ERROR));
//
//        // when
//        commentEntity.setContent("TEST_UPDATE");
//        final Comment updatedCommentEntity = commentRepository.findById(1L).orElseThrow(
//                () -> new BadRequestException(ErrorCode.NO_COMMENT_ERROR));;
//        // then
//        assertThat(updatedCommentEntity.getContent()).isEqualTo("TEST_UPDATE");
//    }
//
//    @Test
//    @Order(6)
//    @Rollback(value = false)
//    public void 댓글_삭제(){
//        // given
//        final Comment commentEntity = commentRepository.findById(1L).orElseThrow(
//                () -> new BadRequestException(ErrorCode.NO_COMMENT_ERROR));;
//        // when
//        commentRepository.delete(commentEntity);
//
//        // then
//        assertThat(commentRepository.findAll().size()).isEqualTo(0);
//    }
//
//    @Test
//    @Order(7)
//    @Rollback(value = false)
//    public void 초기화(){
//        this.commentRepository.deleteAll();
//        this.postRepository.deleteAll();
//        this.userRepository.deleteAll();
//        this.categoryRepository.deleteAll();
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
//    }
//}
