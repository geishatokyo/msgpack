package org.msgpack.template;

import org.msgpack.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Template for lazy binding.This template is used for class which have self class field like below
 * {{{
 * class A{
 *     A a;
 *     A[] aArray;
 * }
 * }}}
 * User: takeshita
 * Create: 11/09/29 1:58
 */
public class LazyBindTemplate implements Template {

    static ConcurrentHashMap<Class<?>,LazyBindTemplate> map = new ConcurrentHashMap<Class<?>,LazyBindTemplate>();

    public static LazyBindTemplate getLazyBindTemplate(Type clazz){
        if(clazz instanceof Class<?>){
            return getLazyBindTemplate((Class<?>)clazz);
        }else{
            return null;
        }
    }

    public static LazyBindTemplate getLazyBindTemplate(Class<?> clazz){

        LazyBindTemplate t = map.get(clazz);
        if(t == null){
            t = new LazyBindTemplate(clazz);
            map.put(clazz,t);
        }

        return t;
    }


    Class<?> selfClass;
    public LazyBindTemplate(Class<?> selfClass){
      this.selfClass = selfClass;
    }

    Template selfTemplate;

    /**
     * Get template
     * @return
     */
    public Template getTemplate(){
        if(selfTemplate == null){
            selfTemplate = TemplateRegistry.lookup(selfClass);
        }
        return selfTemplate;
    }

    public Object convert(MessagePackObject from, Object to) throws MessageTypeException {
        if(from.isNil()) return null;
        else return getTemplate().convert(from,to);
    }

    public void pack(Packer pk, Object target) throws IOException {
        if(target == null) pk.packNil();
        else getTemplate().pack(pk,target);
    }

    public Object unpack(Unpacker pac, Object to) throws IOException, MessageTypeException {
        if(pac.tryUnpackNull()) return null;
        else return getTemplate().unpack(pac,to);
    }
}
