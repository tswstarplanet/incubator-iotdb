/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.iotdb.db.utils;

import org.apache.iotdb.db.conf.IoTDBDescriptor;
import org.apache.iotdb.db.qp.constant.SQLConstant;
import org.apache.iotdb.tsfile.file.metadata.enums.TSDataType;
import org.apache.iotdb.tsfile.utils.Binary;

public class TypeInferenceUtils {

  private static TSDataType booleanStringInferType = IoTDBDescriptor.getInstance().getConfig().getBooleanStringInferType();

  private static TSDataType integerStringInferType = IoTDBDescriptor.getInstance().getConfig().getIntegerStringInferType();

  private static TSDataType floatingStringInferType = IoTDBDescriptor.getInstance().getConfig().getFloatingStringInferType();

  private TypeInferenceUtils() {

  }

  static boolean isNumber(String s) {
    try {
      Double.parseDouble(s);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  private static boolean isBoolean(String s) {
    return s.equalsIgnoreCase(SQLConstant.BOOLEN_TRUE) || s
        .equalsIgnoreCase(SQLConstant.BOOLEN_FALSE);
  }

  /**
   * Get predicted DataType of the given value
   */
  public static TSDataType getPredictedDataType(Object value) {

    if (value instanceof String) {
      String strValue = (String) value;
      if (isBoolean(strValue)) {
        return booleanStringInferType;
      } else if (isNumber(strValue)){
        if (!strValue.contains(".")) {
          return integerStringInferType;
        } else {
          return floatingStringInferType;
        }
      } else {
        return TSDataType.TEXT;
      }
    } else if (value instanceof Boolean) {
      return TSDataType.BOOLEAN;
    } else if (value instanceof Integer) {
      return TSDataType.INT32;
    } else if (value instanceof Long) {
      return TSDataType.INT64;
    } else if (value instanceof Float) {
      return TSDataType.FLOAT;
    } else if (value instanceof Double) {
      return TSDataType.DOUBLE;
    } else if (value instanceof Binary) {
      return TSDataType.TEXT;
    }

    return TSDataType.TEXT;
  }
}
