package org.ruott.provide;

import org.ruott.rpcstarter.annotate.EnableRuottRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@EnableRuottRpc(needServer = true)
@SpringBootApplication
public class ProvideBoot {

    public static void main(String[] args) {
        SpringApplication.run(ProvideBoot.class, args);
    }

}
