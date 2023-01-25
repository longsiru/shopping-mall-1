package com.myshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//업로드한 파일을 읽어롤 경로를 설정
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	@Value("${uploadPath}")  //프로퍼트의 값을 읽어온다.
	String uploadPath;

	//web 브라우저에 입력하는 url이  /images로 사작하는 경우 uploadPath에 설정
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		System.out.println("addResourceHandlers - uploadPath - " + uploadPath);
		registry.addResourceHandler("/images/**")
		.addResourceLocations(uploadPath);
	}
	
	
}
