package com.levi.community.util;

import lombok.Getter;
import lombok.Setter;
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

@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    private static final String REPLACEMENT = "***";
    // root
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt"); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("Failed to load sensitive word file " + e.getMessage());
        }
    }

    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null) {
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            tempNode = subNode;
            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        // point 1
        TrieNode tempNode = rootNode;
        // point 2
        int begin = 0;
        // point 3
        int position = 0;
        // result
        StringBuilder stringBuilder = new StringBuilder();
        while (position < text.length()) {
            char c = text.charAt(position);
            // skip symbol
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    stringBuilder.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                stringBuilder.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                stringBuilder.append(REPLACEMENT);
                begin = ++position;
            } else {
                position++;
            }
        }
        stringBuilder.append(text.substring(begin));
        return stringBuilder.toString();
    }

    private boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    private class TrieNode {
        @Getter
        @Setter
        private boolean isKeywordEnd = false;
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }
}
