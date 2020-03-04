package cn.com.wanwei;

import cn.com.wanwei.bic.BicApplication;
import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.service.AuditLogService;
import cn.com.wanwei.common.model.Org;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import com.google.common.collect.Maps;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BicApplication.class)
@Transactional
public class AuditLogServiceTest {

    @Autowired
    private AuditLogService auditLogService;

    private AuditLogEntity auditLogEntity;

    private User user;

    @Before
    public void before(){
        System.out.println("----------------审核记录管理接口单元测试开始 ---------------------");
        auditLogEntity=new AuditLogEntity();
        auditLogEntity.setOpinion("测试审核");
        auditLogEntity.setPreStatus(0);
        auditLogEntity.setPrincipalId("-1111111");
        auditLogEntity.setStatus(0);
        auditLogEntity.setType(1);
        //user填充数据
        user=new User();
        user.setUsername("ceshi");
        Org org=new Org();
        org.setCode("111");
        user.setOrg(org);
    }
    @After
    public void after(){
        System.out.println("---------------- 审核记录管理接口单元测试结束 ---------------------");
    }

    @org.junit.Test
    public void findByPageTest(){
        System.out.println("---------------审核记录管理信息分页列表---------------");
        Map<String,Object> filter= Maps.newHashMap();
        filter.put("principalId","-1111111");
        ResponseMessage back = auditLogService.findByPage(1, 10,filter);
        System.out.println("返回值：" +back.getData() );
        Assert.assertSame("返回值是1", true, back.getData()!=null?true:false);
    }

    @Test
    @Rollback
    public void createTest() {
        System.out.println("---------------新增审核记录信息---------------");
        ResponseMessage back = auditLogService.insert(auditLogEntity, user.getUsername());
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是1", 1, status);
    }
}
