package com.edu.search.controller;


import com.ctc.wstx.util.ExceptionUtil;
import com.edu.common.bean.SearchResult;
import com.edu.common.bean.ShoppingResult;
import com.edu.search.service.SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService ;
    @ResponseBody
    @RequestMapping("/query")
    public ShoppingResult search(@RequestParam("q") String queryString,
                                 @RequestParam(value = "page",defaultValue = "1") Integer page,
                                 @RequestParam(value = "rows",defaultValue = "60") Integer rows) {
        if(StringUtils.isEmpty(queryString)) {
            return ShoppingResult.build(500,"查询失败");
        }
        try {
            queryString = new String(queryString.getBytes("iso8859-1"),"UTF-8");
            SearchResult searchResult = searchService.search(queryString,page,rows);
            return ShoppingResult.ok(searchResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ShoppingResult.build(500, "查询出错了");
        }
    }
}
