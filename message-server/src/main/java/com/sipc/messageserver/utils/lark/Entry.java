package com.sipc.messageserver.utils.lark;

import lombok.Data;

import java.util.List;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 15:26
 */
@Data
public class Entry {

    private String msg_type;

    private Content card;

    public static class Content {
        private List<Element> elements;
        private Header header;

        public static class Element {
            private String tag;
            private String content;
            private Img img;
            private String mode;
            private boolean preview;
            private String src;

            public static class Img {
                private String img_key;
                private PlainText alt;
            }
        }

        public static class Header {
            private String template;
            private PlainText title;
        }
    }

    public static class PlainText {
        private String content;
        private String tag;
    }
}




