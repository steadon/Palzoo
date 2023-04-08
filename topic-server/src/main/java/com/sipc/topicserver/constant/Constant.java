package com.sipc.topicserver.constant;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author o3141
 * @since 2023/4/5 16:23
 * @version 1.0
 */
public class Constant {

    //ofPattern("yyyy-MM-dd HH:mm:ss")
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final ZoneOffset zoneOffset = ZoneOffset.ofHours(8);

}
