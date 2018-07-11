package com.xinhuanet.microService;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.xinhuanet.microService.util.MethodUtils;

public class MethodUtilsTest {

    @Test
    public void test1() throws Exception {
        Map<String, Object> mes = new HashMap<String, Object>();
        mes.put("authType", "authEnterprise");
        mes.put("oldState", 0);
        mes.put("newState", 1);

        Integer num = MethodUtils.getMethodNum(mes);
        System.out.println("************** num: " + num);
        assertTrue(num == 1);
    }

    @Test
    public void test2() throws Exception {
        Map<String, Object> mes = new HashMap<String, Object>();
        mes.put("authType", "authPersonalRealname");
        mes.put("oldState", 2);
        mes.put("newState", 11);

        Integer num = MethodUtils.getMethodNum(mes);
        System.out.println("************** num: " + num);
        assertTrue(num == 2);
    }

    @Test
    public void test3() throws Exception {
        Map<String, Object> mes = new HashMap<String, Object>();
        mes.put("authType", "authPersonalIdentity");
        mes.put("oldState", 2);
        mes.put("newState", 12);

        Integer num = MethodUtils.getMethodNum(mes);
        System.out.println("************** num: " + num);
        assertTrue(num == 3);
    }

    @Test
    public void test4() throws Exception {
        Map<String, Object> mes = new HashMap<String, Object>();
        mes.put("authType", "authEnterprise");
        mes.put("oldState", 5);
        mes.put("newState", 1);

        Integer num = MethodUtils.getMethodNum(mes);
        System.out.println("************** num: " + num);
        assertTrue(num == 4);
    }

    @Test
    public void test5() throws Exception {
        Map<String, Object> mes = new HashMap<String, Object>();
        mes.put("authType", "authPersonalRealname");
        mes.put("oldState", 5);
        mes.put("newState", 11);

        Integer num = MethodUtils.getMethodNum(mes);
        System.out.println("************** num: " + num);
        assertTrue(num == 5);
    }

    @Test
    public void test6() throws Exception {
        Map<String, Object> mes = new HashMap<String, Object>();
        mes.put("authType", "authEnterprise");
        mes.put("oldState", 5);
        mes.put("newState", null);

        Integer num = MethodUtils.getMethodNum(mes);
        System.out.println("************** num: " + num);
        assertTrue(num == 6);
    }

    @Test
    public void test7() throws Exception {
        Map<String, Object> mes = new HashMap<String, Object>();
        mes.put("authType", "authPersonalRealname");
        mes.put("oldState", 5);
        mes.put("newState", null);

        Integer num = MethodUtils.getMethodNum(mes);
        System.out.println("************** num: " + num);
        assertTrue(num == 7);
    }

    @Test
    public void test8() throws Exception {
        Map<String, Object> mes = new HashMap<String, Object>();
        mes.put("authType", "authPersonalIdentity");
        mes.put("oldState", 6);
        mes.put("newState", 7);

        Integer num = MethodUtils.getMethodNum(mes);
        System.out.println("************** num: " + num);
        assertTrue(num == 8);
    }
}
