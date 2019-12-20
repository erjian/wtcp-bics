package cn.com.wanwei.bic.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BatchAuditModel implements Serializable {

    @ApiModelProperty(value="关联信息ID集合", required = true)
    private List<String> ids;

    @ApiModelProperty(value="状态", required = true)
    private Integer status;

    @ApiModelProperty(value="审核意见", required = true)
    private String msg;

    @ApiModelProperty(value="操作类型。0：审核操作  1：上下线操作", required = true)
    private Integer type;

    @ApiModelProperty(value="要操作的对象类前缀，例如：Scenic | Poi", required = true)
    private String classPrefixName;

}
