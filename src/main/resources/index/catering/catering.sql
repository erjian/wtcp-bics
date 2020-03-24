/**
 * 餐饮
 */

select t.id relateId,t.id,t.`code`, t.title,t.sub_title subTitle,t.full_spell fullSpell, t.simple_spell simpleSpell,t.slogan,
t.summary, t.description, t.address,t.`level`,t.score,t.weight,t.cook_style cookStyle,t.feature_tags featureTags,t.web_comment webComment, t.avg_consumption avgConsumption,
t.open_time openTime,t.traffic_notice trafficNotice,t.repast_notice repastNotice,t.vr_url vrUrl,
t.latitude, t.longitude,CONCAT(t.latitude,",",t.longitude) geoPoint,t.region,t.region_full_code regionFullCode,t.region_full_name regionFullName,
t.dept_code deptCode,'' images,'' videos,'' audios, '' tags,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus,
from t_bic_catering t;


select b.id relateId,CONCAT('[',GROUP_CONCAT('"',t.tag_name,'"'),']') tags
from t_bic_catering_tags t LEFT JOIN t_bic_catering b on t.principal_id = b.id
where t.principal_id in(select a.id from t_bic_catering a  ) GROUP BY t.principal_id;

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