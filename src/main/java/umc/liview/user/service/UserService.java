package umc.liview.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.liview.community.repository.adapter.PostRedisAdapter;
import umc.liview.config.s3.AmazonS3Manager;
import umc.liview.exception.NotFoundException;
import umc.liview.exception.code.ErrorCode;
import umc.liview.user.converter.UserConverter;
import umc.liview.user.domain.Follow;
import umc.liview.user.domain.FollowRepositiory;
import umc.liview.user.domain.User;
import umc.liview.user.domain.UserRepository;
import umc.liview.user.dto.UserDTO;
import umc.liview.user.dto.UserRequestDTO;
import umc.liview.user.dto.UserResponseDTO;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FollowRepositiory followRepository;
    private final AmazonS3Manager s3Manager;
    private final PostRedisAdapter postRedisAdapter;

    //Refactor
    //ServiceImpl 추가,DAO 추가, Autiowired로 레포지토리 의존성 주입,DataHandler ?


    // 팔로워 아이디넣고
    // 유저를 넣고

    // 팔로우 레파지토리에
    // 이 (유저, 팔로워)의 쌍을 찾아야함. 어캐하지  ?딜리트 하면 될 거 같은데


    @Transactional
    public void followUser(Long followerId, Long userId) {

        Optional<Follow> optionalFollow = followRepository.findByFollowerIdAndUserId(followerId, userId);
        // 팔로우 한 이력이 있으면 언팔
        if (optionalFollow.isPresent()) {
            Follow follow = optionalFollow.get();
            followRepository.delete(follow);
        }
        // 팔로우 한 이력이 없으면 팔로우
        else {
            // null 일 결우 에러 해야겠지 ?
            //following
            Optional<User> optionalUser = userRepository.findById(userId);
            User followingUser = optionalUser.get();

            Follow follow = Follow.builder()
                    .followerId(followerId)
                    .user(followingUser)
                    .build();

            followRepository.save(follow);

        }
    }


    @Transactional
    public UserResponseDTO.SimpleProfile getSimpleProfile(Long userId){
        User userById = getUserById(userId);
        Long follower = getFollower(userId);
        Long following = getFollowing(userId);
        return UserConverter.toSimpleProfile(userById, follower, following);
    }

    @Transactional
    public UserResponseDTO.UserProfile getUserProfile(Long userId){
        User userById = getUserById(userId);
        return UserConverter.toUserProfile(userById);
    }

    @Transactional
    public void putUserProfile(Long userId, UserRequestDTO.PutUserProfile userProfile, MultipartFile file){
        User userById = getUserById(userId);
        if (!file.isEmpty()){
            String imgURL = s3Manager.uploadFile(file);
            userById.modifyUserProfile(userProfile, imgURL);
        }
        else{
            userById.modifyUserProfile(userProfile);
        }
    }

    @Transactional
    public UserResponseDTO.UserPrivacy getPrivacyInfo(Long userId){
        User userById = getUserById(userId);
        return UserConverter.toUserPrivacy(userById);
    }

    @Transactional
    public void patchEmailPrivacy(Long userId){
        User userById = getUserById(userId);
        userById.getPrivacyStatus().handleEmailPrivacy();
    }

    @Transactional
    public void patchBoardPrivacy(Long userId){
        User userById = getUserById(userId);
        userById.getPrivacyStatus().handleBoardPrivacy();
    }

    @Transactional
    public void deleteUser(Long userId){
        User userById = getUserById(userId);
        followRepository.deleteAllByFollowerId(userId);
        followRepository.deleteAllByUser(userById);
        userRepository.delete(userById);
    }

    // 검색기록 조회
    @Transactional(readOnly = true)
    public List<String> findSearchLogs(Long userId) {
        return postRedisAdapter.findSearchedLogs(userId);
    }

    // 검색기록 삭제
    @Transactional
    public void deleteSearchLog(Long userId, String log) {
        postRedisAdapter.deleteSearchedLogs(userId, log);
    }

    private User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND, userId));
    }

    private Long getFollower(Long userId){
        Optional<Long> opFollower = followRepository.countFollowByUserId(userId);
        return opFollower.orElseGet(() -> 0L);
    }

    private Long getFollowing(Long userId){
        Optional<Long> opFollowing = followRepository.countFollowByFollowerId(userId);
        return opFollowing.orElseGet(() -> 0L);
    }

}
