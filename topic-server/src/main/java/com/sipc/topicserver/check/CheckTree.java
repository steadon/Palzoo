package com.sipc.topicserver.check;

import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/6 15:43
 */
@AllArgsConstructor
public class CheckTree {

    private RuleNode root; // 根节点

    // 构造函数
    public CheckTree() {
        this.root = new RuleNode();
    }

    // 添加规则
    public void addRule(String rule) {
        String[] keywords = rule.split(" "); // 将规则拆分为关键词
        RuleNode curNode = root; // 从根节点开始

        // 依次添加节点
        for (String keyword : keywords) {
            RuleNode child = null;

            // 查找是否已经有对应关键词的子节点
            for (RuleNode node : curNode.getChildren()) {
                if (node.getKeyword().equals(keyword)) {
                    child = node;
                    break;
                }
            }

            // 如果没有则新建一个子节点
            if (child == null) {
                child = new RuleNode(keyword);
                curNode.addNode(child);
            }

            curNode = child;
        }
    }

    // 审核发言文本
    public List<RuleNode> check(String text) {
        // 从根节点开始匹配
        return root.match(text);
    }

}
