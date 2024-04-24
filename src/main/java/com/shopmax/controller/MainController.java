package com.shopmax.controller;

import com.shopmax.dto.MemberFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping(value = "/") // localhost/
    public String main() {
        return "main"; // 사이트 메인화면(main.html) 띄움
    }
}
