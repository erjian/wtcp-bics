package cn.com.wanwei.bic.model;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SyncDataModel {

    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value="标题/名称")
    private String title;

    @ApiModelProperty(value="地址")
    private String address;

    @ApiModelProperty(value="行政区划编码")
    private String region;

    @ApiModelProperty(value="经度")
    private Double longitude;

    @ApiModelProperty(value="纬度")
    private Double latitude;

    @ApiModelProperty(value="电话（景区、农家乐、自驾营地、旅行社：需关联表进行获取）")
    private String phone;

    @ApiModelProperty(value="内容")
    private String content;

    @ApiModelProperty(value="星级（自驾营地、农家乐、租车、餐饮服务：没有星级）")
    private String grade;

    @ApiModelProperty(value="评分（农家乐、自驾营地、旅行社、租车：没有评分）")
    private Float score;

    @ApiModelProperty(value="权重")
    private Integer weight;

    @ApiModelProperty(value="资源类别（说明：景区、农家乐、旅行社......）")
    private String category;

    @ApiModelProperty(value="资源子分类（说明：火车站、机场、中餐厅、川菜......）")
    private String type;

    @ApiModelProperty(value="关联标签列表")
    List<BaseTagsEntity> tagsList;

    @ApiModelProperty(value="附件列表")
    private Map<String,Object> fileList;
}
