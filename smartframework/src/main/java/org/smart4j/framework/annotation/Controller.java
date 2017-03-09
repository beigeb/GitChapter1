package org.smart4j.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ibm on 2017/3/8.
 * 控制器注解
 */
// 指定使用位置 TYPR 代表 在类上
@Target ( ElementType.TYPE )
// 指定注解保留阶段  RUNTIME 代表 被JVM 保留 在运行时使用
@Retention ( RetentionPolicy.RUNTIME )
public @interface Controller {
}
