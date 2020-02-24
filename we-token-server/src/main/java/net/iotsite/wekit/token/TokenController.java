package net.iotsite.wekit.token;

import net.iotsite.wekit.token.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("token")
public class TokenController {

    @Autowired
    private TokenService tokenCache;

    @GetMapping("/apply")
    public String token() throws TokenException {
        return tokenCache.apply().toJSONSting();
    }
}
