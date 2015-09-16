package example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    // @Bean
    // JedisConnectionFactory jedisConnectionFactory() {
    // JedisConnectionFactory redis = new JedisConnectionFactory();
    // redis.setHostName("localhost");
    // redis.setPort(6379);
    // return redis;
    // }

    @Bean
    RedisTemplate<String, String> redisTemplate() {
        final RedisTemplate<String, String> template = new RedisTemplate<String, String>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericToStringSerializer<String>(String.class));
        template.setValueSerializer(new GenericToStringSerializer<String>(String.class));
        return template;
    }

}
