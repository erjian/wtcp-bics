package cn.com.wanwei.bic.utils;

import cn.com.wanwei.common.utils.RequestUtil;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * @author 崔振元
 */
public class PageUtils {

    private static PageUtils instance = new PageUtils();

    private PageUtils() {
    }

    public static PageUtils getInstance() {
        return instance;
    }

    public static final String TOKEN_KEY = "access_token";

    public static final String SORT_FIELD_KEY = "sortField";

    public static final String ORDER_BY_KEY = "orderBy";

    public MybatisPageRequest setPage(Integer page, Integer size, Map<String, Object> filter, Sort.Direction orderBy, String... sortFields) {
        List<Sort.Order> sortList = Lists.newArrayList();
        if (filter.containsKey(ORDER_BY_KEY)) {
            if (StringUtils.equals("asc", filter.get(ORDER_BY_KEY).toString().toLowerCase())) {
                orderBy = Sort.Direction.ASC;
            } else {
                orderBy = Sort.Direction.DESC;
            }
        }
        if (filter.containsKey(SORT_FIELD_KEY) && null != filter.get(SORT_FIELD_KEY) && StringUtils.isNotEmpty(filter.get(SORT_FIELD_KEY).toString())) {
            sortList.add(new Sort.Order(orderBy, filter.get(SORT_FIELD_KEY).toString()));
        } else {
            for (String field : sortFields) {
                sortList.add(new Sort.Order(orderBy, field));
            }
        }
        Sort sort = Sort.by(sortList);
        MybatisPageRequest pageRequest = MybatisPageRequest.of(page, size, sort);
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize());
        PageHelper.orderBy(pageRequest.getOrders());
        return pageRequest;
    }

    public void setToken(Map<String, Object> filter) {
        if (filter.containsKey(TOKEN_KEY) && StringUtils.isNotEmpty(TOKEN_KEY)) {
            RequestUtil.getRequest().setAttribute(TOKEN_KEY, filter.get(TOKEN_KEY));
        }
    }

}
