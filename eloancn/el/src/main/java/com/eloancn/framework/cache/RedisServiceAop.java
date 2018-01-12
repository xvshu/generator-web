package com.eloancn.framework.cache;
/**
 * @Package com.eloancn.framework.cache
 * @Title: RedisServiceAop
 * @author hapic
 * @date 2018/1/2 15:43
 * @version V1.0
 */

import com.dianping.cat.message.Transaction;
import com.eloancn.framework.exception.BusinessException;
import com.eloancn.framework.ump.Profiler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Descriptions: 切割redis各个方法
 */
@Aspect
@Slf4j
@Component
public class RedisServiceAop {

    private final String catType="ware.redis";

    /**
     * 拦截除getRedisTemplate外的所有方法
     * @param jp
     * @return
     * @throws Throwable
     */
    @Around(value = "execution(* com.eloancn.framework.cache.RedisService.*(..))  " +
            "&& !execution(* com.eloancn.framework.cache.RedisService.getRedisTemplate())")
    public Object processTx(ProceedingJoinPoint jp) throws Throwable {

        // 访问执行目标方法的参数
        Object[] args = jp.getArgs();
        // 当执行目标方法的参数存在
        // 且第一个参数是字符串参数
        if (args == null && args.length < 1) {
            log.info("args is null or length < 1");
           throw new BusinessException("参数异常！");
        }
        Signature sig = jp.getSignature();
        MethodSignature msig = null;
        StringBuffer methodName=new StringBuffer();
        if (sig instanceof MethodSignature) {
//            throw new IllegalArgumentException("该注解只能用于方法");
            msig = (MethodSignature) sig;
            Object target = jp.getTarget();
            methodName.append(target.getClass().getSimpleName());
            methodName.append(".");
            methodName.append(msig.getName());

        }


        Transaction transaction = Profiler.registerInfo(catType, methodName.toString()+":"+String.valueOf(args[0]));
        log.info("catType:{},method:{}",catType,methodName.toString()+":"+String.valueOf(args[0]));
        Object rvt = null;

        try {

            // 执行目标方法，并保存目标方法执行后的返回值
            rvt = jp.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            //添加ip和端口信息
            String hostPortInfo = addHostPortInfo(jp.getTarget());
            Profiler.logInfo("redis","ERROR",hostPortInfo,throwable.getMessage());
            Profiler.functionError(transaction,hostPortInfo,throwable);

            throw  throwable;
        } finally {
            Profiler.registerEnd(transaction);
        }
        return rvt;
    }

    private String addHostPortInfo(Object obj) {
        try {
            if(obj instanceof RedisService){
                RedisService redisServiceTarget = (RedisService)obj;
                RedisTemplate<String, Object> redisTemplate = redisServiceTarget.getRedisTemplate();
                RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
                if(connectionFactory instanceof JedisConnectionFactory){
                    JedisConnectionFactory jedisConnectionFactory=(JedisConnectionFactory)connectionFactory;
                    String s = jedisConnectionFactory.getHostName() + ":" + jedisConnectionFactory.getPort();
                    log.info("add redis info to cat "+s);
                    return s;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.toString());
        }
        return "";
    }
}
