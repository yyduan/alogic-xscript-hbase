/**
 * 
 */
package com.alogic.xscript.hbase.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author heweiguo
 *
 */
public class FColumnUtil {

    /**
     * 通过参数col(列族：列名)拆解分离2个byte[]
     * 
     * @return byte[0]=列族,byte[1]=列名
     */
    public static byte[][] getFamilyAndColumnBytes(String col) {
        if (StringUtils.isNotEmpty(col)) {
            byte[] family = null;
            byte[] column = null;
            if (col.indexOf(":") > -1) {
                String[] vars = col.split(":");
                family = Bytes.toBytes(vars[0]);
                column = Bytes.toBytes(vars[1]);
            } else {
                family = Bytes.toBytes(col);
            }
            return new byte[][] { family, column };
        }
        return null;
    }

}
