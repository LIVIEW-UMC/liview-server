package umc.liview.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.liview.user.service.UserService;

@RestController //레스트 컨트롤러
@RequiredArgsConstructor
//@RequestMapping("/follow") // 대표주소 설정
public class UserController {
    // 생성자 방식인데 빌더로 바꿔보자 담에
    private final UserService userService;

    @GetMapping("/")
    public void userJoin() {
        userService.join();

    }

    @PostMapping("/follow/{user_id}/{follower_id}")
    public void follow(@PathVariable("user_id") Long user_id,@PathVariable("follower_id") Long follower_id){
        //user를 어떻게 넣어줘야하지 ??? 내가 그냥 유저 조인해서 임의로 테스트해줘야하나
        userService.followUser(user_id,follower_id);
    }
}
