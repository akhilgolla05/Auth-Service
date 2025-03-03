package org.example.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.example.models.UserInfoDto;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class UserInfoSerializer implements Serializer<UserInfoDto> {


    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
       // Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, UserInfoDto userInfoDto) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            retVal = objectMapper.writeValueAsString(userInfoDto).getBytes();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return retVal;
    }


    @Override
    public void close() {
    }
}
