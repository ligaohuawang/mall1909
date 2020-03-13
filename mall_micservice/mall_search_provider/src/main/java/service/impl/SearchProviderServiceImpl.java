package service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.entity.Goods;
import com.qf.service.ISearchProviderService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchProviderServiceImpl implements ISearchProviderService {

    //注入Solr客户端对象
    @Autowired
    private SolrClient solrClient;

    /**
     * 根据信息查询索引库
     *
     * @param information
     * @return
     */
    @Override
    public List<Goods> queryGoodsListByInformation(String information) {
        //构造条件对象
        SolrQuery solrQuery = new SolrQuery();
        //如果查询条件不为空或空字符串
        if (information != null && !information.equals("")) {
            solrQuery.setQuery("subject:" + information + " || info:" + information);
        } else {
            solrQuery.setQuery("*:*");
        }

        //设置搜索的高亮
        //开启高亮
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre("<font color='red'>");//前缀
        solrQuery.setHighlightSimplePost("</font>");//后缀
        solrQuery.addHighlightField("subject");

        try {
            //得到响应
            QueryResponse queryResponse = solrClient.query(solrQuery);

            //获得高亮后的结果
            //Map<id, 高亮信息>
            //高亮信息 -   Map<字段, 高亮内容的集合>
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();


            //获得搜索的结果
            SolrDocumentList results = queryResponse.getResults();
            List<Goods> goodsList = new ArrayList<>();
            for (SolrDocument document : results) {
                Goods goods = new Goods();
                goods.setId(Integer.parseInt((String) document.get("id")));
                goods.setSubject(document.get("subject") + "")
                        .setSave((int) document.get("save"))
                        .setPrice(BigDecimal.valueOf((double) document.get("price")))
                        .setFongmianurl(document.get("image") + "");


                //处理高亮结果
                if (highlighting.containsKey(goods.getId() + "")) {
                    //说明当前商品有高亮信息
                    //依次获取高亮的字段
                    Map<String, List<String>> stringListMap = highlighting.get(goods.getId() + "");
                    List<String> subject = stringListMap.get("subject");
                    if (subject != null) {
                        goods.setSubject(subject.get(0));
                    }
                }


                goodsList.add(goods);
            }
            return goodsList;
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 添加商品到数据库时同步索引库
     *
     * @param good
     * @return
     */
    @Override
    public int addGoodsToSolr(Goods good) {
        //构建文档对象参数
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        //索引库字段id为string类型
        solrInputDocument.addField("id", good.getId() + "");
        solrInputDocument.addField("subject", good.getSubject());
        solrInputDocument.addField("info", good.getInfo());
        solrInputDocument.addField("image", good.getFongmianurl());
        //索引库字段price为pdouble类型
        solrInputDocument.addField("price", good.getPrice().doubleValue());
        solrInputDocument.addField("save", good.getSave());
        //Solr客户端对象调用add方法添加数据，文档对象做参数
        try {
            solrClient.add(solrInputDocument);
            //提交。增删改都要提交
            solrClient.commit();
            return 1;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
