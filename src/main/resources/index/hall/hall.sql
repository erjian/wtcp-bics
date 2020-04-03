/**
 * 厅室
 */

select t.id relateId,t.id,t.`code`, t.title,t.full_spell fullSpell, t.simple_spell simpleSpell,
t.summary, t.description,t.category,t.area,t.weight,t.vr_url vrUrl,t.max_reception_num maxReceptionNum,
t.latitude, t.longitude,CONCAT(t.latitude,",",t.longitude) geoPoint,t.venue_id venueId,
t.dept_code deptCode,'' vrCoverImage,'' videoCoverImage,'' images,'' videos,'' audios, '' tags,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus
from t_bic_hall t;


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