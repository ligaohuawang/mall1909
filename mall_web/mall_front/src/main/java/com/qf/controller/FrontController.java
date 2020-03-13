package com.qf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("front")
public class FrontController {

    @RequestMapping("/toIndexPage")
    public String toIndexPage() {
        return "index";
    }

}
