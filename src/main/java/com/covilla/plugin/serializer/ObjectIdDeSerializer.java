package com.covilla.plugin.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.bson.types.ObjectId;

import java.io.IOException;

/**
 * Created by qmaolong on 2017/1/16.
 */
public class ObjectIdDeSerializer extends JsonDeserializer<ObjectId> {

    @Override
    public ObjectId deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        try {
            String text = jp.getText();
            if (text != null&&text != ""){
                return new ObjectId(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}