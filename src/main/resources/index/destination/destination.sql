/**
 * 目的地
 */

select t.id relateId,t.id,t.weight,t.introduce,t.eat_introduce eatIntroduce,t.drink_introduce drinkIntroduce,
t.play_introduce playIntroduce,t.tourism_introduce tourismIntroduce,t.shop_introduce shopIntroduce,
t.entertainment_introduce entertainmentIntroduce,t.region,t.region_full_code regionFullCode,t.region_full_name regionFullName,
t.dept_code deptCode,'' images,'' videos,'' audios, '' tags,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus
from t_bic_destination t ;


select b.id relateId,CONCAT('[',GROUP_CONCAT('"',t.tag_name,'"'),']') tags
from t_bic_destination_tags t LEFT JOIN t_bic_destination b on t.principal_id = b.id
where t.principal_id in(select a.id from t_bic_destination a  ) GROUP BY t.principal_id;

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