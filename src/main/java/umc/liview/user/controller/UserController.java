package umc.liview.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.liview.config.auth.JwtUserDetails;
import umc.liview.user.dto.UserRequestDTO;
import umc.liview.user.dto.UserResponseDTO;
import umc.liview.user.service.UserService;

@RestController //레스트 컨트롤러
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/follow/{follower_id}")
    public void follow(@PathVariable("follower_id") Long follower_id,
                       @AuthenticationPrincipal JwtUserDetails jwtUserDetails){

        Long user_id = jwtUserDetails.getUserId();;
        userService.followUser(follower_id,user_id);
    }

    @GetMapping("/users")
    public UserResponseDTO.SimpleProfile userSimple(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        Long userId = jwtUserDetails.getUserId();
        return userService.getSimpleProfile(userId);
    }

    @GetMapping("/users/detail")
    public UserResponseDTO.UserProfile userProfile(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        Long userId = jwtUserDetails.getUserId();
        return userService.getUserProfile(userId);
    }

    @PutMapping("/users/myInfo")
    public void putUserInfo(@AuthenticationPrincipal JwtUserDetails jwtUserDetails,
                            @RequestPart(value = "userProfile") UserRequestDTO.PutUserProfile userProfile,
                            @RequestPart(value = "file", required = false) MultipartFile file){
        Long userId = jwtUserDetails.getUserId();
        userService.putUserProfile(userId, userProfile, file);
    }

    @GetMapping("/users/privacy-info")
    public UserResponseDTO.UserPrivacy getUserPrivacy(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        Long userId = jwtUserDetails.getUserId();
        return userService.getPrivacyInfo(userId);
    }

    @PatchMapping("/users/email-approval")
    public void patchEmailApproval(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        Long userId = jwtUserDetails.getUserId();
        userService.patchEmailPrivacy(userId);
    }

    @PatchMapping("/users/board-approval")
    public void patchBoardApproval(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        Long userId = jwtUserDetails.getUserId();
        userService.patchBoardPrivacy(userId);
    }

    @DeleteMapping("/users/myInfo")
    public void deleteUser(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        Long userId = jwtUserDetails.getUserId();
        userService.deleteUser(userId);
    }

    @GetMapping("/users/myId")
    public Long getMyIdController(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        return jwtUserDetails.getUserId();
    }
}
