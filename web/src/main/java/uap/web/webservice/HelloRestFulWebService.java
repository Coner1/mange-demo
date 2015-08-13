package uap.web.webservice;

import javax.jws.WebParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.web.MediaTypes;

import uap.web.example.entity.Demo;
import uap.web.example.service.demo.DemoService;

@Path(value = "/demorestfulws")   
public class HelloRestFulWebService {
	
	private static Logger logger = LoggerFactory.getLogger(HelloRestFulWebService.class);
	
	@Autowired
	private DemoService demoService;
	
	@GET  
    @Path(value = "/{id}.xml")  
	@Produces(MediaTypes.APPLICATION_XML_UTF_8)
	public Demo getEmallDemoById(@PathParam(value="id") Long id){
		logger.info("restful webservice test...");
		return demoService.getDemoById(id);
	}
	
	@POST
	@Path(value = "/save")
	@Consumes(value={"text/xml", "application/json"})
	public long createDemo(@WebParam(name="demo") Demo demo){
		try {
			demoService.saveEntity(demo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return demo.getId();
	}

}
