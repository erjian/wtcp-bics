package cn.com.wanwei;

import cn.com.wanwei.bic.BicApplication;
import cn.com.wanwei.bic.entity.PoiEntity;
import cn.com.wanwei.bic.service.PoiService;
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
public class PoiServiceTest {

    @Autowired
    private PoiService poiService;

    private PoiEntity poiEntity;

    private User user;

    @Before
    public void before(){
        System.out.println("---------------- poi管理接口单元测试开始 ---------------------");
        poiEntity=new PoiEntity();
        poiEntity.setId("-11111");
        poiEntity.setDeptCode("111111");
        poiEntity.setCode("123");
        poiEntity.setStatus(0);
        poiEntity.setWeight(4);
        poiEntity.setAddress("ceshi");
        poiEntity.setContent("ceshi");
        poiEntity.setDescription("ceshi1");
        poiEntity.setLatitude(12.111);
        poiEntity.setLongitude(12.111);
        poiEntity.setParentId("1");
        poiEntity.setPrincipalId("1");
        poiEntity.setRegion("ceshi");
        poiEntity.setRegionFullCode("12,13");
        poiEntity.setRegionFullName("甘肃省，兰州市，城关区");
        poiEntity.setSlogan("ceshi1");
        poiEntity.setSubTitle("1233");
        poiEntity.setSummary("123123");
        poiEntity.setTitle("qeq23");
        poiEntity.setType("123");
        //user填充数据
        user=new User();
        user.setUsername("ceshi");
        Org org=new Org();
        org.setCode("111");
        user.setOrg(org);
    }
    @After
    public void after(){
        System.out.println("---------------- poi管理接口单元测试结束 ---------------------");
    }

    @Test
    public void findByPageTest(){
        System.out.println("---------------poi信息分页列表---------------");
        Map<String,Object>filter= Maps.newHashMap();
        ResponseMessage back = poiService.findByPage(1, 10,filter);
        System.out.println("返回值：" +back.getData() );
        Assert.assertSame("返回值是1", true, back.getData()!=null?true:false);
    }

    @Test
    public void findTest(){
        System.out.println("---------------查询poi信息---------------");
        Map<String,Object>filter= Maps.newHashMap();
        ResponseMessage back = poiService.find("-11111");
        System.out.println("返回值：" +back.getData() );
        Assert.assertSame("返回值是1", true, back.getData()==null?true:false);
    }

    @Test
    @Rollback
    public void createTest() {
        System.out.println("---------------新增poi信息---------------");
        ResponseMessage back = poiService.create(poiEntity, user);
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是1", 1, status);
    }

    @Test
    @Rollback
    public void updateTest() {
        System.out.println("---------------编辑poi信息---------------");
        ResponseMessage back = poiService.update("-11111",poiEntity, user);
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是0", 0, status);
    }

    @Test
    @Rollback
    public void deleteTest() {
        System.out.println("---------------删除poi信息---------------");
        ResponseMessage back = poiService.delete("-11111");
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是1", 1, status);
    }

    @Test
    @Rollback
    public void goWeightTest() {
        System.out.println("---------------权重更改---------------");
        ResponseMessage back = poiService.goWeight("-11111",1, user);
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是0", 0, status);
    }

    @Test
    public void checkTitleTest(){
        System.out.println("---------------标题重名校验---------------");
        ResponseMessage back = poiService.checkTitle("-11111","cesh");
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是1", 1, status);
    }
}
