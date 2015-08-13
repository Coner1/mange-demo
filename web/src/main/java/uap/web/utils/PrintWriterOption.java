package uap.web.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class PrintWriterOption
{
//  private static Logger logger = Logger.getLogger(PrintWriterOption.class);
	private final Logger logger = LoggerFactory.getLogger(getClass());

  public   void outXML(String result, HttpServletResponse response)
  {
    try
    {
      response.setContentType("text/xml; charset=utf-8");
      response.flushBuffer();
      PrintWriter writer = response.getWriter();
      writer.write(String.valueOf(result));
      writer.close();
    } catch (IOException e) {
        logger.error("向前台传递XML信息发生IO异常.");
    }
  }

  public   void outHTML(String result, HttpServletResponse response)
  {
    try
    {
      response.setContentType("text/html; charset=utf-8");
      response.flushBuffer();
      PrintWriter writer = response.getWriter();
      writer.write(String.valueOf(result));
      writer.close();
    } catch (IOException e) {
      logger.error("向前台传递HTML信息发生IO异常.");
    }
  }

  public   void outHTMLForGB2312(String result, HttpServletResponse response)
  {
    try
    {
      response.setContentType("text/html; charset=GB2312");
      response.flushBuffer();
      PrintWriter writer = response.getWriter();
      writer.write(String.valueOf(result));
      writer.close();
    } catch (IOException e) {
      logger.error("向前台传递HTML信息发生IO异常.");
    }
  }

  public   void outHTMLForGBK(String result, HttpServletResponse response) {
    try {
      response.setContentType("text/html; charset=GBK");
      response.flushBuffer();
      PrintWriter writer = response.getWriter();
      writer.write(result);
      writer.close();
    } catch (IOException e) {
       logger.error("向前台传递HTML信息发生IO异常.");
    }
  }
}