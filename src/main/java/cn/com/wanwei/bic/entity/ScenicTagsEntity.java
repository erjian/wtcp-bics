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
@Table(name = "bic_scenic_tags")
@ApiModel(description = "景区基础信息管理")
@ToString(callSuper = true)
public class ScenicTagsEntity extends BaseTagsEntity {

}
