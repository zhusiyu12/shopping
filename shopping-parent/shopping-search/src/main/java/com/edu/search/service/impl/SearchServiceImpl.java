package com.edu.search.service.impl;


import com.edu.common.bean.SearchResult;
import com.edu.search.dao.SearchDao;
import com.edu.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao searchDao ;
    @Override
    public SearchResult search(String queryString, Integer page, Integer rows) {
        // 封装查询的条件
        SolrQuery query = new SolrQuery();
        query.setQuery(queryString);
        query.setStart(rows*(page -1));
        query.setRows(rows);
        // 设置默认查询的域
        query.set("df","item_keywords");
        // 设置是否高亮显示
        query.setHighlight(true);
        // 设置highlight的域
        query.addHighlightField("item_title");
        // 设置highlight
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em>");
        SearchResult result = null;
        try {
            result = searchDao.search(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 计算查询结果的总页数
        long rowCount = result.getRowCount() ;
        long pageCount = rowCount % page == 0 ? rowCount / page :(rowCount/page+1);
        result.setCurrentPage(page);
        result.setPageCount((int)pageCount);
        return result;
    }
}
