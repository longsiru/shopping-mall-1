package com.myshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing  //jpa의 auditing기능을 활성화

//auditing特性在实体类的创建人、创建时间、最后更新人，最后更新时间属性上，使用以下注解@CreatedBy、@CreatedDate、@LastModifiedBy、@LastModifiedDate。
//这些注解会在调用数据库前，对修饰的字段进行填充，无需再在业务代码中对这些属性进行手动赋值。
//@CreatedBy、@CreatedDate、@LastModifiedBy、@LastModifiedDate insert时会对字段进行填充
//@LastModifiedBy、@LastModifiedDate update时会对字段进行填充

public class AuditConfig {

	@Bean
	public AuditorAware<String> auditorProvider(){
		return new AuditorAwareImpl();
	}
}
