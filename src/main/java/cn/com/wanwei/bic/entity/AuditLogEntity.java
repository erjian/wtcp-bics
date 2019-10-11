package cn.com.wanwei.bic.entity;

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
@Table(name="bic_audit_log")
@ApiModel(description="审核记录管理")
@ToString(callSuper = true)
public class AuditLogEntity implements Serializable {

    @Id
    @ApiModelProperty(value="主键" ,readOnly = true)
    private String id;

    @ApiModelProperty(value="关联信息ID" )
    private String principalId;

    @ApiModelProperty(value="审核前状态（0：待审，1：通过，2：退回，9：上线）" )
    private Boolean preStatus;

    @ApiModelProperty(value="审核状态（0：待审，1：通过，2：退回，9：上线）" )
    private Boolean status;

    @ApiModelProperty(value="审核意见" )
    private String opinion;

    @ApiModelProperty(value="创建用户" ,readOnly = true)
    private String createdUser;

    @ApiModelProperty(value="创建时间" ,readOnly = true)
    private Date createdDate;
}