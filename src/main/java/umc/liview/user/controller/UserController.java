package umc.liview.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.liview.config.auth.JwtUserDetails;
import umc.liview.user.dto.UserRequestDTO;
import umc.liview.user.dto.UserResponseDTO;
import umc.liview.user.service.UserService;

import java.util.List;

@RestController //레스트 컨트롤러
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/follow/{follower_id}")
    public void follow(@PathVariable("follower_id") Long follower_id,
                       @AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        userService.followUser(follower_id, extractUserId(jwtUserDetails));
    }

    @GetMapping("/users")
    public UserResponseDTO.SimpleProfile userSimple(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        Long userId = extractUserId(jwtUserDetails);
        return userService.getSimpleProfile(userId);
    }

    @GetMapping("/users/{userId}")
    public UserResponseDTO.SimpleProfile userSimple(@PathVariable("userId") Long userId){
        return userService.getSimpleProfile(userId);
    }

    @GetMapping("/users/detail")
    public UserResponseDTO.UserProfile userProfile(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        return userService.getUserProfile(extractUserId(jwtUserDetails));
    }

    @PutMapping("/users/myInfo")
    public void putUserInfo(@AuthenticationPrincipal JwtUserDetails jwtUserDetails,
                            @RequestPart(value = "userProfile") UserRequestDTO.PutUserProfile userProfile,
                            @RequestPart(value = "file", required = false) MultipartFile file){
        userService.putUserProfile(extractUserId(jwtUserDetails), userProfile, file);
    }

    @GetMapping("/users/privacy-info")
    public UserResponseDTO.UserPrivacy getUserPrivacy(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        return userService.getPrivacyInfo(extractUserId(jwtUserDetails));
    }

    @PatchMapping("/users/email-approval")
    public void patchEmailApproval(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        userService.patchEmailPrivacy(extractUserId(jwtUserDetails));
    }

    @PatchMapping("/users/board-approval")
    public void patchBoardApproval(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        userService.patchBoardPrivacy(extractUserId(jwtUserDetails));
    }

    @DeleteMapping("/users/myInfo")
    public void deleteUser(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        userService.deleteUser(extractUserId(jwtUserDetails));
    }

    @GetMapping("/users/myId")
    public Long getMyIdController(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        return extractUserId(jwtUserDetails);
    }

    // 검색기록 조회
    @GetMapping("/users/search/log")
    public List<String> findSearchLogs(@AuthenticationPrincipal JwtUserDetails jwtUserDetails) {
        return userService.findSearchLogs(extractUserId(jwtUserDetails));
    }

    // 검색기록 삭제
    @DeleteMapping("/users/search/log")
    public void deleteSearchLog(@RequestParam String value, @AuthenticationPrincipal JwtUserDetails jwtUserDetails) {
        userService.deleteSearchLog(extractUserId(jwtUserDetails), value);
    }

    private long extractUserId(JwtUserDetails jwtUserDetails) {
        return jwtUserDetails.getUserId();
    }
}
