package io.github.donmahallem.dota2parser;

import okio.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ToJsonFileParser extends FileParser {
    private final BufferedSink mOutput;
    private Map<String,Boolean> mFirstItemInBlock=new HashMap<>();

    public ToJsonFileParser(String inputFile, BufferedSink output) throws FileNotFoundException {
        super(inputFile);
        this.mOutput=output;
    }

    public ToJsonFileParser(File inputFile, BufferedSink output) throws FileNotFoundException {
        super(inputFile);
        this.mOutput=output;
    }

    public ToJsonFileParser(Source source, BufferedSink output) {
        super(source);
        this.mOutput=output;
    }

    @Override
    void parseAttribute(List<String> path, String tag, String value) {
        Buffer buffer=new Buffer();
        final String blockKey=createKeyFromPath(path);
        if(this.hasFirstItemWritten(blockKey)){
            buffer.write(ByteString.encodeUtf8(","));
        }else{
            this.setHasFirstItemWritten(blockKey,true);
        }
        buffer.write(ByteString.encodeUtf8("\""+tag+"\":\""+value+"\""));
        try {
            this.mOutput.write(buffer,buffer.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onBefore(){
        try {
            this.mOutput.writeUtf8("{");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAfter(){
        try {
            this.mOutput.writeUtf8("}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearKeysWithPrefix(String key){
        //System.out.println("Clear: "+key);
        for(String currentKey:this.mFirstItemInBlock.keySet()){
            if(currentKey.length()<key.length()){
                continue;
            }
            if(currentKey.startsWith(key)){
                this.mFirstItemInBlock.put(currentKey,false);
            }
        }
    }
    public String createKeyFromPath(List<String> path){
        return String.join("\\\\",path);
    }

    public void setHasFirstItemWritten(String key,boolean value){
        this.mFirstItemInBlock.put(key,value);
    }

    public boolean hasFirstItemWritten(String key){
        return this.mFirstItemInBlock.getOrDefault(key,false);
    }

    @Override
    void startBlock(List<String> path, String name) {
        Buffer buffer=new Buffer();
        final String blockKey=createKeyFromPath(path);
        if(this.hasFirstItemWritten(blockKey)){
            buffer.write(ByteString.encodeUtf8(","));
        }else{
            this.setHasFirstItemWritten(blockKey,true);
        }
        buffer.write(ByteString.encodeUtf8("\""+name+"\""+":{"));
        try {
            this.mOutput.write(buffer,buffer.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void endBlock(List<String> path, String name) {
        Buffer buffer=new Buffer();
        buffer.write(ByteString.encodeUtf8("}"));
        List<String> copyPath= new ArrayList<String>(path);
        copyPath.add(name);
        this.clearKeysWithPrefix(createKeyFromPath(copyPath));
        try {
            this.mOutput.write(buffer,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
