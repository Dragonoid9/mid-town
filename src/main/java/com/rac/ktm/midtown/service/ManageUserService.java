package com.rac.ktm.midtown.service;

import com.rac.ktm.midtown.entity.User;
import com.rac.ktm.midtown.repository.UserManageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageUserService {

    @Autowired
    UserManageRepository userManageRepository;

    public List<User> findAll(){
        return userManageRepository.findAllNonAdminUsers();
    }

    public void deleteById(int id) {
        userManageRepository.deleteById(id);
    }
}
