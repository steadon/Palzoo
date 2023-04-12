package com.sipc.topicserver.util.ac;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/12 16:23
 */
@Data
public class ACResult implements Serializable {

    @JsonProperty("StatusCode")
    private Integer statusCode;

    @JsonProperty("StatusMsg")
    private String statusMsg;

    @JsonProperty("TitleList")
    private List<String> titleList;

    @JsonProperty("ContentList")
    private List<String> contentList;

}
