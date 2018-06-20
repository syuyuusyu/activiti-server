package com.bzh.activiti.controller;

import com.bzh.activiti.service.ActProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 流程定义相关Controller
 * @author liyz
 * @version 2017-08-04
 */
@Controller
@RequestMapping(value = "/process")
public class ActProcessController {
	@Autowired
	private ActProcessService actProcessService;


	/**
	 * 读取资源，通过部署ID
	 * @param procDefId
	 * @param proInsId
	 * @param resType
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "resource/read")
	public void resourceRead(String procDefId, String proInsId, String resType, HttpServletResponse response) throws Exception {
		InputStream resourceAsStream = actProcessService.resourceRead(procDefId, proInsId, resType);
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

	
	/**
	 * 部署流程 - 保存
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/deploy", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> deploy(
			String category, MultipartFile file) throws IOException {
		Map<String,String> map=new HashMap<>();
		String fileName = file.getOriginalFilename();
		
		String message="";
		
		if (StringUtils.isEmpty(fileName)){
			message="请选择要部署的流程文件";
		}else{
			message = actProcessService.deploy(category, file);
		}
		map.put("msg",message);
		return map;
		
	}
	
	/**
	 * 设置流程分类
	 * @throws IOException 
	 */
	@RequestMapping(value = "updateCategory")
	public void updateCategory(String procDefId, String category, RedirectAttributes redirectAttributes,HttpServletRequest request
			,HttpServletResponse response) throws IOException {
		actProcessService.updateCategory(procDefId, category);
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write("<script> location.href='../process';</script>");
		
	}

	/**
	 * 挂起、激活流程实例
	 * @throws IOException 
	 */
	@RequestMapping(value = "update/{state}")
	@ResponseBody
	public Map<String,String> updateState(@PathVariable("state") String state, String procDefId, RedirectAttributes redirectAttributes,HttpServletRequest request
			,HttpServletResponse response) throws IOException {
		String message = actProcessService.updateState(state, procDefId);
		Map<String,String> map=new HashMap<>();

		map.put("msg",message);
		return map;
		
	}
	
	/**
	 * 将部署的流程转换为模型
	 * @param procDefId
	 * @param redirectAttributes
	 * @return
	 * @throws XMLStreamException
	 * @throws IOException 
	 */
	@RequestMapping(value = "convert/toModel")
	@ResponseBody
	public Map<String,String> convertToModel(String procDefId) throws XMLStreamException, IOException {
		org.activiti.engine.repository.Model modelData = actProcessService.convertToModel(procDefId);
		String msg= "转换模型成功，模型ID="+modelData.getId();
		Map<String,String> map=new HashMap<>();
		map.put("msg",msg);
		return map;

		
	}
	
	/**
	 * 导出图片文件到硬盘
	 */
	@RequestMapping(value = "export/diagrams")
	@ResponseBody
	public List<String> exportDiagrams(@Value("") String exportDir) throws IOException {
		if(null==exportDir||"".equals(exportDir)){
			exportDir="";//actProperties.getDiagramExportPath();
		}
		List<String> files = actProcessService.exportDiagrams(exportDir);
        return files;
	}

	/**
	 * 删除部署的流程，级联删除流程实例
	 * @param deploymentId 流程部署ID
	 * @throws IOException 
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String,Object> delete(String deploymentId,HttpServletRequest request
			,HttpServletResponse response) throws IOException {
		Map<String,Object> map=new HashMap<>();
		try {
			actProcessService.deleteDeployment(deploymentId);
			map.put("msg","删除成功");
			map.put("success",true);
		}catch (Exception e){
			e.printStackTrace();
			map.put("msg","删除失败");
			map.put("success",false);
		}
		return map;

		
	}
	
	/**
	 * 删除流程实例
	 * @param procInsId 流程实例ID
	 * @param reason 删除原因
	 * @throws IOException 
	 */
	@RequestMapping(value = "deleteProcIns")
	public void deleteProcIns(String procInsId, String reason) throws IOException {

		actProcessService.deleteProcIns(procInsId, reason);

	}

}
