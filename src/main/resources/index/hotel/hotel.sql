/**
 * 酒店
 */

select t.id relateId,t.id,t.`code`, t.title,t.sub_title subTitle,t.full_spell fullSpell, t.simple_spellv simpleSpell,t.slogan,
t.feature, t.lightspot,t.room_num roomNum,t.min_price minPrice,t.facility,t.services,t.vrUrl,t.open_time openTime,
t.summary, t.description, t.address,t.`level`,t.category,t.area,t.traffic_notice trafficNotice,t.stay_notice stayNotice,t.score,t.weight,
t.latitude, t.longitude,CONCAT(t.latitude,",",t.longitude) geoPoint,t.region,t.region_full_code regionFullCode,t.region_full_name regionFullName,
t.dept_code deptCode,'' images,'' videos,'' audios, '' tags,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus,
(select b.service_facility from t_bic_business b where b.principal_id = t.id) as serviceFacility,
(select b.phone from t_bic_business b where b.principal_id = t.id) as phone
from t_bic_hotel t;


select b.id relateId,CONCAT('[',GROUP_CONCAT('"',t.tag_name,'"'),']') tags
from t_bic_hotel_tags t LEFT JOIN t_bic_scenic b on t.principal_id = b.id
where t.principal_id in(select a.id from t_bic_scenic a  ) GROUP BY t.principal_id;

select t.principal_id relateId, CONCAT('[',GROUP_CONCAT('"',REPLACE(t.file_url,'\\','/'),'"'),']') images
from t_bic_material t
where t.file_type = 'image' and t.principal_id is not null
GROUP BY t.principal_id;

select t.principal_id relateId, CONCAT('[',GROUP_CONCAT('"',REPLACE(t.file_url,'\\','/'),'"'),']') videos
from t_bic_material t
where t.file_type = 'video' and t.principal_id is not null
GROUP BY t.principal_id;

select t.principal_id relateId, CONCAT('[',GROUP_CONCAT('"',REPLACE(t.file_url,'\\','/'),'"'),']') audios
from t_bic_material t
where t.file_type = 'audio' and t.principal_id is not null
GROUP BY t.principal_id;