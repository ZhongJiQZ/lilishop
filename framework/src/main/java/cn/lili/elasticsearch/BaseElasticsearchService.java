package cn.lili.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

/**
 * @author paulG
 * @since 2020/10/14
 **/
@Slf4j
public abstract class BaseElasticsearchService {

    protected static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();

        //默认缓冲限制为100MB，此处修改为30MB。
        builder.setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Autowired
    public ElasticsearchOperations client;

}
