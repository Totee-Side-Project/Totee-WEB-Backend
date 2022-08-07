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
//import static com.study.totee.api.util.EntityFactory.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@Transactional
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class ReplyRepositoryTest {
//
//    @Autowired
//    private ReplyRepository replyRepository;
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
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Test
//    @Order(1)
//    @Rollback(value = false)
//    public void 답글_등록() {
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
//        User savedUserEntity = userRepository.save(userEntity);
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
//        final Post savedPostEntity = postRepository.save(postEntity);
//
//        final Comment commentEntity = Comment.builder()
//                .id(1L)
//                .post(postEntity)
//                .user(userEntity)
//                .content("TEST")
//                .build();
//
//        final Comment savedCommentEntity = commentRepository.save(commentEntity);
//
//        final Reply replyEntity = Reply.builder()
//                .id(1L)
//                .nickname("TEST")
//                .comment(commentEntity)
//                .user(userEntity)
//                .content("TEST")
//                .build();
//
//        // when
//        Reply savedReplyEntity = replyRepository.save(replyEntity);
//
//        // then
//        assertThat(savedReplyEntity.getId()).isGreaterThan(0);
//        assertThat(savedReplyEntity.getUser()).isEqualTo(savedUserEntity);
//        assertThat(savedReplyEntity.getComment()).isEqualTo(savedCommentEntity);
//        assertThat(savedReplyEntity.getContent()).isEqualTo("TEST");
//        assertThat(savedReplyEntity.getCreatedAt()).isNotNull();
//        assertThat(savedReplyEntity.getModifiedAt()).isNotNull();
//        assertThat(savedReplyEntity.getNickname()).isEqualTo("TEST");
//        assertThat(savedReplyEntity.getUser().getProfileImageUrl()).isEqualTo(userEntity.getProfileImageUrl());
//
//    }
//
//    @Test
//    @Order(2)
//    public void 답글_내답글조회() {
//        // given
//        final User foundUserEntity = userRepository.findById("TEST");
//
//        // when
//        final Reply foundReplyEntity = replyRepository.findByIdAndUser(1L, foundUserEntity);
//
//        // then
//        assertThat(foundReplyEntity.getId()).isEqualTo(1L);
//    }
//
//    @Test
//    @Order(3)
//    public void 답글_특정코멘트에달린답글리스트조회(){
//        // given
//        final Comment foundCommentEntity = commentRepository.findById(1L).get();
//
//        // when
//        final List<Reply> foundReplyEntityList = replyRepository.findReplyEntityByComment_Id(foundCommentEntity.getId());
//
//        // then
//        assertThat(foundReplyEntityList.size()).isEqualTo(1);
//    }
//
//    @Test
//    @Order(4)
//    @Rollback(value = false)
//    public void 답글_수정(){
//        // given
//        final User foundUserEntity = userRepository.findById("TEST");
//        final Reply foundReplyEntity = replyRepository.findByIdAndUser(1L, foundUserEntity);
//
//        // when
//        foundReplyEntity.setContent("TEST_UPDATE");
//        final Reply updatedReplyEntity = replyRepository.findByIdAndUser(1L, foundUserEntity);
//
//        // then
//        assertThat(updatedReplyEntity.getContent()).isEqualTo("TEST_UPDATE");
//    }
//
//    @Test
//    @Order(5)
//    @Rollback(value = false)
//    public void 답글_삭제(){
//        // given
//        final User foundUserEntity = userRepository.findById("TEST");
//        final Reply foundReplyEntity = replyRepository.findByIdAndUser(1L, foundUserEntity);
//
//        // when
//        replyRepository.delete(foundReplyEntity);
//        final Reply deletedReplyEntity = replyRepository.findByIdAndUser(1L, foundUserEntity);
//
//        // then
//        assertThat(deletedReplyEntity).isNull();
//    }
//
//    @Test
//    @Order(6)
//    @Rollback(value = false)
//    public void 초기화(){
//        this.commentRepository.deleteAll();
//        this.postRepository.deleteAll();
//        this.userRepository.deleteAll();
//        this.categoryRepository.deleteAll();
//        this.replyRepository.deleteAll();
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
//                .createNativeQuery("ALTER TABLE tb_reply ALTER COLUMN `reply_id` RESTART WITH 1")
//                .executeUpdate();
//    }
//}
