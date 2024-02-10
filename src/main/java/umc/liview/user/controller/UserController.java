package umc.liview.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.liview.config.auth.JwtUserDetails;
import umc.liview.user.service.UserService;

@RestController //레스트 컨트롤러
@RequiredArgsConstructor
//@RequestMapping("/follow") // 대표주소 설정
public class UserController {
    // 생성자 방식인데 빌더로 바꿔보자 담에
    private final UserService userService;


    @PostMapping("/follow/{follower_id}")
    public void follow(@PathVariable("follower_id") Long follower_id,
                       @AuthenticationPrincipal JwtUserDetails jwtUserDetails){

        Long user_id = jwtUserDetails.getUserId();;
        userService.followUser(follower_id,user_id);
    }
}
