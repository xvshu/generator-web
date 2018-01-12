package com.eloancn.framework.ump;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Administrator on 2016/5/3.
 */
public class ExceptionUtils {
    public static String getMsg(Throwable error) {
        PrintWriter out = null;
        try {
            StringWriter sw = new StringWriter();
            out = new PrintWriter(sw);
            error.printStackTrace(out);
            out.flush();
            out.close();
            return "\r\n" + sw.toString() + "\r\n";
        } catch (Exception e) {
            return "bad getErrorMap";
        }
    }

}
