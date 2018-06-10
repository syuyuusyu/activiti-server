package com.bzh.activiti.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.stream.Collectors;


@RestController
public class UserSynController {

    @Autowired
    IdentityService identityService;

    @RequestMapping(value="/act/userSyn",method = RequestMethod.POST)
    @Transactional
    public Map<String,Object> usersyn(@RequestBody SynEntity entity){
        System.out.println("entity = " + entity);
        Map<String,Object> map=new HashMap<>();
        //删除现有用户角色
        identityService.createGroupQuery().list().forEach(g->{
            identityService.createUserQuery().memberOfGroup(g.getId()).list().forEach(u->{
                identityService.deleteMembership(u.getId(),g.getId());
            });
        });

        identityService.createUserQuery().list().forEach(u->{
            identityService.createGroupQuery().groupMember(u.getId()).list().forEach(g->{
                identityService.deleteMembership(u.getId(),g.getId());
            });
        });

        identityService.createUserQuery().list().stream().map(u->u.getId()).forEach(identityService::deleteUser);
        identityService.createGroupQuery().list().stream().map(g->g.getId()).forEach(identityService::deleteGroup);


        try {
            entity.getUsernames().stream().map(identityService::newUser).forEach(identityService::saveUser);
            entity.getGroupnames().stream().map(identityService::newGroup).forEach(identityService::saveGroup);
            entity.getUserRole().stream().collect(Collectors.toSet()).stream()
                    .map(s->new String[]{s.split(",")[0],s.split(",")[1]})
                    .forEach(n->identityService.createMembership(n[0],n[1]));
            map.put("success",true);
        }catch (Exception e){
            System.out.println("e.toString() = " + e.toString());
            map.put("success",false);
        }
        return map;
    }




}

class SynEntity{
    private List<String> usernames;
    private List<String> groupnames;
    private List<String> userRole;


    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public List<String> getGroupnames() {
        return groupnames;
    }

    public void setGroupnames(List<String> groupnames) {
        this.groupnames = groupnames;
    }

    public List<String> getUserRole() {
        return userRole;
    }

    public void setUserRole(List<String> userRole) {
        this.userRole = userRole;
    }
}



