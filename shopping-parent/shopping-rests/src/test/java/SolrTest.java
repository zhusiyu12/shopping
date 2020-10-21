import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class SolrTest {

    /**
     * 删除
     * @throws Exception
     */
    @Test
    public void testDEl() throws Exception {
        SolrServer solrServer = new HttpSolrServer("http://192.168.43.49:8080/solr/");
//        solrServer.deleteById("test003");
        solrServer.deleteByQuery("*:*");
        solrServer.commit();
    }

    /**
     * 查询
     * @throws Exception
     */
    @Test
    public void testquery() throws Exception {
        SolrServer solrServer = new HttpSolrServer("http://192.168.43.49:8080/solr/");
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        QueryResponse respons = solrServer.query(query);
        SolrDocumentList results = respons.getResults();
        for (SolrDocument result:results){
            String id = (String)result.get("id");
            String item_title = (String)result.get("item_title");
            System.out.println(id + "::" + item_title);
        }
    }
    /**
     * 修改
     * @throws Exception
     */
    @Test
    public void testupdate() throws Exception {
        SolrServer solrServer = new HttpSolrServer("http://192.168.43.49:8080/solr/");
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id","test003");
        document.setField("item_title","测试修改商品一");
        document.setField("item_price",6000);
        solrServer.add(document);
        solrServer.commit();
    }

    /**
     * 新增
     * @throws Exception
     */
    @Test
    public void testSava() throws Exception {
        SolrServer solrServer = new HttpSolrServer("http://192.168.43.49:8080/solr/");
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id","test003");
        document.setField("item_title","测试商品三");
        document.setField("item_price",6000);
        solrServer.add(document);
        solrServer.commit();
    }
}
