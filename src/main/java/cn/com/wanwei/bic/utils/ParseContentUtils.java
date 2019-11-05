package cn.com.wanwei.bic.utils;

import cn.com.wanwei.bic.entity.MaterialEntity;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.*;

@Slf4j
public class ParseContentUtils {

    private static ParseContentUtils instance = new ParseContentUtils();

    private ParseContentUtils() {
    }

    public static ParseContentUtils getInstance() {
        return instance;
    }

    /**
     * 根据富文本中的内容解析附件信息
     * @param content 内容
     * @return ResponseMessage
     */
    public List<MaterialEntity> parse(String content, String principalId, User user) {
        List<MaterialEntity> backList = Lists.newArrayList();
        try {
            Document document = Jsoup.parse(content);
            backList.addAll(getContentEle(document, "image", principalId, user));
            backList.addAll(getContentEle(document, "audio", principalId, user));
            backList.addAll(getContentEle(document, "video", principalId, user));
        } catch (Exception e) {
            log.error("附件信息解析错误：关联信息ID：{}", principalId);
            log.error(e.getMessage());
        }
        return backList;
    }

    private List<MaterialEntity> getContentEle(Document document, String type, String principalId, User user){
        List<MaterialEntity> backList = Lists.newArrayList();
        Elements elementList = document.select(type.equals("image") ? "img" : type);
        for (Element element : elementList) {
            String url = element.attr("src");
            String subStr = "wtcp-file";
            if(url.contains("wtcp-mfs")){
                subStr = "wtcp-mfs";
            }
            if(url.contains(subStr)){
                url = url.substring(url.indexOf(subStr) + subStr.length());
                if (!Strings.isNullOrEmpty(url)) {
                    MaterialEntity materialEntity = new MaterialEntity();
                    materialEntity.setFileName(System.currentTimeMillis() + url.substring(url.lastIndexOf(".")));
                    materialEntity.setFileType(type);
                    materialEntity.setFileUrl(url);
                    materialEntity.setPrincipalId(principalId);
                    materialEntity.setCreatedDate(new Date());
                    materialEntity.setCreatedUser(user.getUsername());
                    materialEntity.setUpdatedUser(user.getUsername());
                    materialEntity.setUpdatedDate(new Date());
                    backList.add(materialEntity);
                }
            }
        }
        return backList;
    }

}
