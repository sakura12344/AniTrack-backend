package com.anitrack.anitrack.service;

import com.anitrack.anitrack.entity.AdultFilter;
import com.anitrack.anitrack.entity.User;
import com.anitrack.anitrack.entity.UserRole;
import com.anitrack.anitrack.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //13.根据id获取用户
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("该用户不存在"));
    }

    //1.后台查询有多少用户
    public long countUsers() {
        return userRepository.count();
    }

    //2.查询用户名是否存在
    public boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    //3.查询邮箱是否已经注册
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    //6.更改用户名
    public User updateUsername(Long id, String newUsername) {
        if (isUsernameExists(newUsername)) {
            throw new IllegalArgumentException("用户名已被占用");
        }

        User user = getUserById(id);
        user.setUsername(newUsername);
        return userRepository.save(user);
    }

    //5.删除用户
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    //8.个性化设置：过滤成人内容
    public User updateAdultFilter(Long id, AdultFilter adultFilter) {
        User user = getUserById(id);
        user.setAdultFilter(adultFilter);
        return userRepository.save(user);
    }

    //9.个性化设置：过滤非官方内容
    public User updateExcludeUnlicensed(Long id, Boolean excludeUnlicensed) {
        User user = getUserById(id);
        user.setExcludeUnlicensed(excludeUnlicensed);
        return userRepository.save(user);
    }

    //10.按角色筛选
    public List<User> getUsersByRole(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("角色不能为空");
        }
        return userRepository.findByRole(role);
    }

    //用户名糊搜索用户
    public List<User> searchByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("搜索关键词不能为空");
        }
        return userRepository.findByUsernameContaining(username);
    }

    //11.邮箱模糊搜索用户
    public List<User> searchByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("搜索关键词不能为空");
        }
        return userRepository.findByEmailContaining(email);
    }

    //11.用户名 邮箱模糊搜索用户
    public List<User> searchByKeyword(String username, String email) {
        if (username == null || email == null || username.trim().isEmpty() || email.trim().isEmpty()) {
            throw new IllegalArgumentException("搜索关键词不能为空");
        }
        return userRepository.searchByUsernameAndEmail(username, email);
    }

    //7.更改用户头像
    public User updateAvatar(Long id, String avatar) {
        User user = getUserById(id);
        user.setAvatarUrl(avatar);
        return userRepository.save(user);
    }

    //4.增加用户（检查邮箱是否存在 用户是否存在 密码加密 密码校验 ）

}


//12.密码修改
