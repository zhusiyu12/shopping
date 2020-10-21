package com.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String list(){
        return "index";
    }

    @RequestMapping("/{path}")
    public String show(@PathVariable String path){
        return path;
    }


}
