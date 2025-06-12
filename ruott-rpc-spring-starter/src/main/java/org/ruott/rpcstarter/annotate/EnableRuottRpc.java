package org.ruott.rpcstarter.annotate;


import org.ruott.rpcstarter.bootstrap.RpcConsumerBootStrap;
import org.ruott.rpcstarter.bootstrap.RpcInitBootStrap;
import org.ruott.rpcstarter.bootstrap.RpcServiceBootStrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcConsumerBootStrap.class, RpcInitBootStrap.class, RpcServiceBootStrap.class})
public @interface EnableRuottRpc {

    boolean needServer() default false;
}
