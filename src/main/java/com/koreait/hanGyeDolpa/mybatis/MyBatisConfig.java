package com.koreait.hanGyeDolpa.mybatis;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

@Configuration  // Config(설정) 관련 클래스
@RequiredArgsConstructor
@MapperScan("com.koreait.hanGyeDolpa.mapper")  // Mapper를 스캔할 패키지
public class MyBatisConfig {
   
   // 커넥션 풀 및 MyBatis에 필요한 요소들을 메모리에 할당하고 관리
   // XML과 java 연동에 필요한 경로 관리
   private final ApplicationContext applicationContext;
   
   // 1. Property 가져오기
   // @Bean
   // @Configuration 또는 @Component가 작성된 클래스의 메서드에 사용
   // 메서드의 리턴 객체를 Spring 컨테이너에 등록
   @Bean
   @ConfigurationProperties(prefix="spring.datasource")
   public HikariConfig hikariConfig() { return new HikariConfig(); }
   
   // 2. DataSource 설정
   @Bean
   public DataSource dataSource() {
      HikariDataSource hds = new HikariDataSource(hikariConfig());
      return hds;
   }
   
   // 3. SQL Session Factory
   @Bean
   public SqlSessionFactory sqlSessionFactory() throws IOException {

      SqlSessionFactoryBean sfb = new SqlSessionFactoryBean();
      sfb.setDataSource(dataSource());
      
      // SQL을 작성할 xml 경로 설정
      sfb.setMapperLocations(applicationContext.getResources("classpath*:/mapper/*.xml"));
      sfb.setConfigLocation(applicationContext.getResource("classpath:/config/config.xml"));
      
      try {
         SqlSessionFactory factory = sfb.getObject();
         // DB는 대소문자 구분이 없으므로 컬럼명을 _로 연결하여 만든다.
         // _로 연결된 변수를 CamelCase로 바꾸는 설정을 true로 입력
         // open_date(DB) -> openDate(Java)
         factory.getConfiguration().setMapUnderscoreToCamelCase(true);
         return factory;
      }
      catch(Exception e) { e.printStackTrace(); }
      
      return null;
   }
   
}   