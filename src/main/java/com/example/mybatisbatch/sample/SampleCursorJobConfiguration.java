package com.example.mybatisbatch.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SampleCursorJobConfiguration {
    private final SqlSessionFactory sqlSessionFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job cursorJob(){
        return jobBuilderFactory.get("SampleCursorJob")
                .start(cursorStep())
                .build();
    }

    @Bean
    public Step cursorStep(){
        return stepBuilderFactory.get("SampleCursorStep")
                .<Order, Order>chunk(1000)
                .reader(cursorReader())
                .writer(cursorWriter())
                .build();
    }

    @Bean
    public MyBatisCursorItemReader<Order> cursorReader() {
        return new MyBatisCursorItemReaderBuilder<Order>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.example.mybatisbatch.sample.SampleMapper.selectOrder")
                .build();
    }

    @Bean
    public ItemWriter<Order> cursorWriter() {
        return list -> {
            for (Order temp : list) {
                log.info("[Cursor]writer : {}", temp);
            }
        };
    }
}
