/**
 * 展品
 */

select t.id relateId,t.id,t.`code`, t.title,t.sub_title subTitle,t.full_spell fullSpell, t.simple_spell simpleSpell,t.slogan,
t.venue_id venueId,t.hall_id hallId,t.type, t.exhibition_area exhibitionArea,t.register_number registerNumber,t.years_kind yearsKind,
t.summary, t.description, t.years,t.`level`,t.category,t.appearance_size appearanceSize,t.quality,t.excavation_address excavationAddress,t.weight,
t.latitude, t.longitude,CONCAT(t.latitude,",",t.longitude) geoPoint,t.vr_url vrUrl,t.ar_url arUrl,t.author,
t.dept_code deptCode,'' images,'' videos,'' audios, '' tags,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus
from t_bic_exhibits t;


select b.id relateId,CONCAT('[',GROUP_CONCAT('"',t.tag_name,'"'),']') tags
from t_bic_exhibits_tags t LEFT JOIN t_bic_scenic b on t.principal_id = b.id
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