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
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_drive_camp")
@ApiModel(description="自驾营地管理")
@ToString(callSuper = true)
public class DriveCampEntity extends commonEntity{

    @ApiModelProperty(value = "简拼",readOnly = true)
    private String simpleSpell;

    @ApiModelProperty(value = "全拼",readOnly = true)
    private String fullSpell;

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "接待时间")
    private String openTime;

    @ApiModelProperty(value = "开业时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String startTime;

    @ApiModelProperty(value = "组织机构编码")
    private String deptCode;

    @ApiModelProperty(value = "审核状态（0：待审，1：通过，2：退回，9：上线）")
    private Integer status;

    @ApiModelProperty(value = "交通信息")
    private String traffic;

    @ApiModelProperty(value = "在线状态")
    private Integer onlineStatus;

    public Integer getOnlineStatus() {
        return this.status == 9 ? this.status : 1;
    }
}