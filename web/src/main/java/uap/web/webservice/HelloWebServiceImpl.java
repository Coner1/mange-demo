package uap.web.webservice;

import javax.jws.WebResult;

import org.springframework.beans.factory.annotation.Autowired;

import uap.web.example.entity.Demo;
import uap.web.example.service.demo.DemoService;

public class HelloWebServiceImpl implements HelloWebService {

	@Autowired
	DemoService demoService;
	
	public @WebResult String sayHi() {
		return "Hello liujmc！";
	}

	public Demo getDemoById(Long id) {
		return demoService.getDemoById(id);
	}
}
