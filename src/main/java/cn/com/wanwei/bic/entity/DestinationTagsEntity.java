/**
 * 该源代码文件 DestinationTagsEntity 是工程“wtcp-bics”的一部分
 *
 * @project wtcp-bics
 * @author zhanglei
 * @date 2019年10月10日09:12:28
 * @description 目的地标签实体类
 */
package cn.com.wanwei.bic.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "bic_destination_tags")
@ApiModel(description = "目的地标签实体类")
@ToString(callSuper = true)
public class DestinationTagsEntity extends BaseTagsEntity {

}