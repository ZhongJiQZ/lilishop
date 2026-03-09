package cn.lili.common.sensitive.quartz;

import cn.lili.cache.Cache;
import cn.lili.cache.CachePrefix;
import cn.lili.common.sensitive.SensitiveWordsFilter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * 间隔更新敏感词
 *
 * @author Chopper
 * @version v1.0
 * 2021-11-23 16:31
 */
@Slf4j
public class SensitiveQuartz extends QuartzJobBean {

    @Autowired
    private Cache<List<String>> cache;

    /**
     * 定时更新敏感词信息
     *
     * @param jobExecutionContext
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        log.info("敏感词定时更新");
        try {
            List<String> sensitives = cache.get(CachePrefix.SENSITIVE.getPrefix());
            if (sensitives == null || sensitives.isEmpty()) {
                log.warn("敏感词缓存为空，跳过本次更新");
                return;
            }
            SensitiveWordsFilter.init(sensitives);
            log.info("敏感词更新完成，共加载 {} 个敏感词", sensitives.size());
        } catch (Exception e) {
            log.error("敏感词定时更新失败，可能是缓存数据格式错误", e);
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