package com.edu.service.impl;

import com.edu.bean.TbContent;
import com.edu.bean.TbContentExample;
import com.edu.bean.TbItem;
import com.edu.bean.TbItemExample;
import com.edu.common.bean.EUDatagridResult;
import com.edu.common.bean.ShoppingResult;
import com.edu.common.util.HttpClientUtil;
import com.edu.mapper.TbContentMapper;
import com.edu.service.ContentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.edu.service.ContentService;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;
    @Value("${REDIS_CACHE_BASE}")
    private String REDIS_CACHE_BASE;
    @Value("${CONTENT_CATEGORY_PIC}")
    private String CONTENT_CATEGORY_PIC;
    @Override
    public EUDatagridResult getAll(Long categoryId, int page, int rows) {
        PageHelper.startPage(page,rows);
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> contentList = tbContentMapper.selectByExampleWithBLOBs(example);
        PageInfo pageInfo = new PageInfo(contentList);
        EUDatagridResult result = new EUDatagridResult();
        result.setRows(pageInfo.getList());
        result.setTotal(pageInfo.getTotal());



        return result;
    }

    @Override
    public ShoppingResult insertContent(TbContent tbContent) {
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());
        tbContentMapper.insertSelective(tbContent);

        //使用HTTPClient进行远程调用
        HttpClientUtil.doGet(REDIS_CACHE_BASE+CONTENT_CATEGORY_PIC+"/"+tbContent.getCategoryId());
        return ShoppingResult.ok();
    }
}
