package org.msgpack.template;

import org.msgpack.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自分のフィールドに自分のClassを保持するクラスをPackするための遅延ロードTemplate
 * User: takeshita
 * Create: 11/09/29 1:58
 */
public class SelfTypeTemplate implements Template {

    static ConcurrentHashMap<Class<?>,SelfTypeTemplate> map = new ConcurrentHashMap<Class<?>,SelfTypeTemplate>();

    public static SelfTypeTemplate getSelfTypeTemplate(Class<?> clazz){

        SelfTypeTemplate t = map.get(clazz);
        if(t == null){
            t = new SelfTypeTemplate(clazz);
            map.put(clazz,t);
        }

        return t;
    }


    Class<?> selfClass;
    public SelfTypeTemplate(Class<?> selfClass){
      this.selfClass = selfClass;
    }

    Template selfTemplate;

    /**
     * 遅延読み込みを行う。
     * @return
     */
    public Template getSelfTemplate(){
        if(selfTemplate == null){
            selfTemplate = TemplateRegistry.lookup(selfClass);
        }
        return selfTemplate;
    }

    public Object convert(MessagePackObject from, Object to) throws MessageTypeException {
        return getSelfTemplate().convert(from,to);
    }

    public void pack(Packer pk, Object target) throws IOException {
        getSelfTemplate().pack(pk,target);
    }

    public Object unpack(Unpacker pac, Object to) throws IOException, MessageTypeException {
        return getSelfTemplate().unpack(pac,to);
    }
}
