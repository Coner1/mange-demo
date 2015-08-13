package uap.web.example.web.demo;

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
import uap.web.utils.HttpTookit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/restful")
public class RestFulServiceCaller {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "call", method = RequestMethod.POST)
    public @ResponseBody Object call(@RequestParam(value = "callUrl") String callUrl) {
        //模拟客户端解析令牌，对请求进行签名；
        String clientType = "UAP_MOBILE";
        ApplicationClientToken clientToken;
        try {
            //获取方式待优化
            String clientTokenStr = FileUtils.readFileToString(new java.io.File("G:\\client.txt"));
            clientToken = ApplicationTokenLoader.resolve(clientTokenStr);
            ApplicationRequestSigner signer = new ApplicationRequestSigner(clientToken);
            String sign = signer.sign(callUrl);


            // 发起http请求
            Map<String, String> params = new HashMap<String, String>();
            params.put("sign", sign);
            params.put("clientType", clientType);
            String result = HttpTookit.doPost(callUrl, params);
            logger.error(result);
            return result;
        } catch (IOException e) {
            logger.error("sign error!", e);
            e.printStackTrace();
        } catch (SecurityException e) {
            logger.error("sign error!", e);
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("result", "fail");
        return json;
    }
}
