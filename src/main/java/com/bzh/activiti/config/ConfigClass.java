package com.bzh.activiti.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration

/**
 * ImportResource引入资源文件有三种方式：
 *     1.直接引入，该路径就是src/resources/下面的文件：file
 *     2.classpath引入：该路径就是src/java下面的配置文件：classpath:file
 *     3.引入本地文件：该路径是一种绝对路径：file:D://....
 */
@ImportResource(locations={"classpath:application-bean.xml"})
public class ConfigClass {

}
