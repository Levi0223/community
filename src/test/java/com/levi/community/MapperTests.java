package com.levi.community;

import com.levi.community.dao.DiscussPostMapper;
import com.levi.community.dao.LoginTicketMapper;
import com.levi.community.dao.MessageMapper;
import com.levi.community.dao.UserMapper;
import com.levi.community.entity.DiscussPost;
import com.levi.community.entity.LoginTicket;
import com.levi.community.entity.Message;
import com.levi.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class MapperTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    void testSelectUserById() {
        User user = userMapper.selectById(101);
        System.out.println(user);
        User liubei = userMapper.selectByName("liubei");
        System.out.println(liubei);
        User user1 = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user1);
    }

    @Test
    void testInsertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@gmail.com");
        user.setHeaderUrl("https://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
    }

    @Test
    void testUpdateUser() {
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);
        userMapper.updateHeader(150, "https://www.nowcoder.com/102.png");
        userMapper.updatePassword(150, "hello");
    }

    @Test
    void testSelectPosts() {
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for (DiscussPost post : discussPosts) {
            System.out.println(post);
        }
        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }

    @Test
    void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("qwe");
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        loginTicket.setStatus(0);
        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    void testSelectAndUpdateLoginTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("qwe");
        System.out.println(loginTicket);
        loginTicketMapper.updateStatus("qwe", 1);
        System.out.println(loginTicketMapper.selectByTicket("qwe"));
    }

    @Test
    void testSelectLetters() {
        List<Message> list = messageMapper.selectConversations(111, 0, 20);
        for (Message message : list) {
            System.out.println(message);
        }
        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);
        list = messageMapper.selectLetters("111_112", 0, 10);
        for (Message message : list) {
            System.out.println(message);
        }
        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);
        count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count);
    }
}
