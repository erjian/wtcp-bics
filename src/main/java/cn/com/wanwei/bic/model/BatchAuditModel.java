package cn.com.wanwei.bic.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class BatchAuditModel implements Serializable {

    @ApiModelProperty(value="关联信息ID集合", required = true)
    @NotEmpty(message = "关联信息ID集合不可为空")
    private List<String> ids;

    @ApiModelProperty(value="状态", required = true)
    @NotNull(message = "状态不可为空")
    private Integer status;

    @ApiModelProperty(value="审核意见", required = true)
    private String msg;

    @ApiModelProperty(value="操作类型。0：审核操作  1：上下线操作", required = true)
    @NotNull(message = "操作类型不可为空")
    private Integer type;

    @ApiModelProperty(value="要操作的对象类前缀，例如：Scenic | Poi", required = true)
    @NotEmpty(message = "要操作的对象类前缀不可为空")
    private String classPrefixName;

}
