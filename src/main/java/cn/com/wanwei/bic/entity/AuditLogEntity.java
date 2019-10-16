package cn.com.wanwei.bic.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_audit_log")
@ApiModel(description="审核记录管理")
@ToString(callSuper = true)
public class AuditLogEntity implements Serializable {

    @Id
    @ApiModelProperty(value="主键" ,readOnly = true)
    private String id;

    @ApiModelProperty(value="关联信息ID" ,required = true)
    private String principalId;

    @ApiModelProperty(value="审核前状态（0：待审，1：通过，2：退回，9：上线）")
    private Integer preStatus;

    @ApiModelProperty(value="审核状态（0：待审，1：通过，2：退回，9：上线）" ,required = true)
    private Integer status;

    @ApiModelProperty(value="审核意见" )
    private String opinion;

    @ApiModelProperty(value="创建用户" ,readOnly = true)
    private String createdUser;

    @ApiModelProperty(value="创建时间" ,readOnly = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createdDate;

    @ApiModelProperty(value="操作记录类型（0：审核记录，1：上线/下线记录）" ,readOnly = true)
    private Integer type;
}