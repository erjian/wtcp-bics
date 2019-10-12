package cn.com.wanwei;

import cn.com.wanwei.bic.BicApplication;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.MaterialEntity;
import cn.com.wanwei.bic.entity.ScenicTagsEntity;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.common.model.Org;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BicApplication.class)
@Transactional
@Slf4j
public class TagsServiceTest {

    @Autowired
    private TagsService tagsService;

    private BaseTagsEntity entity;

    private User user;

    @Before
    public void before() {
        log.info("---------------- 标签管理接口单元测试开始 -------------------");
        entity = new BaseTagsEntity();
        entity.setId("-1");
        entity.setPrincipalId("-1");
        entity.setCreatedUser("ceshi");
        entity.setCreatedDate(new Date());
        entity.setTagCatagory("测试分类");
        entity.setTagName("测试标签");

        user = new User();
        user.setUsername("ceshi");
        Org org = new Org();
        org.setCode("111");
        user.setOrg(org);
    }

    @After
    public void after() {
        log.info("---------------- 标签管理接口单元测试结束 ---------------------");
    }

    @Test
    public void selectByPrimaryKeyTest() {
        log.info("--------------- 获取标签信息---------------");
        BaseTagsEntity back = tagsService.selectByPrimaryKey(entity.getId(), ScenicTagsEntity.class);
        System.out.println("返回值：" + back);
        Assert.assertSame("返回值是true", true, back == null ? true : false);
    }

    @Test
    public void findByPrincipalIdTest() {
        log.info("--------------- 获取标签信息---------------");
        ResponseMessage back = tagsService.findByPrincipalId(entity.getPrincipalId(), ScenicTagsEntity.class);
        System.out.println("返回值：" + back.getData());
        Assert.assertSame("返回值是true", true, back.getData() == null ? true : false);
    }

    @Test
    public void deleteByPrincipalIdTest() {
        log.info("---------------根据主键删除标签信息---------------");
        int back = tagsService.deleteByPrincipalId(entity.getId(), ScenicTagsEntity.class);
        System.out.println("返回值：" + back);
        Assert.assertSame("返回值是0", 0, back);
    }

    @Test
    @Rollback
    public void batchInsertTest() {
        log.info("--------------- 批量添加素材信息---------------");
        List<BaseTagsEntity> list = Lists.newArrayList();
        list.add(entity);
        int back = tagsService.batchInsert("-1", list, user, ScenicTagsEntity.class);
        System.out.println("返回值：" + back);
        Assert.assertSame("返回值是true", true, back >= 0 ? true : false);
    }


}
