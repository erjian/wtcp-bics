package cn.com.wanwei.bic.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HotelListModel {

    @ApiModelProperty(value="酒店主键")
    private String id;

    @ApiModelProperty(value="酒店名称")
    private String title;

}
