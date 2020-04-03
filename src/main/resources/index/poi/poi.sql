/**
 * POI
 */

select t.id relateId,t.id,t.`code`,t.parent_id parentId,t.principal_id principalId,t.title,t.sub_title subTitle,t.weight,t.slogan,
t.summary,t.description,t.content,t.address,t.type,t.num,t.man_pit manPit,t.woman_pit womanPit,t.inside_scenic insideScenic,
t.latitude, t.longitude,CONCAT(t.latitude,",",t.longitude) geoPoint,t.region,t.region_full_code regionFullCode,t.region_full_name regionFullName,
t.dept_code deptCode,'' vrCoverImage,'' videoCoverImage,'' images,'' videos,'' audios, '' tags,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus
from t_bic_poi t ;


select b.id relateId,CONCAT('[',GROUP_CONCAT('"',t.tag_name,'"'),']') tags
from t_bic_poi_tags t LEFT JOIN t_bic_poi b on t.principal_id = b.id
where t.principal_id in(select a.id from t_bic_poi a  ) GROUP BY t.principal_id;

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

SELECT t.principal_id relateId,	CONCAT('[', GROUP_CONCAT(CONCAT('{"fileUrl":"', t.file_url, '","fileIdentify":"',IFNULL( t.file_identify, '' ),
				'","coverImageUrl":"', IFNULL( t.cover_image_url, '' ),	'","fileName":"', IFNULL( t.file_name, '' ), '"}')), ']') vrCoverImage
FROM t_bic_material t
WHERE t.file_type = 'image'	AND t.principal_id IS NOT NULL AND t.principal_id != ''
GROUP BY t.principal_id;

SELECT t.principal_id relateId,	CONCAT('[',	GROUP_CONCAT(CONCAT('{"fileUrl":"',	t.file_url,	'","fileIdentify":"', IFNULL( t.file_identify, '' ), '","coverImageUrl":"',
				IFNULL( t.cover_image_url, '' ), '","fileName":"', IFNULL( t.file_name, '' ), '"}')), ']') videoCoverImage
FROM t_bic_material t
WHERE t.file_type = 'video'	AND t.principal_id IS NOT NULL AND t.principal_id != ''
GROUP BY t.principal_id;