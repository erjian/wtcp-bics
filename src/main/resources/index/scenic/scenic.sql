/**
 * 景区
 */

select t.id relateId,t.id,t.`code`, t.title,t.sub_title subTitle,t.title_qp fullSpell, t.title_jp simpleSpell,t.slogan,
t.rank,t.start_time startTime,t.summer_time summerTime, t.winter_time winterTime,t.light_title lightTitle,t.summary, t.description, t.address,t.`level`,t.category,t.area,t.panoramic_url1 panoramicUrl1,t.panoramic_url2 panoramicUrl2,
t.panoramic_url3 panoramicUrl3,t.panoramic_url4 panoramicUrl4,t.playtime,t.score,t.weight,
t.latitude, t.longitude,CONCAT(t.latitude,",",t.longitude) geoPoint,t.region,t.region_full_code regionFullCode,t.region_full_name regionFullName,
t.dept_code deptCode,(SELECT REPLACE(s.file_url,'\\','/') vrImage FROM t_bic_material s WHERE TRIM(s.file_identify)='vr' AND s.principal_id = t.id ORDER BY s.created_date DESC LIMIT 1) vrImageUrl,null videoResources,'' images,'' videos,'' audios, '' tags,'' relateTags, '' allTags, if(t.status=9, 1,0) publishStatus,
(select b.service_facility from t_bic_business b where b.principal_id = t.id) as serviceFacility,
(select b.phone from t_bic_business b where b.principal_id = t.id) as phone
from t_bic_scenic t;


select b.id relateId,CONCAT('[',GROUP_CONCAT('"',t.tag_name,'"'),']') tags
from t_bic_scenic_tags t LEFT JOIN t_bic_scenic b on t.principal_id = b.id
where t.principal_id in(select a.id from t_bic_scenic a  ) GROUP BY t.principal_id;

select t.principal_id relateId, CONCAT('[',GROUP_CONCAT('"',REPLACE(t.file_url,'\\','/'),'"'),']') images
from t_bic_material t
where t.file_type = 'image' and t.principal_id is not null
GROUP BY t.principal_id;

select t.principal_id relateId, CONCAT('[',GROUP_CONCAT('"',REPLACE(t.file_url,'\\','/'),'"'),']') videos
from t_bic_material t
where t.file_type = 'video' and t.principal_id is not null
GROUP BY t.principal_id;

SELECT t.principal_id relateId,	CONCAT('[',	GROUP_CONCAT(CONCAT('{"fileUrl":"',	t.file_url,	'","fileIdentify":"', IFNULL( t.file_identify, '' ), '","coverImageUrl":"',
				IFNULL( t.cover_image_url, '' ), '","fileName":"', IFNULL( t.file_name, '' ), '"}')), ']') videoResources
FROM t_bic_material t
WHERE t.file_type = 'video'	AND t.principal_id IS NOT NULL AND t.principal_id != ''
GROUP BY t.principal_id;