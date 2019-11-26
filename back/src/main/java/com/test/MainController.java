package com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/demo")
public class MainController {
    @Autowired
    private MainService mainService;

    @GetMapping(path = "/getAll")
    public @ResponseBody Iterable<User> getAllUsers()
    {
        return mainService.getAllUsers();
    }

    @PostMapping(path = "/get")
    public @ResponseBody List<User> findByName(String name)
    {
        return mainService.findByName(name);
    }

    @PostMapping(path = "/add")
    public @ResponseBody boolean add(@RequestParam String name)
    {
        return mainService.add(name);
    }

    @PostMapping(path = "/modify")
    public @ResponseBody boolean modify(@RequestParam Integer id,@RequestParam String name)
    {
        return mainService.modify(id,name);
    }

    @PostMapping(path = "/delete")
    public @ResponseBody boolean deleteByName(@RequestParam String name)
    {
        return mainService.deleteByName(name);
    }
}
