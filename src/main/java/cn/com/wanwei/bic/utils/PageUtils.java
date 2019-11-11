package cn.com.wanwei.bic.utils;

import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public class PageUtils {

    private static PageUtils instance = new PageUtils();

    private PageUtils() {
    }

    public static PageUtils getInstance() {
        return instance;
    }

    public MybatisPageRequest setPage(Integer page, Integer size, Map<String, Object> filter, Sort.Direction orderBy, String... sortFields){
        List<Sort.Order> sortList = Lists.newArrayList();
        if(filter.containsKey("orderBy") && StringUtils.equals("asc", filter.get("orderBy").toString().toLowerCase())){
            orderBy = Sort.Direction.ASC;
        }
        if(filter.containsKey("sortField") && null != filter.get("sortField") && StringUtils.isNotEmpty(filter.get("sortField").toString())){
            sortList.add(new Sort.Order(orderBy, filter.get("sortField").toString()));
        }else{
            for(String field : sortFields){
                sortList.add(new Sort.Order(orderBy, field));
            }
        }
        Sort sort = Sort.by(sortList);
        MybatisPageRequest pageRequest = MybatisPageRequest.of(page, size, sort);
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize());
        PageHelper.orderBy(pageRequest.getOrders());
        return pageRequest;
    }

}
