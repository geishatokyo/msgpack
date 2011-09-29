package org.msgpack;

import org.junit.BeforeClass;
import org.msgpack.annotation.MessagePackMessage;
import org.junit.Test;
import org.junit.Assert;
import org.msgpack.annotation.Nullable;
import org.msgpack.template.builder.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Test for class which have self type field.
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

        /**
         * Must be nullable
         */
        @Nullable
        public SelfRefClass child = null;

        public List<SelfRefClass> child2 = new ArrayList<SelfRefClass>();

        public SelfRefClass[] child3 = new SelfRefClass[2];

        public Map<String,SelfRefClass> map = new HashMap<String,SelfRefClass>();

        public String name = "";

    }

    @Test
    public void testPackAndUnpackSelfRefClass(){

        SelfRefClass root = new SelfRefClass("kamio haruko");
        root.child = new SelfRefClass("kamio misuzu");
        root.child2.add(new SelfRefClass("furukawa nagisa"));
        root.child3[0] = new SelfRefClass("natsume rin");
        root.map.put("rewrite",new SelfRefClass("kanbe kotori"));

        byte[] packed = MessagePack.pack(root);

        SelfRefClass unpacked = MessagePack.unpack(packed,SelfRefClass.class);

        Assert.assertEquals(root.name,unpacked.name);
        Assert.assertNotNull(unpacked.child);
        Assert.assertEquals(root.child.name,unpacked.child.name);
        Assert.assertEquals(1,unpacked.child2.size());
        Assert.assertEquals(root.child2.get(0).name,unpacked.child2.get(0).name);
        Assert.assertEquals(2,unpacked.child3.length);
        Assert.assertEquals(root.child3[0].name,unpacked.child3[0].name);
        Assert.assertNull(unpacked.child3[1]);
        Assert.assertEquals(1,unpacked.map.size());
        Assert.assertEquals(root.map.get("rewrite").name , unpacked.map.get("rewrite").name);

    }

}
