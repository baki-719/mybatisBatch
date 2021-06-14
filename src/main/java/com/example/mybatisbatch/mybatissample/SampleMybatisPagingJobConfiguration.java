package com.example.mybatisbatch.mybatissample;

import com.example.mybatisbatch.dto.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
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
public class SampleMybatisPagingJobConfiguration {

    private final SqlSessionFactory sqlSessionFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job sampleMybatisPagingJob(){
        return jobBuilderFactory.get("SampleMybatisPagingJob")
                .start(sampleMybatisPagingStep())
                .build();
    }

    @Bean
    public Step sampleMybatisPagingStep(){
        return stepBuilderFactory.get("SampleMybatisPagingStep")
                .<Order, Order>chunk(1000)
                .reader(sampleMybatisPagingReader())
                .writer(sampleMybatisPagingWriter())
                .build();
    }

    @Bean
    public MyBatisPagingItemReader<Order> sampleMybatisPagingReader() {
        return new MyBatisPagingItemReaderBuilder<Order>()
                .sqlSessionFactory(sqlSessionFactory)
                .pageSize(2)
                .queryId("com.example.mybatisbatch.sample.SampleMapper.selectOrder")
                .build();
    }

    @Bean
    public ItemWriter<Order> sampleMybatisPagingWriter() {
        return list -> {
            for (Order temp : list) {
                log.info("[Paging]writer : {}", temp);
            }
        };
    }
}
