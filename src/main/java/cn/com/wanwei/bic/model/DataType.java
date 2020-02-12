/**
 * 该源代码文件 DataType 是工程“wtcp-bics”的一部分。
 *
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2020年2月7日10:06:30
 */
package cn.com.wanwei.bic.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * wtcp-bics - DataType 资源类型
 */
public enum DataType {
    SCENIC_TYPE("125001001","普通景区"),
    TOUR_VILLAGE_TYPE("125001002","旅游示范村"),
    TRAVEL_TYPE("125003001","旅行社"),
    RENTAL_CAR_TYPE("125004001","租车"),
    TRAFFIC_AGENT_TYPE("125004002","交通枢纽"),
    DRIVE_CAMP_TYPE("125005001","自驾营地"),
    AGRITAINMENT_TYPE("125005002","农家乐"),
    SPECIAL_SNACKS("125002001","特色小吃"),
    FOOD_TYPE("125002002","餐饮"),
    FOOD_STREET("125002003","小吃街"),
    SHOPPING_TYPE("125002004","购物"),
    SPECIALTY("125002004","特产");

    private String key;
    private String name;

    DataType(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public static List<Map<String, Object>> list() {
        List<Map<String, Object>> list = Lists.newArrayList();
        for (DataType item : DataType.values()) {
            Map<String, Object> itemMap = Maps.newHashMap();
            itemMap.put("key", item.getKey());
            itemMap.put("name", item.getName());
            list.add(itemMap);
        }
        return list;
    }

    public static String getName(String key) {
        for(DataType item : DataType.values()) {
            if(item.getKey().equals(key)) {
                return item.name;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

}
