package cn.com.wanwei.bic.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_celebrity")
@ApiModel(description="名人管理")
@ToString(callSuper = true)
public class CelebrityEntity extends BaseEntity{

    @ApiModelProperty(value = "名人类别 历史名人、近代名人、现代名人、神话人物")
    private String category;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "姓名简拼")
    private String simpleSpell;

    @ApiModelProperty(value = "姓名全拼")
    private String fullSpell;

    @ApiModelProperty(value = "别名")
    private String alias;

    @ApiModelProperty(value = "简介")
    private String summary;

    @ApiModelProperty(value = "摘要")
    private String description;

    @ApiModelProperty(value = "广告语")
    private String slogan;

    @ApiModelProperty(value = "性别 0：保密，1：男，2：女")
    private Integer sex;

    @ApiModelProperty(value = "出生日期")
    private Date birthDate;

    @ApiModelProperty(value = "去世日期")
    private Date passAwayDate;

    @ApiModelProperty(value = "出生地")
    private Integer birthplace;

    @ApiModelProperty(value = "民族")
    private Integer nation;

    @ApiModelProperty(value = "类型 民族英雄、科学家、专家学者、劳模、非遗传承人、帝王、大臣、政治家、作家、诗人、企业家、宗教领袖、舞蹈家、画家、歌唱家、曲艺家、收藏家、导演、艺人、运动员、军阀、国民党")
    private String type;

    @ApiModelProperty(value = "代表成就(待定) 记录名人的一项代表性成就，信息包含：关联信息、名称、摘要 例如：非遗传承人可以记录其传承的非遗项目编号、项目名称、项目摘要。画家可以记录其代表画作图片链接和画作名称、画作摘要;\n" +
            "注意：此处应该结构化处理，可以显示，也可以分解出信息中细节数据，用于关联提取其他表中的详细数据。")
    private Integer behalfAchievement;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "组织机构编码")
    private String deptCode;

    @ApiModelProperty(value = "主要成就")
    private String chiefBehalf;

    @ApiModelProperty(value = "生平事迹")
    private String story;
}