package uap.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DisableUrlSessionFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest hr = (HttpServletRequest) request;
			System.out.println(hr.getRequestURL());
			String url = hr.getRequestURL().toString();
			if(url.indexOf("jsessionid")>0){
				String redirectUrl = url.substring(0, url.indexOf("jsessionid")-1);
				HttpServletResponse rp = (HttpServletResponse) response;
				rp.sendRedirect(redirectUrl);
			}
		}
		
		chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
	}

	public void destroy() {
	}
}
