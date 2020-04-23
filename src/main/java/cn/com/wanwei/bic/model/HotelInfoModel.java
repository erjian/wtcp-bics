package cn.com.wanwei.bic.model;

import cn.com.wanwei.bic.entity.ContactEntity;
import cn.com.wanwei.bic.entity.HotelEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
public class HotelInfoModel {

    @ApiModelProperty(value="酒店基础信息")
    private HotelEntity hotelEntity;

    @ApiModelProperty(value="酒店通讯信息")
    private ContactEntity contactEntity;

    @ApiModelProperty(value="素材信息")
    private Map<String,Object> fileList;
}
