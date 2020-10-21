package com.edu.rest.service.impl;


import com.alibaba.druid.util.StringUtils;
import com.edu.bean.TbContent;
import com.edu.bean.TbContentExample;
import com.edu.common.util.JsonUtils;
import com.edu.mapper.TbContentMapper;
import com.edu.rest.dao.RedisDao;
import com.edu.rest.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContentServiceImpl implements ContentService {
   @Autowired
   private TbContentMapper tbContentMapper;

    @Value("${INDEX_CACHE_CONTENT_PIC}")
    private String INDEX_CACHE_CONTENT_PIC;
   @Autowired
   private RedisDao redisDao;

    @Override
    public List<Map<String, Object>> getAllByCategoryId(Long categoryId) {
        List<TbContent> contentsList = null;
        try {
            String str = redisDao.hget(INDEX_CACHE_CONTENT_PIC,categoryId+"");
            if(!StringUtils.isEmpty(str)){
                contentsList=JsonUtils.jsonToList(str,TbContent.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (contentsList==null){
            TbContentExample example = new TbContentExample();
            TbContentExample.Criteria criteria = example.createCriteria();
            criteria.andCategoryIdEqualTo(categoryId);
            contentsList = tbContentMapper.selectByExampleWithBLOBs(example);
            try {
                //把数据放到缓存中
                String result = JsonUtils.objectToJson(contentsList);
                redisDao.hset(INDEX_CACHE_CONTENT_PIC,categoryId+"",result);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        List<Map<String,Object>> lists = new ArrayList<>();

        for (TbContent content : contentsList){
            Map<String,Object> map = new HashMap<>();
            map.put("src",content.getPic());
            map.put("height",240);
            map.put("width",670);
            map.put("alt",content.getSubTitle());
            map.put("srcB",content.getPic2());
            map.put("widthB",550);
            map.put("heightB",240);
            map.put("href",content.getUrl());
            lists.add(map);
        }

        return lists;
    }
}
