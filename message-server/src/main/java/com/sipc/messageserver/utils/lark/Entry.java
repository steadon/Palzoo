package com.sipc.messageserver.utils.lark;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 15:26
 */
@Data
public class Entry {

    private String msg_type;

    private Card card;

    @Data
    public static class Card {
        private List<Element> elements;
        private Header header;

        @Data
        public static class Element {
            private String tag;
            private String content;
            private Img img;
            private String mode;
            private boolean preview;
            private String src;

            @Data
            public static class Img {
                private String img_key;
                private Title alt;
            }
        }

        @Data
        public static class Header {
            private String template;
            private Title title;
        }
    }

    @Data
    public static class Title {
        private String content;
        private String tag;
        private String template;
    }

    public Entry(String title, String url, String content) {

        //设置card
        Card card = new Card();
        //设置header
        Card.Header header = new Card.Header();
        Title headerTitle = new Title();
        headerTitle.setTag("plain_text");
        headerTitle.setTag(title + "请求异常");

        //组装header
        header.setTemplate("wathet");
        header.setTitle(headerTitle);

        //组装elements
        List<Card.Element> elements = new ArrayList<>();

        Card.Element element1 = new Card.Element();
        element1.setTag("markdown");
        element1.setContent("<at id=all></at>\n" +"请求路径：**" + url + "**");
        elements.add(element1);

        Card.Element element2 = new Card.Element();
        element2.setTag("hr");
        elements.add(element2);

        Card.Element element3 = new Card.Element();
        element3.setTag("markdown");
        element3.setContent(content);

        card.setElements(elements);

        this.msg_type = "interactive";
        this.card = card;

    }

}




