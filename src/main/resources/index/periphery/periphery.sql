/**
 * 周边
 */

select t.id relateId,
t.id,t.`code`, t.title,t.sub_title subTitle,t.slogan,t.summary,t.description,t.content,t.address,
t.weight,t.category,t.type,t.phone,t.trading_area tradingArea,t.score,t.per_consumption perConsumption,t.latitude, t.longitude,
CONCAT(t.latitude,",",t.longitude) geoPoint,t.region,t.region_full_code regionFullCode,t.region_full_name regionFullName,
t.dept_code deptCode,'' images,'' videos,'' audios, '' tags,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus,
(select CONCAT('[',GROUP_CONCAT('"',c.cate_id,'"'),']') from t_bic_cate_relation c where c.catering_id = t.id GROUP BY c.catering_id) as cateTags
from t_bic_periphery t ;


select b.id relateId,CONCAT('[',GROUP_CONCAT('"',t.tag_name,'"'),']') tags
from t_bic_periphery_tags t LEFT JOIN t_bic_periphery b on t.principal_id = b.id
where t.principal_id in(select a.id from t_bic_periphery a  ) GROUP BY t.principal_id;

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