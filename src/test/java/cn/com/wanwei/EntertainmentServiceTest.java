package cn.com.wanwei;

import cn.com.wanwei.bic.BicApplication;
import cn.com.wanwei.bic.entity.EntertainmentEntity;
import cn.com.wanwei.bic.service.EntertainmentService;
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
public class EntertainmentServiceTest {

    @Autowired
    private EntertainmentService entertainmentService;

    private EntertainmentEntity entertainmentEntity;

    private User user;

    @Before
    public void before(){
        System.out.println("----------------休闲娱乐管理接口单元测试开始 ---------------------");
        entertainmentEntity=new EntertainmentEntity();
        entertainmentEntity.setId("-11111");
        entertainmentEntity.setDeptCode("111111");
        entertainmentEntity.setCode("123");
        entertainmentEntity.setStatus(0);
        entertainmentEntity.setWeight(4);
        entertainmentEntity.setAddress("ceshi");
        entertainmentEntity.setContent("ceshi");
        entertainmentEntity.setDescription("ceshi1");
        entertainmentEntity.setLatitude(12.111);
        entertainmentEntity.setLongitude(12.111);
        entertainmentEntity.setRegion("ceshi");
        entertainmentEntity.setRegionFullCode("12,13");
        entertainmentEntity.setRegionFullName("甘肃省，兰州市，城关区");
        entertainmentEntity.setSlogan("ceshi1");
        entertainmentEntity.setSubTitle("1233");
        entertainmentEntity.setSummary("123123");
        entertainmentEntity.setTitle("qeq23");
        entertainmentEntity.setType(123);
        entertainmentEntity.setWithinPark(1);
        entertainmentEntity.setWithinScenic(1);
        //user填充数据
        user=new User();
        user.setUsername("ceshi");
        Org org=new Org();
        org.setCode("111");
        user.setOrg(org);
    }
    @After
    public void after(){
        System.out.println("---------------- 休闲娱乐管理接口单元测试结束 ---------------------");
    }

    @Test
    public void findByPageTest(){
        System.out.println("---------------休闲娱乐信息分页列表---------------");
        Map<String,Object> filter= Maps.newHashMap();
        ResponseMessage back = entertainmentService.findByPage(1, 10,filter);
        System.out.println("返回值：" +back.getData() );
        Assert.assertSame("返回值是1", true, back.getData()!=null?true:false);
    }

    @Test
    public void findTest(){
        System.out.println("---------------查询休闲娱乐信息---------------");
        Map<String,Object>filter= Maps.newHashMap();
        ResponseMessage back = entertainmentService.find("-11111");
        System.out.println("返回值：" +back.getData() );
        Assert.assertSame("返回值是1", true, back.getData()==null?true:false);
    }

    @Test
    @Rollback
    public void createTest() {
        System.out.println("---------------新增休闲娱乐信息---------------");
        ResponseMessage back = entertainmentService.create(entertainmentEntity, user);
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是1", 1, status);
    }

    @Test
    @Rollback
    public void updateTest() {
        System.out.println("---------------编辑休闲娱乐信息---------------");
        ResponseMessage back = entertainmentService.update("-11111",entertainmentEntity, user);
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是0", 0, status);
    }

    @Test
    @Rollback
    public void deleteTest() {
        System.out.println("---------------删除休闲娱乐信息---------------");
        ResponseMessage back = entertainmentService.delete("-11111");
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是1", 1, status);
    }

//    @Test
//    @Rollback
//    public void goWeightTest() {
//        System.out.println("---------------权重更改---------------");
//        ResponseMessage back = entertainmentService.goWeight("-11111",Float.valueOf("1"), user);
//        int status=  back.getStatus();
//        System.out.println("返回值：" + status);
//        Assert.assertSame("返回值是0", 0, status);
//    }

    @Test
    public void checkTitleTest(){
        System.out.println("---------------标题重名校验---------------");
        ResponseMessage back = entertainmentService.checkTitle("-11111","cesh");
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是1", 1, status);
    }
}
