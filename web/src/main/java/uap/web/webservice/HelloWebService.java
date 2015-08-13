package uap.web.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import uap.web.example.entity.Demo;

@WebService(name = "hellowebservice")
public interface HelloWebService {
	
	 @WebMethod  
	 @WebResult String sayHi();

	 @WebMethod
	 @WebResult Demo getDemoById(@WebParam(name = "id") Long id);

}
