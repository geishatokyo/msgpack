package org.msgpack;

import org.junit.BeforeClass;
import org.msgpack.annotation.MessagePackMessage;
import org.junit.Test;
import org.junit.Assert;
import org.msgpack.annotation.Nullable;
import org.msgpack.template.builder.*;

/**
 * User: takeshita
 * Create: 11/09/29 1:20
 */
public class TestSelfRefClass {

    /*@BeforeClass
    public static void beforeClass(){
        BuilderSelectorRegistry.reset();
    }*/

    @MessagePackMessage
    public static class SelfRefClass{


        public SelfRefClass(){}
        public SelfRefClass(String name){ this.name  = name;}

        @Nullable
        public SelfRefClass child = null;

        public String name = "";

    }

    @Test
    public void testPackAndUnpackSelfRefClass(){

        SelfRefClass root = new SelfRefClass("kamio haruko");
        root.child = new SelfRefClass("kamio misuzu");

        byte[] packed = MessagePack.pack(root);

        SelfRefClass unpacked = MessagePack.unpack(packed,SelfRefClass.class);

        Assert.assertEquals(root.name,unpacked.name);
        Assert.assertNotNull(unpacked.child);
        Assert.assertEquals(root.child.name,unpacked.child.name);

    }

}
