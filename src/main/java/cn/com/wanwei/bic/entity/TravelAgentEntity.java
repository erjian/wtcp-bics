package cn.com.wanwei.bic.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_travel_agent")
@ApiModel(description="旅行社管理")
@ToString(callSuper = true)
public class TravelAgentEntity extends commonEntity{

    @ApiModelProperty(value = "简拼",readOnly = true)
    private String simpleSpell;

    @ApiModelProperty(value = "全拼",readOnly = true)
    private String fullSpell;

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "经营范围")
    private String businessScope;

    @ApiModelProperty(value = "主营业务类型")
    private String businessType;

    @ApiModelProperty(value = "目的地城市")
    private String destinationCity;

    @ApiModelProperty(value = "客源地城市")
    private String fromCity;

    @ApiModelProperty(value = "级别")
    private String level;

    @ApiModelProperty(value = "等级评定时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String levelTime;

    @ApiModelProperty(value = "推荐状态（1：推荐，0：不推荐）")
    private Byte recommend;

    @ApiModelProperty(value = "成立时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String startTime;

    @ApiModelProperty(value = "接团类型")
    private String teamType;

    @ApiModelProperty(value = "特种旅游类型")
    private String tourType;

    @ApiModelProperty(value = "组织机构编码")
    private String deptCode;

    @ApiModelProperty(value = "审核状态（0：待审，1：通过，2：退回，9：上线）",required = true)
    private Integer status;

    @ApiModelProperty(value = "在线状态")
    private Integer onlineStatus;

    public Integer getOnlineStatus() {
        return this.status == 9 ? this.status : 1;
    }

}