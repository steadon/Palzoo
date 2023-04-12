package com.sipc.topicserver.util.ac;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/12 16:22
 */
@Data
public class ACParam implements Serializable {

    private String title;

    private String content;

}