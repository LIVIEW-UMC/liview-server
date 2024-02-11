package umc.liview.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.liview.config.auth.JwtUserDetails;
import umc.liview.user.dto.UserRequestDTO;
import umc.liview.user.dto.UserResponseDTO;
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
