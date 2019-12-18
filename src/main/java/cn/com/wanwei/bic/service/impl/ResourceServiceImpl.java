package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.bic.feign.AuthServiceFeign;
import cn.com.wanwei.bic.mapper.*;
import cn.com.wanwei.bic.service.ResourceService;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  **
 */
@Service
@Slf4j
@RefreshScope
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ScenicMapper scenicMapper;

    @Autowired
    private PeripheryMapper peripheryMapper;

    @Autowired
    private TravelAgentMapper travelAgentMapper;

    @Autowired
    private RentalCarMapper rentalCarMapper;

    @Autowired
    private TrafficAgentMapper trafficAgentMapper;

    @Autowired
    private EntertainmentMapper entertainmentMapper;

    @Autowired
    private DriveCampMapper driveCampMapper;

    @Autowired
    private AuthServiceFeign authServiceFeign;

    @Override
    public ResponseMessage findByPieChart(User user) {
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        Long scenicCount = scenicMapper.findByDeptCode(user.getOrg().getCode());
        map1.put("value",scenicCount);
        map1.put("name","景区");
        list.add(map1);
        Long peripheryCount = peripheryMapper.findByDeptCode(user.getOrg().getCode());
        Map<String,Object> map2 = new HashMap<>();
        map2.put("value",peripheryCount);
        map2.put("name","周边");
        list.add(map2);
        Long travelCount = travelAgentMapper.findByDeptCode(user.getOrg().getCode());
        Map<String,Object> map3 = new HashMap<>();
        map3.put("value",travelCount);
        map3.put("name","从业");
        list.add(map3);
        Long rentalCount = rentalCarMapper.findByDeptCode(user.getOrg().getCode());
        Long trafficCount = trafficAgentMapper.findByDeptCode(user.getOrg().getCode());
        Map<String,Object> map4 = new HashMap<>();
        map4.put("value",rentalCount + trafficCount);
        map4.put("name","出行");
        list.add(map4);
        Long entertainmentCount = entertainmentMapper.findByDeptCode(user.getOrg().getCode());
        Long driveCount = driveCampMapper.findByDeptCode(user.getOrg().getCode());
        Map<String,Object> map5 = new HashMap<>();
        map5.put("value",entertainmentCount + driveCount);
        map5.put("name","休闲娱乐");
        list.add(map5);
        return ResponseMessage.defaultResponse().setData(list);
    }

    @Override
    public ResponseMessage findByHistogram(User user) {
        Map<String,Object> nameMap = new HashMap<>();
        List<String> nameList = new ArrayList<>();
        List<String> codeList = new ArrayList<>();
        nameMap.put("deptName",nameList);
        //获取组织机构数据
        ResponseMessage responseMessage = authServiceFeign.findByDeptCode(user.getOrg().getCode());
        List<Map<String,String>> list = (List<Map<String, String>>) responseMessage.getData();
        for (Map<String,String> map : list){
            nameList.add(map.get("name"));
            codeList.add(map.get("code"));
        }
        Map<String,Object> histogramMap = this.histogramData(codeList,user);
        histogramMap.put("deptName",nameList);
        return ResponseMessage.defaultResponse().setData(histogramMap);
    }

    /**
     * 景区、周边、从业、出行、休闲娱乐的数量
     * @param codeList 组织机构编码
     * @param user 用户
     * @return Map
     */
    private Map<String,Object> histogramData(List<String> codeList,User user){
        Map<String,Object> map = new HashMap<>();
        // 景区
        List<Long> scenicList = new ArrayList<>();
        // 周边
        List<Long> peripheryList = new ArrayList<>();
        //从业
        List<Long> employedList = new ArrayList<>();
        // 出行
        List<Long> travelList = new ArrayList<>();
        // 休闲娱乐
        List<Long> entertainmentList = new ArrayList<>();
        for (String deptCode : codeList){
            Long scenicCount = scenicMapper.findByDeptCode(deptCode);
            scenicList.add(scenicCount);
            Long peripheryCount = peripheryMapper.findByDeptCode(user.getOrg().getCode());
            peripheryList.add(peripheryCount);
            Long travelCount = travelAgentMapper.findByDeptCode(user.getOrg().getCode());
            employedList.add(travelCount);
            Long rentalCount = rentalCarMapper.findByDeptCode(user.getOrg().getCode());
            Long trafficCount = trafficAgentMapper.findByDeptCode(user.getOrg().getCode());
            travelList.add(rentalCount + trafficCount);
            Long entertainmentCount = entertainmentMapper.findByDeptCode(user.getOrg().getCode());
            Long driveCount = driveCampMapper.findByDeptCode(user.getOrg().getCode());
            entertainmentList.add(entertainmentCount + driveCount);
        }
        map.put("景区",scenicList);
        map.put("周边",peripheryList);
        map.put("从业",employedList);
        map.put("出行",travelList);
        map.put("休闲",entertainmentList);
        return map;
    }

}
