package org.tieland.pea.core;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhouxiang
 * @date 2020/5/20 9:25
 */
@Slf4j
public class DefaultTieMessageConverter implements TieMessageConverter {

    private final ObjectMapper objectMapper;

    public DefaultTieMessageConverter(){
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public DefaultTieMessageConverter(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public TieMessage fromContext(TieMessageContext context) throws ConverterException {
        try{
            if(StringUtils.isEmpty(context.getBody())){
                throw new ConverterException(" MessageContext body must not empty ");
            }

            if(context.getClazz() == null){
                throw new ConverterException(" MessageContext clazz must not null ");
            }

            Object payload = objectMapper.readValue(context.getBody(), context.getClazz());
            String id = context.getMessageId();
            return DefaultTieMessage.builder().payload(payload).build();
        }catch (Exception ex){
            throw new ConverterException(ex);
        }
    }

    @Override
    public void writeContext(TieMessage message, TieMessageContext context) throws ConverterException {

        if(message == null){
            throw new ConverterException(" message must not null ");
        }

        try {

            //FIXME
            context.setMessageId(message.getId());
            String body = JSONObject.toJSONString(message.getPayload());
            context.setBody(body);
        }catch (Exception ex){
            throw new ConverterException(ex);
        }

    }
}
