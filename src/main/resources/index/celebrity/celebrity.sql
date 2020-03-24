/**
 * 名人
 */

select t.id relateId,t.id, t.name,t.alias,t.full_spell fullSpell, t.simple_spell simpleSpell,t.category,
t.summary, t.description, t.slogan,t.sex,t.birth_date birthDate,t.pass_away_date passAwayDate,t.birthplace,t.nation,t.type,t.weight,
t.behalf_achievement behalfAchievement, t.chief_behalf chiefBehalf,t.story,t.status,t.weight,
t.dept_code deptCode,'' images,'' videos,'' audios, '' tags,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus,
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