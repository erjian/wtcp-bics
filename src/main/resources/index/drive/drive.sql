/**
 * 自驾营地
 */

select t.id relateId,t.id,t.`code`, t.title,t.sub_title subTitle,t.full_spell fullSpell, t.simple_spell simpleSpell,
t.slogan,t.start_time startTime,t.summer_time summerTime, t.winter_time winterTime,
t.summary, t.description, t.address,t.open_time,t.start_time,t.traffic,CONCAT(t.latitude,",",t.longitude) geoPoint,
t.latitude, t.longitude,t.region,t.region_full_code regionFullCode,t.region_full_name regionFullName,t.weight,
t.dept_code deptCode,'' images,'' videos,'' audios, '' tags,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus
from t_bic_drive_camp t ;


select b.id relateId,CONCAT('[',GROUP_CONCAT('"',t.tag_name,'"'),']') tags
from t_bic_drive_camp_tags t LEFT JOIN t_bic_drive_camp b on t.principal_id = b.id
where t.principal_id in(select a.id from t_bic_drive_camp a  ) GROUP BY t.principal_id;

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