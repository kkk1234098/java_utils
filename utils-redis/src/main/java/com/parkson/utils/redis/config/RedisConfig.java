package com.parkson.utils.redis.config;

import com.parkson.utils.redis.util.FastJson2JsonRedisSerializer;
import com.parkson.utils.redis.util.RedisTemplate;
import com.parkson.utils.redis.util.RedisUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @ClassName RedisConfig
 * @Description TO DO
 * @Author chenyijun
 * @Date 2020/8/24 4:21 下午
 * @Version 1.0
 */
@Configuration
@PropertySource("classpath:redis.properties")
public class RedisConfig {

    private static Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${redis.hostName}")
    private String hostName;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.port}")
    private Integer port;

    // 连接池的最大数据库连接数。设为0表示无限制, 默认8
    @Value("${redis.maxTotal:8}")
    private Integer maxTotal;

    // 最大能够保持idle状态的对象数，默认值8
    @Value("${redis.maxIdle:8}")
    private Integer maxIdle;

    // 最少能够保持idle状态的对象数，默认值0
    @Value("${redis.minIdle:0}")
    private Integer minIdle;

    // 客户端超时(connectTimeout, readTimeout)时间单位是毫秒, 默认是2000
    @Value("${redis.timeout:2000}")
    private Integer timeout;

    // 最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。默认值-1
    @Value("${redis.maxWaitMillis:-1}")
    private Integer maxWaitMillis;

    // 默认60000
    @Value("${redis.minEvictableIdleTimeMillis:60000L}")
    private Integer minEvictableIdleTimeMillis;

    // 每次检查几个链接，默认值3
    @Value("${redis.numTestsPerEvictionRun:3}")
    private Integer numTestsPerEvictionRun;

    // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
    @Value("${redis.timeBetweenEvictionRunsMillis:-1}")
    private long timeBetweenEvictionRunsMillis;

    // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个, 默认false
    @Value("${redis.testOnBorrow:false}")
    private boolean testOnBorrow;

    // 是否在返回时检查连接有效性
    @Value("${redis.testOnReturn:true}")
    private boolean testOnReturn;

    // 在空闲时检查有效性, 默认true
    @Value("${redis.testWhileIdle:true}")
    private boolean testWhileIdle;

    // 是否开启事务，默认值false
    @Value("${redis.enableTransactionSupport:false}")
    private boolean enableTransactionSupport;

    /**
     * @auther: cheneason
     * @date: 17:52 2018/8/28
     * @param: []
     * @return: org.springframework.data.redis.connection.jedis.JedisConnectionFactory
     * @Description: Jedis配置
     */
    @Bean
    public JedisConnectionFactory JedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(hostName);
        redisStandaloneConfiguration.setPort(port);
        //由于我们使用了动态配置库,所以此处省略
        //redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jedisPoolingClientConfigurationBuilder = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)jedisClientConfiguration;

        /* ========= 连接池通用配置 ========= */
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(maxTotal);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMaxWaitMillis(maxWaitMillis);
        genericObjectPoolConfig.setTestOnBorrow(testOnBorrow);
        genericObjectPoolConfig.setTestOnReturn(testOnReturn);
        genericObjectPoolConfig.setTestWhileIdle(testWhileIdle);
        genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        genericObjectPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        genericObjectPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);

        jedisClientConfiguration.usePooling();
        jedisClientConfiguration.connectTimeout(Duration.ofMillis(timeout));
        jedisClientConfiguration.readTimeout(Duration.ofMillis(timeout));
        jedisPoolingClientConfigurationBuilder.poolConfig(genericObjectPoolConfig);
        JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration.build());
        return factory;
    }

    /**
     * @auther: cheneason
     * @date: 17:52 2018/8/28
     * @param: [redisConnectionFactory]
     * @return: com.springboot.demo.base.utils.RedisTemplate
     * @Description: 实例化 RedisTemplate 对象
     */
    @Bean
    public RedisTemplate functionDomainRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("RedisTemplate实例化成功！");
        RedisTemplate redisTemplate = new RedisTemplate();
        initDomainRedisTemplate(redisTemplate, redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * @auther: cheneason
     * @date: 17:52 2018/8/28
     * @param: []
     * @return: org.springframework.data.redis.serializer.RedisSerializer
     * @Description: 引入自定义序列化
     */
    @Bean
    public RedisSerializer fastJson2JsonRedisSerializer() {
        return new FastJson2JsonRedisSerializer<Object>(Object.class);
    }

    /**
     * @auther: cheneason
     * @date: 17:51 2018/8/28
     * @param: [redisTemplate, factory]
     * @return: void
     * @Description: 设置数据存入 redis 的序列化方式,并开启事务
     */
    private void initDomainRedisTemplate(RedisTemplate redisTemplate, RedisConnectionFactory factory) {
        //如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer());
        // 开启事务
        redisTemplate.setEnableTransactionSupport(enableTransactionSupport);
        redisTemplate.setConnectionFactory(factory);
    }

    /**
     * @auther: cheneason
     * @date: 17:51 2018/8/28
     * @param: [redisTemplate]
     * @return: com.springboot.demo.base.utils.RedisUtil
     * @Description: 注入封装RedisTemplate
     */
    @Bean(name = "redisUtil")
    public RedisUtil redisUtil(RedisTemplate redisTemplate) {
        log.info("RedisUtil注入成功！");
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        //初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        //设置CacheManager的值序列化方式为json序列化
        RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer();
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
                .fromSerializer(jsonSerializer);
        //设置默认超过期时间是1天
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(1))
                .serializeValuesWith(pair);
        //初始化RedisCacheManager
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
        log.info("CacheManager配置完成！");
        return redisCacheManager;
    }
}
