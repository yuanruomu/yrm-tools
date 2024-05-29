package cn.yrm.tools.service.common;

import cn.dev33.satoken.SaManager;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import cn.yrm.tools.common.annotation.RequestLock;
import cn.yrm.tools.common.exception.BusinessException;
import cn.yrm.tools.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求锁切面
 *
 * @author yuanr
 */
@Slf4j
@Aspect
@Component
public class RequestLockAspect {
    @Autowired
    RedissonClient redissonClient;
    private static final String PARAM_PATTERN = "#\\{[^{}]*\\}";
    private static final String DEFAULT_PARAM_LOGIN = "#{login}";
    private static final String DEFAULT_PARAM_IP = "#{ip}";

    /**
     * 切入点
     */
    @Pointcut("@annotation(cn.yrm.tools.common.annotation.RequestLock)")
    public void pt() {
    }

    @Around("pt()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取自定义注解
        RequestLock repeatLock = method.getAnnotation(RequestLock.class);
        String lockKey = repeatLock.value();
        if (StrUtil.isEmpty(lockKey)) {
            Object result = joinPoint.proceed();
            return result;
        }
        Pattern paramPattern = Pattern.compile(PARAM_PATTERN);
        Matcher matcher = paramPattern.matcher(lockKey);
        JSON paramJson = null;
        while (matcher.find()) {
            String paramPath = matcher.group();
            // #{ip}
            if (DEFAULT_PARAM_IP.equals(paramPath) || DEFAULT_PARAM_LOGIN.equals(paramPath)) {
                HttpServletRequest request = getHttpServletRequest(joinPoint.getArgs());
                if (DEFAULT_PARAM_IP.equals(paramPath)) {
                    lockKey = lockKey.replace(DEFAULT_PARAM_IP, ServletUtil.getClientIP(request));
                } else {
                    String token = request.getHeader(SaManager.getConfig().getTokenName());
                    if (StrUtil.isEmpty(token)) {
                        throw new BusinessException("用户登录状态异常");
                    }
                    JSONObject jsonObject = JWTUtil.parseToken(token).getPayloads();
                    String loginId = jsonObject.getStr("loginId");
                    lockKey = lockKey.replace(DEFAULT_PARAM_LOGIN, loginId);
                }
            } else {
                // 其他对应参数名称
                if (paramJson == null) {
                    HashMap<String, Object> paramMap = new HashMap<>();
                    for (int i = 0; i < method.getParameters().length; i++) {
                        paramMap.put(method.getParameters()[i].getName(), joinPoint.getArgs()[i]);
                    }
                    paramJson = JSONUtil.parse(paramMap);
                }
                lockKey = lockKey.replace(paramPath, String.valueOf(JSONUtil.getByPath(paramJson, paramPath.substring(2, paramPath.length() - 1))));
            }
        }
        // 锁定KEY值
        RLock lock = redissonClient.getLock(repeatLock.value() + lockKey);
        log.info("执行操作锁: " + lockKey);
        boolean res = lock.tryLock();
        if (!res) {
            return Result.error("请勿重复提交或者操作过于频繁");
        }
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            lock.unlock();
        }
    }

    private HttpServletRequest getHttpServletRequest(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && args[i] instanceof HttpServletRequest) {
                return (HttpServletRequest) args[i];
            }
        }
        throw new BusinessException("缺少HttpServletRequest参数");
    }
}
