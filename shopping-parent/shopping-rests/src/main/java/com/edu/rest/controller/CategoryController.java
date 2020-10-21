package com.edu.rest.controller;

import com.edu.common.util.JsonUtils;
import com.edu.rest.bean.CatResultNode;
import com.edu.rest.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.awt.*;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ResponseBody
    @RequestMapping(value = "itemcat/all",produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    public String all(String callback){
        CatResultNode cateResultNode = categoryService.getAll();
        String result = JsonUtils.objectToJson(cateResultNode);
        return callback+"("+ result +");";
    }
}
