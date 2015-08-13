package uap.web.example.web.weixin;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

@Controller
@RequestMapping(value = "/weixin")
public class WeiXinController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value="msgnum", method=RequestMethod.GET)  
	public @ResponseBody String processGetRequest() { 
		
	//	logger.error("js 轮训调度  call me ... ");
		
		int num = new Random().nextInt(100);
		
		String echostr = "{msgmum:"+num+"}";
		
		return echostr;
	}  
	
	@RequestMapping(value="postinfo")  
	public @ResponseBody String processPostRequest(HttpServletRequest request) { 
		
		String name = request.getParameter("name") ;
		int num = new Random().nextInt(100);
		
		String echostr = "{postresult:"+num+"}";
		
		return echostr;
	}  
	
	@RequestMapping(value="processRequest", method=RequestMethod.GET)  
	public @ResponseBody String processGetRequest(HttpServletRequest request, HttpServletResponse response) { 
		
		logger.error("weixin call me ... ");
		
		String echostr = request.getParameter("echostr");
		
		logger.error("yeah jianmin! processRequest...");
		
		return echostr;
	}  
	
	@RequestMapping(value="processRequest", method=RequestMethod.POST)  
	public @ResponseBody String processRequest(HttpServletRequest request, HttpServletResponse response) { 
		
		logger.error("weixin call me ... ");
		
		acceptMessage(request, response);
		
		//return "success";
		
		String returnMsg = getReturnMsg();
		return returnMsg;
	}

	private String getReturnMsg() {
		String returnMsg = "";
		try {
//			<?xml version="1.0" encoding="UTF-8"?>
//			<xml>
//				<ToUserName><![CDATA[ox6cVs7zRf13V6e8ABHXWJAZcjcE]]></ToUserName>
//				<FromUserName><![CDATA[gh_128749e4e746]]></FromUserName>
//				<CreateTime>1433570261</CreateTime>
//				<MsgType><![CDATA[text]]></MsgType>
//				<Content><![CDATA[i am real jian min]]></Content>
//			</xml>
			returnMsg = FileUtils.readFileToString(new java.io.File("g:\\test.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnMsg;
	}  
	
	private void acceptMessage(HttpServletRequest request, HttpServletResponse response){
		try {
			// 处理接收消息
			ServletInputStream in = request.getInputStream();
			// 将流转换为字符串
			StringBuilder xmlMsg = new StringBuilder();
			byte[] b = new byte[4096];
			for (int n; (n = in.read(b)) != -1;) {
				xmlMsg.append(new String(b, 0, n, "UTF-8"));
			}
			logger.error(xmlMsg.toString());
			Document doc = DocumentHelper.parseText(xmlMsg.toString());
			logger.error(doc.asXML());
			Element root = doc.getRootElement();
			Element contentEle = root.element("Content");
			String cValue = contentEle.getText();
			
			logger.error("用户发送的消息体是:" + cValue);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
