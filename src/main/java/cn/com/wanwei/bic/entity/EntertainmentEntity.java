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

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_entertainment")
@ApiModel(description="休闲娱乐管理")
@ToString(callSuper = true)
public class EntertainmentEntity extends CommonEntity {

    @ApiModelProperty(value = "审核状态（0：待审，1：通过，2：退回，9：上线）",required = true)
    private Integer status;

    @ApiModelProperty(value = "所属景区")
    private String scenicId;

    @ApiModelProperty(value = "是否在景区之内（1：是，0：否）" ,required = true)
    private Integer withinScenic;

    @ApiModelProperty(value = "是否有停车场（1：是，0：否）" ,required = true)
    private Integer withinPark;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "组织机构编码" ,required = true)
    private String deptCode;

    @ApiModelProperty(value = "在线状态")
    private Integer onlineStatus;

    @ApiModelProperty(value = "客房数", notes = "客房数")
    private String guestRoomNum;

    @ApiModelProperty(value = "客房床位数", notes = "客房床位数")
    private String guestBedNum;

    @ApiModelProperty(value = "餐厅数", notes = "餐厅数")
    private String restaurantNum;

    @ApiModelProperty(value = "餐厅可容多少人共同用餐", notes = "餐厅可容多少人共同用餐")
    private String restaurantPermit;

    @ApiModelProperty(value = "可接待人数", notes = "该农家乐可接待多少人")
    private String receptionPermit;

    public Integer getOnlineStatus() {
        return this.status == 9 ? this.status : 1;
    }


    /**
     * 用于存储农家乐-标签表
     */
    @Transient
    @ApiModelProperty(value = "标签")
    public List<EntertainmentTagsEntity> tagsEntities;

    //冗余字段，关联素材的id
    @Transient
    private String timeId;

}