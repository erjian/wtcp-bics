/**
 * 农家乐
 */

select t.id relateId,t.id,t.`code`, t.title,t.sub_title subTitle,t.slogan,t.summary, t.description,
t.start_time startTime,t.summer_time summerTime, t.winter_time winterTime,
t.address,t.scenic_id scenicId, t.within_scenic withinScenic, t.within_park withinPark,t.num,t.weight,t.type,
t.latitude, t.longitude,CONCAT(t.latitude,",",t.longitude) geoPoint,t.region,t.region_full_code regionFullCode,t.region_full_name regionFullName,
t.dept_code deptCode,'' images,'' videos,'' audios, '' tags,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus,
(select b.service_facility from t_bic_business b where b.principal_id = t.id) as serviceFacility,
(select b.phone from t_bic_business b where b.principal_id = t.id) as phone
from t_bic_entertainment t ;


select b.id relateId,CONCAT('[',GROUP_CONCAT('"',t.tag_name,'"'),']') tags
from t_bic_entertainment_tags t LEFT JOIN t_bic_entertainment b on t.principal_id = b.id
where t.principal_id in(select a.id from t_bic_entertainment a  ) GROUP BY t.principal_id;

-- 通用
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