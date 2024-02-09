package umc.liview.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.liview.user.domain.User;
import umc.liview.user.service.UserService;
import umc.liview.user.web.dto.UserRequestDTO;
import umc.liview.user.web.dto.UserResponseDTO;

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

    @GetMapping("/users")
    public UserResponseDTO.SimpleProfile userSimple(@RequestParam Long userId){
        return userService.getSimpleProfile(userId);
    }

    @GetMapping("/users/detail")
    public UserResponseDTO.UserProfile userProfile(@RequestParam Long userId){
        return userService.getUserProfile(userId);
    }

    @PutMapping("/users/myInfo")
    public void putUserInfo(@RequestBody UserRequestDTO.PutUserProfile userProfile){
        userService.putUserProfile(userProfile);
    }

    @GetMapping("/users/privacy-info")
    public UserResponseDTO.UserPrivacy getUserPrivacy(@RequestParam Long userId){
        return userService.getPrivacyInfo(userId);
    }

    @PatchMapping("/users/email-approval")
    public void patchEmailApproval(@RequestBody UserRequestDTO.UserId userId){
        userService.patchEmailPrivacy(userId);
    }

    @PatchMapping("/users/board-approval")
    public void patchBoardApproval(@RequestBody UserRequestDTO.UserId userId){
        userService.patchBoardPrivacy(userId);
    }

    @DeleteMapping("/users/myinfo")
    public void deleteUser(@RequestBody UserRequestDTO.UserId userId){
        userService.deleteUser(userId);
    }
}
