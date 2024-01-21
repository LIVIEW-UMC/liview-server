package umc.liview.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.liview.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow") // 대표주소
public class UserController {
    // 생성자 방식인데 빌더로 바꿔보자 담에
    private final UserService userService;

    @PostMapping("/{id}")










}
