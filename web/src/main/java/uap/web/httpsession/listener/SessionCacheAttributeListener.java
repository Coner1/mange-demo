package uap.web.httpsession.listener;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

public class SessionCacheAttributeListener implements HttpSessionAttributeListener {

	public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {  
		/*
        HttpSession session = httpSessionBindingEvent.getSession();  
        String key = httpSessionBindingEvent.getName();  
        Object value = httpSessionBindingEvent.getValue();  
        SessionCacheManager.getInstance().putSessionCacheAttribute(session.getId(), key, value);
        */  
    }  
  
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {  
    	/*
        HttpSession session = httpSessionBindingEvent.getSession();  
        String key = httpSessionBindingEvent.getName();  
        SessionCacheManager.getInstance().removeSessionCacheAttribute(session.getId(), key);  
        */
    }  
  
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {  
        //attributeAdded(httpSessionBindingEvent);  
    }  
}  
