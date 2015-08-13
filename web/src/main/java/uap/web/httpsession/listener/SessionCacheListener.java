package uap.web.httpsession.listener;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import uap.web.httpsession.cache.SessionCacheManager;

public class SessionCacheListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent httpSessionEvent) {  
		HttpSession session = httpSessionEvent.getSession();  
        // do nothing  
        System.out.println("-----sessionCreated------" + session.getId());
//        SessionCacheManager.getInstance().putSessionCacheAttribute(session.getId(), "test1", "test1");
//        SessionCacheManager.getInstance().putSessionCacheAttribute(session.getId(), "test2", 2);
//        SessionCacheManager.getInstance().putSessionCacheAttribute(session.getId(), "test3", 3L);
//        Map<String,Object> map = SessionCacheManager.getInstance().getAllSessionAttrCache(session.getId());
//        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
//			String key = iterator.next();
//			System.out.println(map.get(key));
//		}
    }  
  
    //session失效时同时移除缓存内容  
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {  
        HttpSession session = httpSessionEvent.getSession();  
        System.out.println("-----sessionDestroyed------" + session.getId());
        SessionCacheManager.getInstance().removeSessionCache(session.getId());  
    }

}
