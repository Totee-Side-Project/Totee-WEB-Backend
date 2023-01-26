//package com.study.totee.util;
//
//import com.study.totee.api.dto.category.CategoryRequestDto;
//import com.study.totee.api.dto.category.CategoryResponseDto;
//import com.study.totee.api.dto.category.CategoryUpdateRequestDto;
//import com.study.totee.api.dto.user.UserInfoRequestDto;
//import com.study.totee.api.dto.user.UserInfoUpdateRequestDto;
//import com.study.totee.api.model.*;
//import com.study.totee.type.PeriodType;
//import com.study.totee.type.PositionType;
//import com.study.totee.type.ProviderType;
//import com.study.totee.type.RoleType;
//
//import java.time.LocalDateTime;
//
//public class EntityFactory {
////
////    private static LocalDateTime today = LocalDateTime.now();
//
//    public static CategoryRequestDto categoryRequestDto(int idx) {
//        return CategoryRequestDto.builder()
//                .categoryName("프로젝트" + idx)
//                .build();
//    }
//
//    public static CategoryUpdateRequestDto categoryUpdateRequestDto(int idx) {
//        return CategoryUpdateRequestDto.builder()
//                .categoryName("프로젝트" + idx)
//                .newCategoryName("프로젝트" + idx + "수정")
//                .build();
//    }
//
//    public static UserInfoRequestDto userInfoRequestDto(int idx) {
//        return UserInfoRequestDto.builder()
//                .nickname("user" + idx)
//                .position(PositionType.FRONT_END)
//                .build();
//    }
//
//    public static UserInfoUpdateRequestDto userInfoUpdateRequestDto(int idx) {
//        return UserInfoUpdateRequestDto.builder()
//                .nickname("user" + idx + "수정")
//                .position(PositionType.FRONT_END)
//                .build();
//    }
//
////    public static Post post(User user, Category category){
////
////        return Post.builder()
////                .title("TEST")
////                .content("TEST")
////                .user(user)
////                .category(category)
////                .period(PeriodType.ShortTerm)
////                .onlineOrOffline("ONLINE")
////                .status("Y")
////                .commentNum(0)
////                .view(0)
////                .likeNum(0)
////                .recruitNum("1~2명")
////                .contactMethod("노션")
////                .contactLink("https://www.naver.com")
////                .createdAt(today)
////                .modifiedAt(today)
////                .build();
////    }
////
////    public static UserInfo userInfo(){
////        return UserInfo.builder()
////                .nickname("TEST")
////                .positionList(null)
////                .position(PositionType.BACK_END)
////                .createdAt(today)
////                .modifiedAt(today)
////                .build();
////
////    }
////
////    public static User user(){
////
////        UserInfo userInfoEntity = userInfo();
////
////        return User.builder()
////                .id("TEST")
////                .username("TEST")
////                .email("NO_MAIL")
////                .emailVerifiedYn("Y")
////                .profileImageUrl("NO_IMAGE")
////                .providerType(ProviderType.LOCAL)
////                .password("NO_PASSWORD")
////                .roleType(RoleType.USER)
////                .userInfo(userInfoEntity)
////                .createdAt(today)
////                .modifiedAt(today)
////                .build();
////    }
////
////    public static Comment comment(User user, Post post){
////        return Comment.builder()
////                .user(user)
////                .post(post)
////                .content("TEST")
////                .profileImageUrl("TEST")
////                .build();
////    }
////
////    public static Like like(){
////        return Like.builder()
////                .build();
////    }
////
////    public static Reply reply(){
////        return Reply.builder()
////                .content("TEST")
////                .nickname("TEST")
////                .profileImageUrl("TEST")
////                .build();
////    }
////
////    public static Notification notification(User user, Post post){
////        return Notification.builder()
////                .user(user)
////                .post(post)
////                .generator("TEST")
////                .content("TEST 님이 댓글을 남겼습니다.")
////                .isRead("N")
////                .build();
////    }
//
//}
//
