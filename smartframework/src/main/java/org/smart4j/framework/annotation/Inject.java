package org.smart4j.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ibm on 2017/3/8.
 */
@Target ( ElementType.FIELD )
@Retention ( RetentionPolicy.RUNTIME )
public @interface Inject {
}
