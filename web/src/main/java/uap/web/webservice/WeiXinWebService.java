package uap.web.webservice;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Path(value = "/weixin")   
public class WeiXinWebService {
	
	private static Logger logger = LoggerFactory.getLogger(WeiXinWebService.class);
	
	@GET  
    @Path(value = "/test")  
	public Integer test(@PathParam(value="id") Long id){
		System.out.printf("testtest");
		return 1;
	}
	
	
	@POST
	@Path(value = "/postWebservices")
	public @ResponseBody Object createDemo(@RequestBody HashMap<String,String> map){
		JSONObject jsonObj = new JSONObject();
		try {
			System.out.print(map.get("title"));
			jsonObj.put("msg", "webService调用成功！");
			jsonObj.put("title", map.get("title"));
			jsonObj.put("flag", "1");
		} catch (Exception e) {
			jsonObj.put("msg", "调用失败！");
			jsonObj.put("flag", "0");
			logger.error("失败!");
		}
		return jsonObj;
	}

}
