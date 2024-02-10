package umc.liview.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.liview.user.domain.Follow;
import umc.liview.user.domain.FollowRepositiory;
import umc.liview.user.domain.User;
import umc.liview.user.domain.UserRepository;
import umc.liview.user.dto.UserDTO;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FollowRepositiory followRepository;

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

}
