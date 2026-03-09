package cn.lili.common.sensitive.init;

import cn.lili.cache.Cache;
import cn.lili.cache.CachePrefix;
import cn.lili.common.sensitive.SensitiveWordsFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 敏感词加载
 *
 * @author Chopper
 * @version v1.0
 * 2021-11-23 12:08
 */
@Component
@Slf4j
public class SensitiveWordsLoader implements ApplicationRunner {

    @Autowired
    private Cache<List<String>> cache;

    /**
     * 程序启动时，获取最新的需要过滤的敏感词
     * <p>
     * 这里即便缓存中为空也没关系，定时任务会定时重新加载敏感词
     *
     * @param args 启动参数
     */
    @Override
    public void run(ApplicationArguments args) {
        log.info("系统初始化敏感词");
        try {
            List<String> sensitives = cache.get(CachePrefix.SENSITIVE.getPrefix());
            if (sensitives == null || sensitives.isEmpty()) {
                log.warn("敏感词缓存为空，跳过初始化");
                return;
            }
            SensitiveWordsFilter.init(sensitives);
            log.info("敏感词初始化完成，共加载 {} 个敏感词", sensitives.size());
        } catch (Exception e) {
            log.error("敏感词初始化失败，可能是缓存数据格式错误", e);
            // 清除可能损坏的缓存数据
            try {
                cache.remove(CachePrefix.SENSITIVE.getPrefix());
                log.info("已清除损坏的敏感词缓存数据");
            } catch (Exception clearException) {
                log.error("清除敏感词缓存失败", clearException);
            }
        }
    }

}
