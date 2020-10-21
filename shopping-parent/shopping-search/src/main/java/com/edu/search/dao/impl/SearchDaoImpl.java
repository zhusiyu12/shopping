package com.edu.search.dao.impl;

import com.edu.common.bean.Item;
import com.edu.common.bean.SearchResult;
import com.edu.search.dao.SearchDao;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDaoImpl implements SearchDao {
    @Autowired
    private SolrServer solrServer ;
    @Override
    public SearchResult search(SolrQuery query) throws Exception {
        SearchResult result = new SearchResult() ;
        QueryResponse response = solrServer.query(query);
        SolrDocumentList solrDocuments = response.getResults();
        result.setRowCount(solrDocuments.getNumFound());
        List<Item> itemList = new ArrayList<>();
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for(SolrDocument document:solrDocuments) {
            Item item = new Item();
            item.setId((String) document.get("id"));
            List<String> stringList = highlighting.get(document.get("id")).get("item_title");
            String title = "";
            if(stringList != null && stringList.size() > 0) {
                title = stringList.get(0);
            } else {
                title = (String) document.get("item_title");
            }
            item.setTitle(title);
            item.setSell_point((String) document.get("item_sell_point"));
            item.setPrice((Long) document.get("item_price"));
            item.setImage((String) document.get("item_image"));
            item.setCategory_name((String) document.get("item_category_name"));
            itemList.add(item);
        }
        result.setItem(itemList);
        return result;
    }
}
