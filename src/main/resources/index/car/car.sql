/**
 * 租车
 */

select t.id relateId,t.id,t.`code`, t.title,t.sub_title subTitle,t.title_qp fullSpell, t.title_jp simpleSpell,t.slogan,
t.summary, t.address,t.weight,t.establish_time,t.chartered_bus_type,t.regular_bus_type,t.keyword,t.latitude, t.longitude,
CONCAT(t.latitude,",",t.longitude) geoPoint,t.region,t.region_full_code regionFullCode,t.region_full_name regionFullName,
t.dept_code deptCode,'' images,'' videos,'' audios, '' tags,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus
from t_bic_rental_car t ;


select b.id relateId,CONCAT('[',GROUP_CONCAT('"',t.tag_name,'"'),']') tags
from t_bic_rental_car_tags t LEFT JOIN t_bic_rental_car b on t.principal_id = b.id
where t.principal_id in(select a.id from t_bic_rental_car a  ) GROUP BY t.principal_id;

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