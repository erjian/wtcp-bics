package cn.com.wanwei.destination;

import cn.com.wanwei.bic.BicApplication;
import cn.com.wanwei.bic.entity.DestinationEntity;
import cn.com.wanwei.bic.model.DestinationModel;
import cn.com.wanwei.bic.service.DestinationService;
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
 * @date 2019/10/11 17:02:02
 * @desc 目的地管理单元测试.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BicApplication.class)
@Transactional
public class DestinationServiceTest {

    @Autowired
    private DestinationService destinationService;

    private DestinationEntity destinationEntity;
    private DestinationModel destinationModel;
    private User user;

    @Before
    public void before() {
        System.out.println("---------------- 目的地管理接口单元测试开始 ---------------------");
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        destinationEntity = new DestinationEntity();
        destinationEntity.setId("TEST_1001");
        destinationEntity.setDeptCode("1001");
        destinationEntity.setStatus(0);
        destinationEntity.setDrinkIntroduce("hdjshfkjsdahfhsdahfjdsafkdsfkasfjdksafhsd");
        destinationEntity.setEatIntroduce("sdjjfsdafkashfuhoihafoasf");
        destinationEntity.setIntroduce("dshgfiusdafphdsiuafhuiadsfdosuafypuehfuipas");
        destinationEntity.setRegion("ceshi");
        destinationEntity.setRegionFullCode("12,13");
        destinationEntity.setEntertainmentIntroduce("jsdfhupiafhuipefhiepf");
        destinationEntity.setRegionFullName("甘肃省,兰州市,城关区");
        destinationEntity.setPlayIntroduce("jdshfuasdhfsouiahfiuopafhuioearf");
        destinationEntity.setShopIntroduce("sdgasdh;jfdasfiodhasodosa;ijfkl");
        destinationEntity.setTourismIntroduce("jsidfudashfdiosqafu[i0jeoihfuiefhuiosd");
        destinationEntity.setCreatedUser("zhanglei");
        destinationEntity.setCreatedDate(new Date());
        destinationEntity.setWeight(4);
        destinationModel.setDestinationEntity(destinationEntity);
        destinationModel.setList(list);
    }
    @After
    public void after(){
        System.out.println("---------------- 目的地管理接口单元测试结束 ---------------------");
    }

    @Test
    public void findByPageTest() throws Exception{
        System.out.println("---------------目的地信息分页列表---------------");
        Map<String,Object> filter = Maps.newHashMap();
        ResponseMessage back = destinationService.findByPage(1,10,user,filter);
        System.out.println("返回值：" +back.getData() );
        Assert.assertSame("返回值是1", true, back.getData()!=null?true:false);
    }

    @Test
    @Rollback
    public void createTest() throws Exception{
        System.out.println("---------------新增目的地信息---------------");
        ResponseMessage back = destinationService.save(destinationModel, user);
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是1", 1, status);
    }

    @Test
    @Rollback
    public void updateTest() throws Exception{
        System.out.println("---------------编辑目的地信息---------------");
        ResponseMessage back = destinationService.edit("-111111111",destinationModel, user);
        int status=  back.getStatus();
        System.out.println("返回值：" + status);
        Assert.assertSame("返回值是0", 0, status);
    }


}
