package com.lolsearcher.reactive.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.env.Environment;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CacheUtils {

    private final Environment environment;

    private final ExpressionParser expressionParser = new SpelExpressionParser();

    public Method getMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod();
    }

    public TypeReference getTypeReference(Method method) {
        return new TypeReference<>() {
            @Override
            public Type getType() {
                return getMethodActualReturnType(method);
            }
        };
    }

    public String resolveKey(JoinPoint joinPoint, String key) {

        String[] parameterNames = getParamNames(joinPoint); //파라미터 이름
        Object[] args = joinPoint.getArgs(); //실제 값

        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        if (StringUtils.hasText(key)) {
            if (key.contains("#")) {
                return (String) expressionParser.parseExpression(key).getValue(context);
            }
            return key;
        }
        throw new RuntimeException("어노테이션 key 설정 값이 잘못되었습니다. 다시 한 번 확인해주세요.");
    }

    public String resolveCompKey(String name, String key) {

        if(name.equals("")){
            return key;
        }
        return name + ":" + key;
    }

    public Duration resolveTtl(String ttl) {

        if(ttl.charAt(0) == '$'){
            int time =  Integer.parseInt(environment.getProperty(ttl.substring(2, ttl.length()-1)));

            return Duration.ofSeconds(time);
        }
        int time = Integer.parseInt(ttl);

        return Duration.ofSeconds(time);
    }

    public String[] getParamNames(JoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        return codeSignature.getParameterNames();
    }

    private Type getMethodActualReturnType(Method method) {
        return ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
    }
}
