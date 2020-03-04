/**
 * 该源代码文件 VenueEntity 是工程“wtcp-bics”的一部分
 *
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2020年3月2日09:47:17
 */
package cn.com.wanwei.bic.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "t_bic_venue")
@ApiModel(description = "场馆管理")
@ToString(callSuper = true)
public class VenueEntity extends CommonEntity {

    @ApiModelProperty(value = "场馆类别 例如：博物馆、科技馆、文化馆、美术馆、图书馆、剧场等")
    private String category;

    @ApiModelProperty(value = "名称简拼")
    private String simpleSpell;

    @ApiModelProperty(value = "名称全拼")
    private String fullSpell;

    @ApiModelProperty(value = "场馆级别")
    private String level;

    @ApiModelProperty(value = "评分")
    private Float score;

    @ApiModelProperty(value = "全景地址")
    private String vrUrl;

    @ApiModelProperty(value = "标题图片")
    private String titleImage;

    @ApiModelProperty(value = "场馆面积")
    private Double area;

    @ApiModelProperty(value = "接待游客上线")
    private Integer maxReceptionNum;

    @ApiModelProperty(value = "开馆时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date openingTime;

    @ApiModelProperty(value = "闭馆时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date closingTime;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "在线状态")
    private Integer onlineStatus;

    public Integer getOnlineStatus() {
        return this.status == 9 ? this.status : 1;
    }

    /**
     * 用于存储景区-标签
     */
    @Transient
    @ApiModelProperty(value = "标签")
    public List<ScenicTagsEntity> tagsEntities;

    //冗余字段，关联素材的id
    @Transient
    private String timeId;

    private Map<String,Object> fileList;
}