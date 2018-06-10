package com.bzh.activiti.controller;



import com.bzh.activiti.service.ActModelService;

import ind.syu.restful.InvokeEntity;
import org.activiti.engine.repository.Model;
import org.activiti.rest.service.api.repository.ModelResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ActModelController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${selfProperties.activitiIp}")
    String activitiIp;

//    @Autowired
//    InvokeEntity createModels;

    @Autowired
    ActModelService actModelService;

    @RequestMapping(value = "/createModel",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> createModel(String name, String key, String description, String category){
        Map<String,Object> result=new HashMap<>();
        try {
            Model modelData = actModelService.create(name, key, description, category);
            result.put("modelId",modelData.getId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("创建模型失败：", e);
        }
        return result;

    }

    @RequestMapping(value = "/modelPage")
    public String modelPage(){
        return "modeler";
    }

    /**
     * 根据Model部署流程
     * @throws IOException
     */
    @RequestMapping(value = "deploy")
    @ResponseBody
    public Map<String,String> deploy(String id) throws IOException {
        String message = actModelService.deploy(id);
        Map<String,String> map=new HashMap<>();
        map.put("msg",message);
        return map;

    }

    /**
     * 导出model的xml文件
     */
    @RequestMapping(value = "export")
    public void export(String id, HttpServletResponse response) {
        actModelService.export(id, response);
    }


}
