package cn.com.wanwei.bic.entity;

import cn.com.wanwei.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "t_bic_heritage")
@ApiModel(description = "非遗管理")
@ToString(callSuper = true)
public class HeritageEntity extends BaseEntity {

    @ApiModelProperty(value = "非遗名称" ,required = true)
    private String title;

    @ApiModelProperty(value = "非遗简称")
    private String subTitle;

    @ApiModelProperty(value = "名称简拼",required = true)
    private String simpleSpell;

    @ApiModelProperty(value = "名称全拼",required = true)
    private String fullSpell;

    @ApiModelProperty(value = "编码",required = true)
    private String code;

    @ApiModelProperty(value = "项目类别,包含：1.民间文学;2.传统音乐;3.传统舞蹈;4.传统戏剧;5.曲艺;6.传统体育、游艺与杂技;7.传统美术;8.传统技艺;9.传统医药;10.民俗;",required = true)
    private String category;

    @ApiModelProperty(value = "项目级别，包含：世界级、国家级、省级、市级、县级",required = true)
    private String level;

    @ApiModelProperty(value = "项目类型，新增项目、扩展项目",required = true)
    private String type;

    @ApiModelProperty(value = "关联项目，如果是扩展项目，需记录其关联的项目")
    private String relateProject;

    @ApiModelProperty(value = "项目编号",required = true)
    private String heritageSerial;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "简介")
    private String description;

    @ApiModelProperty(value = "广告语")
    private String slogan;

    @ApiModelProperty(value = "公布时间",required = true)
    private String publishTime;

    @ApiModelProperty(value = "所在地区",required = true)
    private String region;

    @ApiModelProperty(value = "所在地区名称")
    private String regionFullName;

    @ApiModelProperty(value = "所在地区编码")
    private String regionFullCode;

    @ApiModelProperty(value = "申报地区或单位",required = true)
    private String declareGroup;

    @ApiModelProperty(value = "保护单位",required = true)
    private String guardGroup;

    @ApiModelProperty(value = "发源民族",required = true)
    private String nation;

    @ApiModelProperty(value = "传承地区")
    private String inheritRegion;

    @ApiModelProperty(value = "传承人(名人ids)",required = true)
    private String celebrityIds;

    @ApiModelProperty(value = "全景地址")
    private String vrUrl;

    @ApiModelProperty(value = "状态",required = true)
    private Integer status;

    @ApiModelProperty(value = "权重",required = true)
    private Integer weight;

    @ApiModelProperty(value = "组织机构编码",required = true)
    private String deptCode;

    @ApiModelProperty(value = "内容",required = true)
    private String content;

    @ApiModelProperty(value = "在线状态")
    private Integer onlineStatus;

    public Integer getOnlineStatus() {
        return this.status == 9 ? this.status : 1;
    }

    //冗余字段，关联素材的id
    @Transient
    private String timeId;


}