/**
 * 该源代码文件 ContactEntity 是工程“wtcp-bics”的一部分
 *
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日11:53:40
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

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="bic_contact")
@ApiModel(description="通讯信息管理")
@ToString(callSuper = true)
public class ContactEntity extends BaseEntity {

    @ApiModelProperty(value = "关联主键ID")
    private String principalId;

    @ApiModelProperty(value = "办公室电话")
    private String phone;

    @ApiModelProperty(value = "办公室传真")
    private String fax;

    @ApiModelProperty(value = "官网地址")
    private String website;

    @ApiModelProperty(value = "电子邮箱")
    private String mail;

    @ApiModelProperty(value = "官方微信")
    private String wechat;

    @ApiModelProperty(value = "官方微博")
    private String microBlog;

    @ApiModelProperty(value = "投诉电话")
    private String complaintCall;

    @ApiModelProperty(value = "救援电话")
    private String rescueCall;
}