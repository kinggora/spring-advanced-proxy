package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.config.v4_postprocessor.postprocessor.PackageLogTracePostProcessor;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

    //@Bean
    public Advisor adviser1(LogTrace logTrace) {
        //Pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        //Advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        //Advisor
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    //@Bean
    public Advisor adviser2(LogTrace logTrace) {
        //Pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..))");
        //Advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        //Advisor
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    @Bean
    public Advisor adviser3(LogTrace logTrace) {
        //Pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))");
        //Advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        //Advisor
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}