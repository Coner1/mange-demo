package uap.web.httpsession.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializUtil {
	private static final Logger logger = LoggerFactory.getLogger(SerializUtil.class);
	
	public static void main(String[] args) {
		
		String str = SerializUtil.getStrFromObj(UUID.randomUUID());
		System.out.println(str);
		
		Object obj = SerializUtil.getObjectFromStr(str);
		System.out.println(((UUID)obj).toString());
		
	}

	public static String getStrFromObj(Object obj) {
		String serStr = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(obj);
			serStr = byteArrayOutputStream.toString("ISO-8859-1");
			serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectOutputStream != null) {
					objectOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(byteArrayOutputStream!=null){
					byteArrayOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return serStr;
	}
	
	public static Object getObjectFromStr(String serStr){
		ByteArrayInputStream byteArrayInputStream = null;
		ObjectInputStream objectInputStream = null;
		
		Object obj = null;
		try {
			String redStr = java.net.URLDecoder.decode(serStr, "UTF-8");  
			byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));  
			objectInputStream = new ObjectInputStream(byteArrayInputStream);   
			obj = objectInputStream.readObject();   
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectInputStream != null) {
					objectInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(byteArrayInputStream!=null){
					byteArrayInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return obj;
	}
	
	
	public static Object ByteToObject(byte[] bytes) {
		Object obj = null;
		ByteArrayInputStream bi = null;
		ObjectInputStream oi = null;
		try {
			bi = new ByteArrayInputStream(bytes);
			oi = new ObjectInputStream(bi);
			obj = oi.readObject();
			bi.close();
			oi.close();
		} catch (Exception e) {
			logger.error("session 序列化异常!", e);
		} finally {
			try {
				if(bi!=null){
					bi.close();
					bi = null;
				}
				if(oi!=null){
					oi.close();
					oi = null;
				}
			} catch (Exception exp) {
				logger.error("session 序列化异常!", exp);
			}
		}
		return obj;
	}
	
	public static byte[] ObjectToByte(java.lang.Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bo = null;
		ObjectOutputStream oo = null;
		try {
			bo = new ByteArrayOutputStream();
			oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();
		} catch (Exception e) {
			logger.error("session 序列化异常!", e);
		} finally {
			try {
				if(bo!=null){
					bo.close();
					bo = null;
				}
				if(oo!=null){
					oo.close();
					oo = null;
				}
			} catch (Exception exp) {
				logger.error("session 序列化异常!", exp);
			}
		}
		return bytes;
	}
}
