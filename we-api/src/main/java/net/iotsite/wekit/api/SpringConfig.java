package net.iotsite.wekit.api;


import net.iotsite.wekit.token.TokenException;
import net.iotsite.wekit.token.service.TokenCacheBuilder;
import net.iotsite.wekit.token.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Value("${wekit.token.server}")
    private String tokenServer;

    @Bean
    public TokenService tokenService() throws TokenException {
        return TokenCacheBuilder.builder(tokenServer);
    }

}
