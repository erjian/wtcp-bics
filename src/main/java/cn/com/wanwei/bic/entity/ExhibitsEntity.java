package cn.com.wanwei.bic.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_exhibits")
@ApiModel(description="展品管理")
@ToString(callSuper = true)
public class ExhibitsEntity extends CommonEntity{

    @ApiModelProperty(value = "名称简拼" ,required = true)
    private String simpleSpell;

    @ApiModelProperty(value = "名称全拼" ,required = true)
    private String fullSpell;

    @ApiModelProperty(value = "所属场馆ID" ,required = true)
    private String venueId;

    @ApiModelProperty(value = "所属厅室ID" ,required = true)
    private String hallId;

    @ApiModelProperty(value = "展品类别 文物展品、化石展品、现代展品；主要区分展品是哪一大类，文物和化石是不同的，该字段进行区分" ,required = true)
    private String category;

    @ApiModelProperty(value = "展品级别 如果是文物，则记录文物级别，如：一级,二级,三级,此处按国家标准，记录展品级别；没有标准或未定级的此处统一设为未定级" ,required = true)
    private String level;

    @ApiModelProperty(value = "展品类型 未包含的类别后续根据业务增加，" +
            "分类包含：1.金银器;2.铜器;3.铁器;4.陶、泥器;5.瓷器;6.砖瓦;7.宝、玉石器;8.石器石刻;9.漆木竹器;" +
            "10.绘画;11.书法;12.拓片;13.珐琅器;14.玻璃器;15.骨角牙器;16.纺织（绣）品;17.皮革;18.玺印;" +
            "19.文具;20.乐器;21.法器;22.货币;23.雕塑;24.文献书籍;25.徽章证件;26.邮品;27.票据;28.音像制品;" +
            "29.交通工具;30.武器装备;31.航空航天;32.现代科技;33.古脊椎动物化石;34.古人类化石;35.植物化石;36.建筑;37.摄影;38.民俗;39.工艺;" ,required = true)
    private String type;

    @ApiModelProperty(value = "展览区域 记录展品在展厅哪个区域展览，如：汉代展区、西北区，该字段文字记录即可，用于显示" ,required = true)
    private String exhibitionArea;

    @ApiModelProperty(value = "总登记号 作为唯一识别编码，供管理或与其他系统同一展品关联使用。如果有全球或全国唯一的识别号则输入，" +
            "例如文物登记号，如果没有则采用场馆给出的展品编号（例如：现代艺术品），如果都没有，则系统自定义一个唯一编码。" )
    private String registerNumber;

    @ApiModelProperty(value = "年代分类 包含：第四纪、新近纪、古近纪、白恶纪、侏罗纪、三叠纪、二叠纪、石炭纪、泥盆纪、志留纪、奥陶纪、" +
            "寒武纪、震旦纪、旧石器时代、新石器时代、夏、商、周、秦、汉、三国、西晋、东晋十六国、南北朝、隋、唐、五代十国、宋、辽、西夏、金、元、明、清、民国、现代" ,required = true)
    private String yearsKind;

    @ApiModelProperty(value = "具体年代 例如：1890 或者 1890 ~1899；此处记录具体的作品创作年代" )
    private String years;

    @ApiModelProperty(value = "外形尺寸 记录长宽高（单位mm）" )
    private String appearanceSize;

    @ApiModelProperty(value = "质量 记录质量单位kg" )
    private Integer quality;

    @ApiModelProperty(value = "展品作者 作者可以是多个;对于艺术品、书籍等有作者的记录，化石类等没有作者的不记录" )
    private String author;

    @ApiModelProperty(value = "展品出土地 文物类展品可以记录挖掘出处" )
    private String excavationAddress;

    @ApiModelProperty(value = "全景地址" )
    private String vrUrl;

    @ApiModelProperty(value = "代表介绍 AR 模型" )
    private String arUrl;

    @ApiModelProperty(value = "状态" ,required = true)
    private Integer status;

    public Integer getOnlineStatus() {
        return this.status == 9 ? this.status : 1;
    }

}