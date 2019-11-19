package cn.com.wanwei.bic.controller;

import cn.com.wanwei.common.model.Org;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.common.utils.RequestUtil;
import cn.com.wanwei.common.utils.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

public class BaseController {

    @Autowired
    protected ObjectMapper mapper;

    @Value("${spring.profiles.active}")
    private String active;

    @Value("${wtcp.bic.appCode}")
    protected Integer appCode;

    @Value("${wtcp.bic.ruleId}")
    protected Long ruleId;

    /**
     * 获取当前用户
     */
    protected User getCurrentUser() throws Exception {
        String accessToken = RequestUtil.getAccessToken();
        if (Strings.isNullOrEmpty(accessToken)) {
            throw new UnauthorizedUserException("登录超时，请重新登录");
        }
        User user = UserUtil.getInstance().getUser(accessToken);
        if (user == null) {
            throw new UnauthorizedUserException("登录超时，请重新登录");
        }
        return user;
//        if("dev".equals(active)){
//            User user = new User();
//            user.setId(-1L);
//            user.setUsername("admin");
//            user.setName("admin");
//            user.setPhone("18119477981");
//            Org org = new Org();
//            org.setCode("-999");
//            user.setOrg(org);
//            return user;
//        }else{
//            if (Strings.isNullOrEmpty(accessToken)) {
//                throw new UnauthorizedUserException("登录超时，请重新登录");
//            }
//            User user = UserUtil.getInstance().getUser(accessToken);
//            if (user == null) {
//                throw new UnauthorizedUserException("登录超时，请重新登录");
//            }
//            return user;
//        }
    }
}
