package com.sipc.topicserver.check;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/6 15:44
 */
@Data
@AllArgsConstructor
public class RuleNode {

    // 规则节点对应的关键词
    private String keyword;

    //注释信息，这个节点的内容
    private String comment;

    // 子节点列表
    private List<RuleNode> children;

    public RuleNode() {}

    public RuleNode(String keyword) {
        this.keyword = keyword;

        this.comment = "";

        this.children = new ArrayList<>();
    }

    //添加子节点
    public void addNode(RuleNode ruleNode) {
        children.add(ruleNode);
    }

    // 匹配发言文本并返回需要审核的节点列表
    public List<RuleNode> match(String text) {
        List<RuleNode> needCheckNodes = new ArrayList<>();

        // 如果该节点对应的关键词在发言文本中出现了，则将该节点标记为需要审核
        if (text.contains(keyword)) {
            needCheckNodes.add(this);
        }

        // 递归匹配子节点
        for (RuleNode child : children) {
            needCheckNodes.addAll(child.match(text));
        }

        return needCheckNodes;
    }

}
