package uap.web.httpsession.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uap.web.httpsession.cache.SessionCacheManager;

public class SessionFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected static final String[] noFilterPages = {"login"};  

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;

		String url = request.getRequestURI();
		for (String c : SessionFilter.noFilterPages) {
			if (url.indexOf(c) > 0) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
		}

		String requestedSessionId = request.getRequestedSessionId();
		HttpSession session = request.getSession();
		if (requestedSessionId==null || session.getId().equals(requestedSessionId)) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}
		/*
		Map<String, Object> _map = SessionCacheManager.getInstance().getSessionCache(session.getId());
		if (_map == null) {
			Map<String, Object> map = SessionCacheManager.getInstance().getSessionCache(requestedSessionId);
			if (map == null) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			logger.info("开始转移...");
			SessionCacheManager.getInstance().initSessionCache(session.getId(), map);
		}
		*/

		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {
	}

}
