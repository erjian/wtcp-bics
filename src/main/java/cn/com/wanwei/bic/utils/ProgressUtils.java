package cn.com.wanwei.bic.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ProgressUtils {

    @Async
    public void setProgress(RedisTemplate redisTemplate, String progressKey) throws Exception {
        Integer[] num = new Integer[]{10, 18, 23, 30, 39, 50, 66, 73, 88, 96, 100};
        for(Integer item : num){
            if(item <= 100){
                redisTemplate.opsForValue().set(progressKey, item);
                Thread.sleep(500);
                if(item == 100){
                    Thread.sleep(1000);
                    redisTemplate.delete(progressKey);
                }
            }
        }
    }

}
