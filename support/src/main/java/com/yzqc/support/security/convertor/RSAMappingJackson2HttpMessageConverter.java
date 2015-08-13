package com.yzqc.support.security.convertor;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yzqc.support.security.CryptUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/12.
 */
public class RSAMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private ObjectMapper objectMapper = new ObjectMapper();

    private String jsonPrefix;

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        JsonEncoding encoding = this.getJsonEncoding(outputMessage.getHeaders().getContentType());
        JsonGenerator jsonGenerator = this.objectMapper.getJsonFactory().createJsonGenerator(outputMessage.getBody(),
                encoding);
        if (this.objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
            jsonGenerator.useDefaultPrettyPrinter();
        }
        try {
            if (this.jsonPrefix != null) {
                jsonGenerator.writeRaw(this.jsonPrefix);
            }

            if (StringUtils.equalsIgnoreCase("true", ((ServletServerHttpResponse) outputMessage).getServletResponse().getHeader("bodyEncrypt"))) {
                String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIto_Y$4VF2rIxvH4On7BDIYzzxitgbgLdQps2ka15e6U56blJqeHdbs82f0v3aCc4U1IW2EtG0uDO_uWrFVNP1QEon5PkGOTBFzjlum2wlTKVgYZ4eb8qDDr8_DPEXu6rPda$qvd3IU3YUqJTwCFGnoneTRpDDdK_0q9gKQwvTTAgMBAAECgYBtDqDtAUHeVLaOUPp9j8RkfEwB7SaAknbHdifHnRvysewgM404t2CYdZykQi5VQw$jsUCJMDsE_fVY7Jk$CG3$YgY$$K0lJQ8IvwUHw884wx7QAge37zBqDkWDetgy4eXUc3PtpTuZO1yTAqNPu_Jo_2O15iu0tjvopurjKNAVwQJBANW47CvX1A6TJnKmifVWAAeZfOT94TOgFZoT92XOy8iZlg9YInwFtJKPwZ2c56qLRpkz3UlkNSOGfZ24394aJIsCQQCm_NWsbF7oQzmrNeGrA2zzeyXq2bYcs30WefECq3dXyZ_n4ZAwhZ8dVC74YLCz9lQEOwFkFwX2Pi5YbmZUXlHZAkA7HSX9znmx$lOFHLlF6Z6ie3rHH829aMbw0hr4xuUovAp8fgUzxqQ2cZq9DJwrWNCTKXxIg3YLOHTgPjV1ikeXAkB5_238MpCED9kqBIuCbVZD7Eyfb79k6J0StrQlxy_Bq0RwHJNDX9wHiWFnhPyT_fsTvCgf_9ITPQqPANkx$lbBAkBI6qr1UGqikwtz7bWyLXmMYylr7CgjxmKNdMlYIaiCDEcFOH$eojGkOGY3tpG0goQ0Hx9JTt6Y0lNi_2jOMosF";
                String result = this.objectMapper.writeValueAsString(object);
                String encryBody = CryptUtils.encryptBASE64(CryptUtils.encryptByPrivateKey(result.getBytes(),
                        CryptUtils.decryptBASE64(privateKey)));
                Map<String, String> resultMap = new HashMap<String, String>();
                resultMap.put("result", encryBody);
                this.objectMapper.writeValue(jsonGenerator, resultMap);
            } else {
                this.objectMapper.writeValue(jsonGenerator, object);
            }
        } catch (JsonProcessingException var6) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + var6.getMessage(), var6);
        } catch (com.yzqc.support.security.SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setJsonPrefix(String jsonPrefix) {
        this.jsonPrefix = jsonPrefix;
    }
}
