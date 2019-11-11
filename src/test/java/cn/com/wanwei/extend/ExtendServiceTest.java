package cn.com.wanwei.extend;

import cn.com.wanwei.bic.BicApplication;
import cn.com.wanwei.bic.entity.ExtendEntity;
import cn.com.wanwei.bic.model.ExtendModel;
import cn.com.wanwei.bic.service.ExtendService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhanglei
 * @project wtcp-bics
 * @date 2019/10/21 15:02:02
 * @desc 扩展信息单元测试.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BicApplication.class)
@Transactional
public class ExtendServiceTest {

    @Autowired
    private ExtendService extendService;

    private ExtendEntity extendEntity;
    private ExtendModel extendModel;
    private User user;

    @Before
    public void before() {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        System.out.println("---------------- 扩展信息管理接口单元测试开始 ---------------------");
        extendEntity = new ExtendEntity();
        extendEntity.setId("TEST_1001");
        extendEntity.setCode("1001");
        extendEntity.setStatus(0);
        extendEntity.setPrincipalId("0eab670e70344f54b9e1246b31507c36");
        extendEntity.setContent("testetsdgsuygfsofoda");
        extendEntity.setDescription("描述审贷会sadISO");
        extendEntity.setPrice(Float.valueOf("4.00"));
        extendEntity.setSlogan("dfhaoisfpuif");
        extendEntity.setSubTitle("sdgfisoadfiosda");
        extendEntity.setSummary("hdshgfapsdfpuia");
        extendEntity.setTitle("title");
        extendEntity.setType(1);
        extendEntity.setWeight(Float.valueOf("4.00"));
        extendEntity.setCreatedUser("zhanglei");
        extendEntity.setCreatedDate(new Date());
        extendEntity.setWeight(Float.valueOf("4.00"));
        extendModel.setExtendEntity(extendEntity);
        extendModel.setList(list);
    }
    @After
    public void after(){
        System.out.println("---------------- 扩展信息管理接口单元测试结束 ---------------------");
    }

    @Test
    public void findByPageTest() throws Exception{
        System.out.println("---------------扩展信息分页列表---------------");
        Map<String,Object> filter = Maps.newHashMap();
        ResponseMessage back = extendService.findByPage(1,10,user,filter);
        System.out.println("返回值：" +back.getData() );
        Assert.assertSame("返回值是1", true, back.getData()!=null?true:false);
    }

    @Test
    @Rollback
    public void createTest() throws Exception{
        System.out.println("---------------新增扩展信息---------------");
        ResponseMessage back = extendService.save(extendModel,user, Long.valueOf("1001"), 1002);
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是1", 1, status);
    }

    @Test
    @Rollback
    public void updateTest() throws Exception{
        System.out.println("---------------编辑扩展信息---------------");
        ResponseMessage back = extendService.edit("-111111111",extendModel,user);
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是0", 0, status);
    }

    @Test
    @Rollback
    public void deleteTest() throws Exception{
        System.out.println("---------------删除扩展信息---------------");
        ResponseMessage back = extendService.deleteByPrimaryKey("-11111111111");
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是1", 1, status);
    }

    @Test
    public void findTest() throws Exception{
        System.out.println("---------------查询扩展信息详情---------------");
        Map<String,Object>filter= Maps.newHashMap();
        ResponseMessage back = extendService.selectByPrimaryKey("-11111");
        System.out.println("返回值：" +back.getData() );
        Assert.assertSame("返回值是1", true, back.getData()==null?true:false);
    }

}
