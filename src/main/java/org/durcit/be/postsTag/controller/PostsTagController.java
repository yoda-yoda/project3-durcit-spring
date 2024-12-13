package org.durcit.be.postsTag.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostsTagController {

    @GetMapping("/api/")
    public String index() {
        return "index";
    }


}
