package com.eloancn.framework.ump;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;

/**
 * Created by Administrator on 2016/3/29.
 */
public class Profiler {

    @Deprecated
    public static Transaction registerInfo() {
        Transaction ttn;
        StackTraceElement[] stes = Thread.currentThread().getStackTrace();
        if (null != stes && stes.length >= 4) {
            ttn = Cat.getProducer().newTransaction(stes[3].getClassName(), stes[3].getMethodName());
        } else {
            ttn = Cat.getProducer().newTransaction("NoClassName", "NoMethodName");
        }
        ttn.setStatus(Transaction.SUCCESS);
        return ttn;
    }

    public static Transaction registerInfo(String className, String methodName) {
        Transaction ttn = Cat.getProducer().newTransaction(className, methodName);
        ttn.setStatus(Transaction.SUCCESS);
        return ttn;
    }

    public static Transaction registerInfo(String className, String methodName, String parameters) {
        Transaction ttn = Cat.getProducer().newTransaction(className, methodName);
        ttn.addData("parameters", parameters);
        ttn.setStatus(Transaction.SUCCESS);

        return ttn;
    }

    public static void appendInfo(Transaction ttn, String message) {
        ttn.addData(message);
    }

    public static void functionError(Transaction ttn, String message, Throwable cause) {
        ttn.setStatus(cause);
        Cat.logError(message, cause);
    }

    public static void functionError(Transaction ttn, Throwable cause) {
        ttn.setStatus(cause);
        Cat.logError(cause);
    }

    public static void registerEnd(Transaction ttn) {
        ttn.complete();
    }
    public static void logInfo(String type, String name, String status, String nameValuePairs) {
        Cat.logEvent(type, name, status, nameValuePairs);
    }

    public static void newHeartbeat(String type, String name) {
        Cat.newHeartbeat(type, name);
    }

    public static void logMetricForCount(String metricName){
        Cat.logMetricForCount(metricName);
    }

}
