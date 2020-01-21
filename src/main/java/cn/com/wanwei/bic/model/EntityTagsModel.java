/**
 * 该源代码文件 EntityTagsModel.java 是工程“wtcp-bics”的一部分。
 * 
 * @project wtcp-bics
 * @author linjw
 * @date 2019年10月14日10:31:49
 */

package cn.com.wanwei.bic.model;

import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.MaterialEntity;
import lombok.Data;

import java.util.List;

/**
 * wtcp-bics - ScenicModel
 */
@Data
public class EntityTagsModel<T> {

	private String type;

	private List<BaseTagsEntity> tagsList;

	private T entity;

	private List<MaterialEntity> materialList;
}
