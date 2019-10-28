package cn.luxu.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @description:
 * @author: luxu
 * @date: 2019-10-28 17:27
 **/
@SpringBootApplication
@MapperScan("cn.luxu.bootstrap.mapper")
public class OrganizationApplication {
  public static void main(String[] args) {
      SpringApplication.run(OrganizationApplication.class,args);
  }
}
