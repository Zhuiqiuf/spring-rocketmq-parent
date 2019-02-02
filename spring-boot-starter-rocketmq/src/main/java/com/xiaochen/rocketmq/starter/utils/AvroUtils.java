package com.xiaochen.rocketmq.starter.utils;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AvroUtils {
    public static ByteArrayOutputStream genProdMsg(String json){

        Schema schema = new Schema.Parser().parse(json);
        GenericRecord record = new GenericData.Record(schema);
        record.put("col1", 6);
        record.put("col2", 7L);
        DatumWriter<GenericRecord> writer = new GenericDatumWriter<>(schema);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        try {
            writer.write(record, encoder);//将record写到out中
            encoder.flush();//结束本次序列化操作
        } catch (IOException e) {
        }
        return out;
    }
}
