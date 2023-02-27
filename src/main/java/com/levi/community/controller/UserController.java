package com.levi.community.controller;

import com.levi.community.annotation.annotation.LoginRequired;
import com.levi.community.entity.User;
import com.levi.community.service.UserService;
import com.levi.community.util.CommunityUtil;
import com.levi.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Component
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "You haven't uploaded yet");
            return "/site/setting";
        }
        String fileName = headerImage.getOriginalFilename();
        assert fileName != null;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "incorrect file format");
            return "/site/setting";
        }
        // generate random filename
        fileName = CommunityUtil.generateUUID() + suffix;
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("Failed to upload file: " + e.getMessage());
            throw new RuntimeException("Failed to upload file", e);
        }

        // update headerUrl
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);
        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        fileName = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("image/" + suffix);
        try (FileInputStream fileInputStream = new FileInputStream(fileName); OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("Failed to read avatar: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /*@RequestMapping(path = "/changePassword", method = RequestMethod.POST)
    public String changePassword(String originalPassword, String newPassword, String confirmPassword, Model model) {
        User user = hostHolder.getUser();
        if (!user.getPassword().equals(CommunityUtil.md5(originalPassword + user.getSalt()))) {
            model.addAttribute("originalMsg", "incorrect original password");
            return "/site/setting";
        }
        if (StringUtils.length(newPassword) < 8) {
            model.addAttribute("newMsg", "The password length cannot be less than 8 characters");
            return "/site/setting";
        }
        if (!StringUtils.equals(newPassword, confirmPassword)) {
            model.addAttribute("confirmMsg", "The two passwords entered are inconsistent");
            return "/site/setting";
        }
        String password = CommunityUtil.md5(newPassword + user.getSalt());
        userService.updatePassword(user.getId(), password);
        return "redirect:/logout";
    }*/

    @RequestMapping(path = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(String oldPassword, String newPassword, Model model) {
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(user.getId(), oldPassword, newPassword);
        if (map == null || map.isEmpty()) {
            return "redirect:/logout";
        } else {
            model.addAttribute("oldPasswordMsg", map.get("oldPasswordMsg"));
            model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
            return "/site/setting";
        }
    }

}
