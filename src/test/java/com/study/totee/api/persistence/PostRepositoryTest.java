//package com.study.totee.api.persistence;
//
//import com.study.totee.api.model.Category;
//import com.study.totee.api.model.Position;
//import com.study.totee.api.model.Post;
//import com.study.totee.api.model.User;
//import com.study.totee.exption.BadRequestException;
//import com.study.totee.exption.ErrorCode;
//import com.study.totee.type.PeriodType;
//import com.study.totee.type.PositionType;
//import com.study.totee.type.ProviderType;
//import com.study.totee.type.RoleType;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.time.LocalDateTime;
//import java.util.HashSet;
//
//import static com.study.totee.api.util.EntityFactory.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@Transactional
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class PostRepositoryTest {
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
//    private PositionRepository positionRepository;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Test
//    @Order(1)
//    @Rollback(value = false)
//    public void 게시글_등록() {
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
//        Position positionEntity = new Position();
//        positionEntity.setPosition(PositionType.ANDROID);
//
//        HashSet<Position> positionEntityList = new HashSet<>();
//        positionEntityList.add(positionEntity);
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
//                .positionList(positionEntityList)
//                .build();
//
//
//        // when
//        final Post savedPostEntity = postRepository.save(postEntity);
//        positionEntity.setPost(savedPostEntity);
//        positionRepository.save(positionEntity);
//        savedPostEntity.setPositionList(positionEntityList);
//
//        // then
//
//        assertThat(savedPostEntity.getId()).isNotNull();
//        assertThat(savedPostEntity.getTitle()).isEqualTo("TEST");
//        assertThat(savedPostEntity.getContent()).isEqualTo("TEST");
//        assertThat(savedPostEntity.getUser().getUsername()).isEqualTo("TEST");
//        assertThat(savedPostEntity.getCategory().getCategoryName()).isEqualTo("프로젝트");
//        assertThat(savedPostEntity.getCreatedAt()).isNotNull();
//        assertThat(savedPostEntity.getModifiedAt()).isNotNull();
//        assertThat(savedPostEntity.getStatus()).isEqualTo("Y");
//        assertThat(savedPostEntity.getCommentNum()).isEqualTo(0);
//        assertThat(savedPostEntity.getView()).isEqualTo(0);
//        assertThat(savedPostEntity.getLikeNum()).isEqualTo(0);
//        assertThat(savedPostEntity.getRecruitNum()).isEqualTo("1~2명");
//        assertThat(savedPostEntity.getContactMethod()).isEqualTo("노션");
//        assertThat(savedPostEntity.getContactLink()).isEqualTo("https://www.naver.com");
//        assertThat(savedPostEntity.getPeriod()).isEqualTo(PeriodType.ShortTerm);
//        assertThat(savedPostEntity.getPositionList()).isNotNull();
//    }
//
//    @Test
//    @Order(2)
//    public void 게시글_아이디조회() {
//        // given
//
//        // when
//        final Post postEntity = postRepository.findById(1L).orElseThrow(
//                () -> new BadRequestException(ErrorCode.NO_POST_ERROR));
//
//        // then
//        assertThat(postEntity.getId()).isEqualTo(1L);
//        assertThat(postRepository.findAll().size()).isEqualTo(1);
//    }
//
//    @Test
//    @Order(3)
//    public void 게시글_본인게시글아이디조회() {
//        // given
//        final User userEntity = userRepository.findById("TEST");
//        // when
//        final Post postEntity = postRepository.findByIdAndUser(1L, userEntity);
//
//        // then
//        assertThat(postEntity.getId()).isEqualTo(1L);
//        assertThat(postRepository.findAll().size()).isEqualTo(1);
//    }
//
//    @Test
//    @Order(4)
//    public void 게시글_제목검색_페이징() {
//        // given
//        Pageable pageable = PageRequest.of(0, 5);
//        final String title = "TEST";
//        // when
//        final Page<Post> postEntities = postRepository.findAllByTitleContaining(title, pageable);
//        // then
//        assertThat(postEntities.getTotalElements()).isEqualTo(1);
//    }
//
//    @Test
//    @Order(5)
//    public void 게시글_전체조회_페이징() {
//        // given
//        Pageable pageable = PageRequest.of(0, 5);
//        // when
//        final Page<Post> postEntities = postRepository.findAll(pageable);
//
//        // then
//        assertThat(postEntities.getTotalElements()).isEqualTo(1);
//    }
//
//    @Test
//    @Order(6)
//    public void 게시글_프로젝트모집분야와일치하는게시글조회_페이징(){
//        // given
//        Pageable pageable = PageRequest.of(0, 5);
//
//        // when
//        final Page<Post> postEntities = postRepository.findAllByPosition(PositionType.ANDROID, pageable);
//
//        // then
//        assertThat(postEntities.getTotalElements()).isEqualTo(1);
//    }
//
//    @Test
//    @Order(7)
//    public void 게시글_내가작성한게시글목록조회_페이징(){
//        // given
//        Pageable pageable = PageRequest.of(0, 5);
//        // when
//        final Page<Post> postEntities = postRepository.findAllByUser_Id("TEST", pageable);
//
//        // then
//        assertThat(postEntities.getTotalElements()).isEqualTo(1);
//    }
//
//    @Test
//    @Order(8)
//    @Rollback(value = false)
//    public void 포스트_수정(){
//        // given
//        final Post postEntity = postRepository.findById(1L).orElseThrow(
//                () -> new BadRequestException(ErrorCode.NO_POST_ERROR));
//
//        // when
//        postEntity.setTitle("TEST2");
//        postEntity.setContent("TEST2");
//        final Post updatedPostEntity = postRepository.findById(1L).orElseThrow(
//                () -> new BadRequestException(ErrorCode.NO_POST_ERROR));
//
//        // then
//        assertThat(updatedPostEntity.getTitle()).isEqualTo("TEST2");
//        assertThat(updatedPostEntity.getContent()).isEqualTo("TEST2");
//    }
//
//    @Test
//    @Order(9)
//    @Rollback(value = false)
//    public void 포스트_삭제(){
//        // given
//        final Post postEntity = postRepository.findById(1L).orElseThrow(
//                () -> new BadRequestException(ErrorCode.NO_POST_ERROR));
//
//        // when
//        postRepository.delete(postEntity);
//
//        // then
//        assertThat(postRepository.findAll().size()).isEqualTo(0);
//    }
//
//    @Test
//    @Order(10)
//    @Rollback(value = false)
//    public void 초기화() {
//        this.postRepository.deleteAll();
//        this.positionRepository.deleteAll();
//        this.userRepository.deleteAll();
//        this.categoryRepository.deleteAll();
//
//        this.entityManager
//                .createNativeQuery("ALTER TABLE tb_post ALTER COLUMN `post_id` RESTART WITH 1")
//                .executeUpdate();
//        this.entityManager
//                .createNativeQuery("ALTER TABLE tb_position ALTER COLUMN `position_id` RESTART WITH 1")
//                .executeUpdate();
//        this.entityManager
//                .createNativeQuery("ALTER TABLE tb_user ALTER COLUMN `user_seq` RESTART WITH 1")
//                .executeUpdate();
//        this.entityManager
//                .createNativeQuery("ALTER TABLE tb_category ALTER COLUMN `category_id` RESTART WITH 1")
//                .executeUpdate();
//    }
//}
