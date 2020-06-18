package com.nowcoder.communityy.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词过滤器
 * 步骤：
 * 1.定义前缀树
 * 2.根据敏感词，初始化前缀树
 * 3.编写过滤敏感词的方法
 */
@Component
public class SensitiveFilter {

    /**
     * 前缀树的节点结构
     */
    private class TreeNode {

        // 判断是不是一个敏感词的结尾
        private boolean isKeywordEnd = false;

        // 当前节点的下一个节点(key是下级节点字符，value是下级节点)
        private Map<Character, TreeNode> subNodes = new HashMap<>();

        // 判断是不是到了一个敏感词的结尾处
        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        // 设置一个敏感词的结尾
        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        // 添加子节点，一个节点代表一个字符
        public void addSubNode(Character c, TreeNode node) {
            subNodes.put(c, node);
        }

        // 获取子节点
        public TreeNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }

    // 日志类
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 文本中出现的敏感词替换成的字符***
    private static final String REPLACEMENT = "***";

    // 根节点
    private TreeNode rootNode = new TreeNode();

    // 这里的注解含义：当服务启动的时候，也就是调用这个bean容器之后，就会调用被这个注解标识的方法，自动执行，构造树形结构。
    // 下面try-catch的写法方便之处：在try里面的操作自动执行，并且会在最后自动close，不用我们手动close
    @PostConstruct
    public void init() {
        try (
                // 读取存放敏感词的文件
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 添加到前缀树，一次添加一个敏感词
                this.addKeyword(keyword);
            }
        } catch (IOException e) {

        }
    }

    // 把一个敏感词添加到前缀树
    // 比如当前敏感词是abcc，而前缀树中已经有敏感词abms，则我们不用重新创建节点a和b
    public void addKeyword(String keyword) {
        TreeNode curNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TreeNode subNode = curNode.getSubNode(c);
            if(subNode == null) {
                //初始化子节点
                subNode = new TreeNode();
                curNode.addSubNode(c, subNode);
            }
            // 让当前节点指向子节点
            curNode = subNode;
        }
        // 当这个敏感词读取结束时，就在词尾添加已结束的标识
        curNode.setKeywordEnd(true);
    }

    /**
     * 过滤敏感词。
     * 如果在敏感词中间添加了一些特殊符号，我们可以跳过这些符号，还是可以把这个敏感词给识别出来
     */
    public String filter(String text) {
        // 如果当前文本是空的，直接返回null
        if(StringUtils.isBlank(text)){
            return null;
        }
        // 指针1：在前缀树中遍历移动
        TreeNode tempNode = rootNode;

        // 指针2：在文本串中移动，筛选敏感词时的左边界，即起始位置
        int begin = 0;

        // 指针3：在文本串中移动，筛选敏感词时的右边界
        int position = 0;

        StringBuilder sb = new StringBuilder();

        while(position < text.length()){
            char c = text.charAt(position);// 获得指针3当前指向的字符
            // 跳过特殊符号
            if (isSymbol((c))){
                // 若指针1处于根节点，则将此符号计入结果，让指针2向下走一步
                if(tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                position++;//如果指针3当前指向的字符是特殊符号，继续向后移动
                continue;
            }
            //检查前缀树中当前节点的下级节点，根据字符查找
            tempNode = tempNode.getSubNode(c);
            //如果当前节点的下级节点在前缀树中为null，就说明已经不是敏感词了
            if (tempNode == null) {
                // 以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {// 如果刚好匹配到敏感词且一直到了它的结尾处，则这就是敏感词了
                // 发现敏感词，把begin-position的字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                // 重新指向根节点
                tempNode = rootNode;
            } else { // 还在检测的途中
                // 检查下一个字符
                position++;
            }
        }

        // 当begin还没到终点，但是position已经到了终点，此时发现还不是敏感词
        // 那么，就把begin-position的字符串计入结果即可
        sb.append(text.substring(begin, position));

        return sb.toString();
    }

    // 判断当前字符是否为特殊符号，是的话就返回true
    public boolean isSymbol (Character c){
        // 0x2E80~0x9FFF是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c >  0x9FFF);
    }



}
