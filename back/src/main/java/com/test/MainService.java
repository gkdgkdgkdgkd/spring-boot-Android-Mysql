package com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class MainService {
    @Autowired
    private UserRepository userRepository;

    public Iterable<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    public List<User> findByName(String name)
    {
        return userRepository.findByName(name);
    }

    public boolean add(String name)
    {
        User user = new User();
        user.setName(name);
        userRepository.save(user);
        return true;
    }

    public boolean modify(Integer id,String name)
    {
        User user = new User();
        user.setName(name);
        user.setId(id);
        userRepository.save(user);
        return true;
    }

    public boolean deleteByName(String name)
    {
        return userRepository.deleteByName(name) != 0;
    }
}
