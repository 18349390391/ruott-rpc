package org.ruott.consumer;


import org.ruott.rpcstarter.annotate.EnableRuottRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRuottRpc(needServer = true)
@SpringBootApplication
public class ConsumerBoot {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerBoot.class, args);
    }

}
