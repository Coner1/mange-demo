package uap.web.example.web.sign;

import com.yzqc.support.security.SecurityException;
import com.yzqc.support.security.client.ApplicationClientToken;
import com.yzqc.support.security.client.ApplicationRequestSigner;
import com.yzqc.support.security.client.ApplicationTokenLoader;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;


@Controller
@RequestMapping(value = "/sign")
public class SignController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "signRequest", method = RequestMethod.POST)
    public @ResponseBody JSONObject signRequest(@RequestParam(value = "signUrl") String signUrl) {
        JSONObject json = new JSONObject();
        //模拟客户端解析令牌，对请求进行签名；
        String clientType = "UAP_MOBILE";
        ApplicationClientToken clientToken;
        try {
            String clientTokenStr = FileUtils.readFileToString(new java.io.File("G:\\client.txt"));
            clientToken = ApplicationTokenLoader.resolve(clientTokenStr);
            ApplicationRequestSigner signer = new ApplicationRequestSigner(clientToken);
            String sign = signer.sign(signUrl);
            json.put("result", sign);
            return json;
        } catch (IOException e) {
            logger.error("sign error!", e);
            e.printStackTrace();
        } catch (SecurityException e) {
            logger.error("sign error!", e);
            e.printStackTrace();
        }
        json.put("result", "fail");
        return json;
    }
}
