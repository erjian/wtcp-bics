/**
 * 名人
 */

select t.id relateId,t.id, t.name,t.alias,t.full_spell fullSpell, t.simple_spell simpleSpell,t.category,
t.summary, t.description, t.slogan,t.sex,t.birth_date birthDate,t.pass_away_date passAwayDate,t.birthplace,t.nation,t.type,t.weight,
t.behalf_achievement behalfAchievement, t.chief_behalf chiefBehalf,t.story,t.status,t.weight,
t.dept_code deptCode,(SELECT REPLACE(s.file_url,'\\','/') vrImage FROM t_bic_material s WHERE TRIM(s.file_identify)='vr' AND s.principal_id = t.id ORDER BY s.created_date DESC LIMIT 1) vrImageUrl,	null videoResources,'' images,'' videos,'' audios, '' tags,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus
from t_bic_celebrity t;


select b.id relateId,CONCAT('[',GROUP_CONCAT('"',t.tag_name,'"'),']') tags
from t_bic_celebrity_tags t LEFT JOIN t_bic_celebrity b on t.principal_id = b.id
where t.principal_id in(select a.id from t_bic_celebrity a  ) GROUP BY t.principal_id;

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