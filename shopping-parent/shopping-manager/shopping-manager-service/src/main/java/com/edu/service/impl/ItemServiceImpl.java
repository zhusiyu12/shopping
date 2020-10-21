package com.edu.service.impl;

import com.edu.bean.TbItem;
import com.edu.bean.TbItemDesc;
import com.edu.bean.TbItemExample;
import com.edu.bean.TbItemParamItem;
import com.edu.common.bean.EUDatagridResult;
import com.edu.common.bean.ShoppingResult;
import com.edu.mapper.TbItemDescMapper;
import com.edu.mapper.TbItemMapper;
import com.edu.mapper.TbItemParamItemMapper;
import com.edu.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edu.util.IDUtils;


import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Override
    public EUDatagridResult getAll(int page, int rows) {
        PageHelper.startPage(page,rows);
        TbItemExample example = new TbItemExample();
        List<TbItem> itemList = itemMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(itemList);
        EUDatagridResult datagridResult = new EUDatagridResult();
        datagridResult.setRows(pageInfo.getList());
        datagridResult.setTotal(pageInfo.getTotal());
        return datagridResult;
    }

    @Override
    public ShoppingResult insertItem(TbItem item, String desc, String itemParams) {
        long itemId = IDUtils.genItemId();
        item.setId(itemId);
        item.setStatus((byte)1);
        item.setCreated(new Date());
        item.setUpdated(new Date());

        itemMapper.insertSelective(item);

        ShoppingResult result = insertItemDesc(itemId,desc);

        if (result.getStatus() == 200){
            result=insertItemParamsItem(itemId,itemParams);
            if (result.getStatus() == 200){
                return result;
            } else {
                throw new RuntimeException();
            }
        } else{
            throw new RuntimeException();
        }


    }

    private ShoppingResult insertItemParamsItem(long itemId, String itemParams) {
        TbItemParamItem itemParamItem = new TbItemParamItem();
        itemParamItem.setItemId(itemId);
        itemParamItem.setParamData(itemParams);
        itemParamItem.setCreated(new Date());
        itemParamItem.setUpdated(new Date());
        itemParamItemMapper.insertSelective(itemParamItem);
        return ShoppingResult.ok();
    }

    private ShoppingResult insertItemDesc(long itemId, String desc) {
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        itemDescMapper.insertSelective(itemDesc);
        return ShoppingResult.ok();
    }
}
