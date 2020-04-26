package pl.wsb.massmailingapp;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

  @Bean
  public SendGrid sendGrid(@Value("${api-key}") String apiKey){
    return new SendGrid(apiKey);
  }
}
