package cn.com.wanwei.bic.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@MappedSuperclass
@NoArgsConstructor
public class FindStatusModel implements Serializable {

    private String id;
    private Integer status;

}
