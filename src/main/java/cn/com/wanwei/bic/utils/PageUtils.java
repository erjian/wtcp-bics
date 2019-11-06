package cn.com.wanwei.bic.utils;

import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Sort;

import java.util.List;

public class PageUtils {

    private static PageUtils instance = new PageUtils();

    private PageUtils() {
    }

    public static PageUtils getInstance() {
        return instance;
    }

    public MybatisPageRequest setPage(Integer page, Integer size, Sort.Direction sortBy, String... sortFields){
        List<Sort.Order> sortList = Lists.newArrayList();
        for(String field : sortFields){
            sortList.add(new Sort.Order(sortBy, field));
        }
        Sort sort = Sort.by(sortList);
        MybatisPageRequest pageRequest = MybatisPageRequest.of(page, size, sort);
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize());
        PageHelper.orderBy(pageRequest.getOrders());
        return pageRequest;
    }

}
