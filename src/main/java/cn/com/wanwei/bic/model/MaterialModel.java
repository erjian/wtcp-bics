package cn.com.wanwei.bic.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;


/**
 * @author **
 */
@Data
@Configuration
@ConfigurationProperties("materialtype")
public class MaterialModel {
    private Map<String, List<InfoType>> image;
    private Map<String, List<InfoType>> video;
    private Map<String, List<InfoType>> audio;
    private Map<String, List<InfoType>> file;
}
