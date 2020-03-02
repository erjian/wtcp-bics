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
@Table(name = "t_bic_venue_tags")
@ApiModel(description = "场馆标签管理")
@ToString(callSuper = true)
public class VenueTags extends BaseTagsEntity {
}