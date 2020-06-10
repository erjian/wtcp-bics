/**
 * 该源代码文件 DestinationEntity 是工程“wtcp-bics”的一部分
 * @project wtcp-bics
 * @author zhanglei
 * @date 2019年10月10日09:12:28
 * @description 目的地实体类
 */
package cn.com.wanwei.bic.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_destination")
@ApiModel(description="目的地基础信息管理")
@ToString(callSuper = true)
public class DestinationEntity extends BaseEntity{
    @ApiModelProperty(value = "审核状态")
    private Integer status;

    @ApiModelProperty(value = "地区名称")
    private String regionFullName;

    @ApiModelProperty(value = "地区编码")
    private String regionFullCode;

    @ApiModelProperty(value = "所属区域")
    private String region;

    @ApiModelProperty(value = "目的地介绍")
    private String introduce;

    @ApiModelProperty(value = "美食介绍")
    private String eatIntroduce;

    @ApiModelProperty(value = "饮品介绍")
    private String drinkIntroduce;

    @ApiModelProperty(value = "游玩介绍")
    private String playIntroduce;

    @ApiModelProperty(value = "旅游介绍")
    private String tourismIntroduce;

    @ApiModelProperty(value = "购物介绍")
    private String shopIntroduce;

    @ApiModelProperty(value = "娱乐介绍")
    private String entertainmentIntroduce;

    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "组织机构编码")
    private String deptCode;

    @ApiModelProperty(value = "简拼")
    private String pinyin;

    @ApiModelProperty(value = "全拼")
    private String fullPinyin;


    /**
     * 用于存储景区-标签
     */
    @Transient
    @ApiModelProperty(value = "标签")
    public List<ScenicTagsEntity> tagsEntities;

    //冗余字段，关联素材的id
    @Transient
    private String timeId;

    private Map<String,Object> fileList;
}