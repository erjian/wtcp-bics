/**
 * 非遗
 */

select t.id relateId,t.id,t.`code`, t.title,t.sub_title subTitle,t.full_spell fullSpell, t.simple_spell simpleSpell,t.slogan,
t.type,t.relate_project relateProject,t.heritage_serial heritageSerial,t.publish_time publishTime,t.declare_group declareGroup,t.guard_group guardGroup,
t.summary, t.description, t.nation,t.`level`,t.category,t.inherit_region inheritRegion,t.celebrity_ids celebrityIds,t.vr_url vrUrl,t.weight,
t.region,t.region_full_code regionFullCode,t.region_full_name regionFullName,
t.dept_code deptCode,(SELECT REPLACE(s.file_url,'\\','/') vrImage FROM t_bic_material s WHERE TRIM(s.file_identify)='vr' AND s.principal_id = t.id) vrImageUrl,	null videoResources,'' images,'' videos,'' audios, '' tags,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus
from t_bic_heritage t;


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

SELECT t.principal_id relateId,	CONCAT('[',	GROUP_CONCAT(CONCAT('{"fileUrl":"',	t.file_url,	'","fileIdentify":"', IFNULL( t.file_identify, '' ), '","coverImageUrl":"',
				IFNULL( t.cover_image_url, '' ), '","fileName":"', IFNULL( t.file_name, '' ), '"}')), ']') videoResources
FROM t_bic_material t
WHERE t.file_type = 'video'	AND t.principal_id IS NOT NULL AND t.principal_id != ''
GROUP BY t.principal_id;