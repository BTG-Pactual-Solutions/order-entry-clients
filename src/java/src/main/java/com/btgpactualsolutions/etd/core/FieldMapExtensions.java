package com.btgpactualsolutions.etd.core;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import quickfix.FieldMap;

public class FieldMapExtensions {
	/**
	 * 
	 * @param <T>
	 * @param msg
	 * @param tag
	 * @param typeT
	 * @return
	 */
	public static <T> T tryGetValue(FieldMap msg, int tag, Class<T> typeT) {
        try {
            if (typeT.equals(String.class)) {
                return typeT.cast(msg.getString(tag));
            } else if (typeT.equals(Integer.class)) {
                return typeT.cast(msg.getInt(tag));
            } else if (typeT.equals(BigDecimal.class)) {
                return typeT.cast(msg.getDecimal(tag));
            } else if (typeT.equals(Double.class)) {
                return typeT.cast(msg.getDouble(tag));
            } else if (typeT.equals(Character.class)) {
                return typeT.cast(msg.getChar(tag));
            } else if (typeT.equals(Boolean.class)) {
                return typeT.cast(msg.getBoolean(tag));
            } else if (typeT.equals(LocalDateTime.class)) {
                return typeT.cast(msg.getUtcTimeStamp(tag));
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
