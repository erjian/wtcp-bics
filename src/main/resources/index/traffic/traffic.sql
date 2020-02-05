/**
 * 交通枢纽
 */

select t.id relateId,t.id,t.`code`, t.title,t.full_spell fullSpell,t.simple_spell simpleSpell,t.content,
t.summary, t.description, t.address,t.type,t.phone,t.weight,t.latitude, t.longitude,
CONCAT(t.latitude,",",t.longitude) geoPoint,t.region,t.region_full_code regionFullCode,t.region_full_name regionFullName,
t.dept_code deptCode,'' images,'' videos,'' audios,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus
from t_bic_traffic_agent t ;

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