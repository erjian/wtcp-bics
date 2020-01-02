package cn.com.wanwei;

import cn.com.wanwei.bic.BicApplication;
import cn.com.wanwei.bic.entity.MaterialEntity;
import cn.com.wanwei.bic.entity.PoiEntity;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.common.model.Org;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
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

import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BicApplication.class)
@Transactional
@Slf4j
public class MaterialServiceTest {

    @Autowired
    private MaterialService materialService;

    private MaterialEntity materialEntity;

    private User user;

    @Before
    public void before() {
        log.info("---------------- 素材管理接口单元测试开始 -------------------");
        materialEntity = new MaterialEntity();
        materialEntity.setId("-1");
        materialEntity.setPrincipalId("-1");
        materialEntity.setCreatedUser("ceshi");
        materialEntity.setCreatedDate(new Date());
        materialEntity.setFileType("image");
        materialEntity.setFileIdentify("1");
        materialEntity.setFileUrl("/data/abc.jpg");
        materialEntity.setMaterialId("-666");

        user = new User();
        user.setUsername("ceshi");
        Org org = new Org();
        org.setCode("111");
        user.setOrg(org);
    }

    @After
    public void after() {
        log.info("---------------- 素材管理接口单元测试结束 ---------------------");
    }

    @Test
    public void deleteByPrimaryKeyTest() {
        log.info("---------------根据主键删除素材信息---------------");
        ResponseMessage back = materialService.deleteByPrimaryKey(materialEntity.getId());
        System.out.println("返回值：" + back.getStatus());
        Assert.assertSame("返回值是0", 0, back.getStatus());
    }

    @Test
    public void deleteByPrincipalIdsTest() {
        log.info("---------------根据主键删除素材信息---------------");
        List<String> ids = Lists.newArrayList();
        ids.add(materialEntity.getId());
        ResponseMessage back = materialService.deleteByPrincipalIds(ids);
        System.out.println("返回值：" + back.getStatus());
        Assert.assertSame("返回值是1", 1, back.getStatus());
    }

    @Test
    @Rollback
    public void insertTest() {
        log.info("---------------添加素材信息---------------");
        ResponseMessage back = materialService.insert(materialEntity, user);
        System.out.println("返回值：" + back.getStatus());
        Assert.assertSame("返回值是1", 1, back.getStatus());
    }

    @Test
    @Rollback
    public void batchInsertTest() {
        log.info("--------------- 批量添加素材信息---------------");
        List<MaterialEntity> list = Lists.newArrayList();
        list.add(materialEntity);
        ResponseMessage back = materialService.batchInsert("-1", list, user);
        System.out.println("返回值：" + back.getStatus());
        Assert.assertSame("返回值是1", 1, back.getStatus());
    }

    @Test
    public void selectByPrimaryKeyTest() {
        log.info("--------------- 获取素材信息---------------");
        ResponseMessage back = materialService.selectByPrimaryKey(materialEntity.getId());
        System.out.println("返回值：" + back.getData());
        Assert.assertSame("返回值是true", true, back.getData() == null ? true : false);
    }

    @Test
    public void findByPrincipalIdTest() {
        log.info("--------------- 获取素材信息---------------");
        List<MaterialEntity> list = Lists.newArrayList();
        list.add(materialEntity);
        ResponseMessage back = materialService.findByPrincipalId(materialEntity.getPrincipalId());
        System.out.println("返回值：" + back.getData());
        Assert.assertSame("返回值是true", true, back.getData() == null ? true : false);
    }

    @Test
    public void findByPidAndTypeTest() {
        log.info("--------------- 获取素材信息---------------");
        List<MaterialEntity> list = Lists.newArrayList();
        list.add(materialEntity);
        ResponseMessage back = materialService.findByPidAndType(materialEntity.getPrincipalId(), materialEntity.getFileType());
        System.out.println("返回值：" + back.getData());
        Assert.assertSame("返回值是true", true, back.getData() == null ? true : false);
    }

    @Test
    public void findByPidAndIdentifyTest() {
        log.info("--------------- 获取素材信息---------------");
        List<MaterialEntity> list = Lists.newArrayList();
        list.add(materialEntity);
        ResponseMessage back = materialService.findByPidAndIdentify(materialEntity.getPrincipalId(), materialEntity.getFileIdentify());
        System.out.println("返回值：" + back.getData());
        Assert.assertSame("返回值是true", true, back.getData() == null ? true : false);
    }

    @Test
    @Rollback
    public void updateByPrimaryKeyTest() {
        log.info("--------------- 更新素材信息---------------");
        List<MaterialEntity> list = Lists.newArrayList();
        list.add(materialEntity);
        ResponseMessage back = materialService.updateByPrimaryKey(materialEntity.getId(), materialEntity, user);
        System.out.println("返回值：" + back.getStatus());
        Assert.assertSame("返回值是1", 1, back.getStatus());
    }

    @Test
    @Rollback
    public void updateIdentifyTest() {
        log.info("--------------- 更新素材信息---------------");
        List<MaterialEntity> list = Lists.newArrayList();
        list.add(materialEntity);
        ResponseMessage back = materialService.updateIdentify(materialEntity.getPrincipalId(),materialEntity.getId(), materialEntity.getFileIdentify(), user);
        System.out.println("返回值：" + back.getStatus());
        Assert.assertSame("返回值是0", 0, back.getStatus());
    }

}
