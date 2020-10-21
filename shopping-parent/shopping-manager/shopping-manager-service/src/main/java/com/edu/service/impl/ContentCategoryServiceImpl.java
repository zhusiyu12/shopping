package com.edu.service.impl;

import com.edu.bean.TbContentCategory;
import com.edu.bean.TbContentCategoryExample;
import com.edu.common.bean.EUTreeResult;
import com.edu.common.bean.ShoppingResult;
import com.edu.mapper.TbContentCategoryMapper;
import com.edu.mapper.TbContentMapper;
import com.edu.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;
    @Override
    public List<EUTreeResult> getAll(Long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> contentCategories = contentCategoryMapper.selectByExample(example);
        List<EUTreeResult> treeResults = new ArrayList<>();
        for (TbContentCategory contentCategory:contentCategories){
            EUTreeResult treeResult = new EUTreeResult();
            treeResult.setId(contentCategory.getId());
            treeResult.setText(contentCategory.getName());
            treeResult.setState(contentCategory.getIsParent()?"closed":"open");
            treeResults.add(treeResult);

        }
        return treeResults;
    }

    @Override
    public ShoppingResult insertContentCategory(Long parentId, String name) {
       TbContentCategory tbContentCategory  = new TbContentCategory();
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);
        tbContentCategory.setStatus(1);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        //新增一个节点
        contentCategoryMapper.insertSelective(tbContentCategory);
        //判断这个节点是父节是否是叶子节点如果是就把叶子节点变成父节点
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()){
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKeySelective(parent);
        }
        return ShoppingResult.ok(tbContentCategory);
    }

    @Override
    public ShoppingResult updateContentCategory(Long id, String name) {
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        contentCategory.setName(name);
        contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);

        return ShoppingResult.ok();
    }

    @Override
    public ShoppingResult deleteContentCategory(Long id) {
        //根据Id获取到我们的分类
        TbContentCategory contentCategory =contentCategoryMapper.selectByPrimaryKey(id);
        if (contentCategory.getIsParent()){
            //如果下面有叶子节点需要递归删除
          caseCatAll(id);
        }else {
            contentCategoryMapper.deleteByPrimaryKey(id);
            Long parentId = contentCategory.getParentId();
            TbContentCategoryExample example = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(parentId);
            List<TbContentCategory> contentCategories=contentCategoryMapper.selectByExample(example);
            if (contentCategories ==null || contentCategories.size()==0){
                TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
                parent.setIsParent(false);
                contentCategoryMapper.updateByPrimaryKeySelective(parent);
            }
        }

        return ShoppingResult.ok();
    }

    private void caseCatAll(Long id) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(id);
        List<TbContentCategory> contentCategories = contentCategoryMapper.selectByExample(example);
        for (TbContentCategory contentCategory:contentCategories){
            if (contentCategory.getIsParent()){
                caseCatAll(contentCategory.getId());
            }else{
                contentCategoryMapper.deleteByPrimaryKey(contentCategory.getId());
            }

        }
        contentCategoryMapper.deleteByPrimaryKey(id);



    }
}
