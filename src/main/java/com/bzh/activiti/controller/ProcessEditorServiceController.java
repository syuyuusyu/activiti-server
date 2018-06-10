package com.bzh.activiti.controller;

import com.bzh.activiti.service.ActModelService;
import com.bzh.activiti.util.ModelDataConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * liyz 创建于 2017/8/2.
 */
@Controller
@RequestMapping("/service")
public class ProcessEditorServiceController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActModelService actModelService;




    protected ObjectMapper objectMapper = new ObjectMapper();

    @ResponseBody
    @RequestMapping(value = "/model/{modelId}/json", method = RequestMethod.GET, produces = "application/json")
    public ObjectNode getEditorJson(@PathVariable String modelId) {
        ObjectNode modelNode = null;

        Model model = repositoryService.getModel(modelId);

        if (model != null) {
            try {
                if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                    modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
                } else {
                    modelNode = objectMapper.createObjectNode();
                    modelNode.put(ModelDataConstants.MODEL_NAME, model.getName());
                }
                modelNode.put(ModelDataConstants.MODEL_ID, model.getId());
                ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(new String(repositoryService.getModelEditorSource(model.getId()),
                        "utf-8"));
                modelNode.put("model", editorJsonNode);

            } catch (Exception e) {
                logger.error("Error creating model JSON", e);
                throw new ActivitiException("Error creating model JSON", e);
            }
        }
        return modelNode;

    }
    @RequestMapping(value = "/model/{modelId}/save", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void saveModel(
            @PathVariable String modelId
            ,@RequestParam String name
            ,@RequestParam String description
            ,@RequestParam String json_xml
            ,@RequestParam String svg_xml) {
        try {

            Model model = repositoryService.getModel(modelId);

            if(StringUtils.isNotEmpty(model.getMetaInfo())){
                ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());

                modelJson.put(ModelDataConstants.MODEL_NAME, name);
                modelJson.put(ModelDataConstants.MODEL_DESCRIPTION, description);
                model.setMetaInfo(modelJson.toString());

            }
            model.setName(name);

            repositoryService.saveModel(model);

            repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes("utf-8"));

            InputStream svgStream = new ByteArrayInputStream(svg_xml.getBytes("utf-8"));
            TranscoderInput input = new TranscoderInput(svgStream);

            PNGTranscoder transcoder = new PNGTranscoder();
            // Setup output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);

            // Do the transformation
            transcoder.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();

        } catch (Exception e) {
            logger.error("Error saving model", e);
            throw new ActivitiException("Error saving model", e);
        }
    }
    @ResponseBody
    @RequestMapping(value = "/editor/stencilset", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public  String getStencilset() {
        InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("templates/stencilset.json");
        try {
            return IOUtils.toString(stencilsetStream, "utf-8");
        } catch (Exception e) {
            throw new ActivitiException("Error while loading stencil set", e);
        }
    }


}

