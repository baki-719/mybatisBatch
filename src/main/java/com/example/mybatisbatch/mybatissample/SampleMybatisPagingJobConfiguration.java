package com.example.mybatisbatch.mybatissample;

import com.example.mybatisbatch.dto.Order;
import com.example.mybatisbatch.dto.Delivery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

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
                .<Order, Delivery>chunk(1000)
                .reader(sampleMybatisPagingReader())
                .processor(sampleItemProcessor())
                .writer(sampleMybatisPagingWriter())
                .build();
    }

    @Bean
    public MyBatisPagingItemReader<Order> sampleMybatisPagingReader() {
        return new MyBatisPagingItemReaderBuilder<Order>()
                .sqlSessionFactory(sqlSessionFactory)
                .pageSize(2)
                .queryId("com.example.mybatisbatch.mybatissample.SampleMapper.selectOrder")
                .build();
    }

    @Bean
    public ItemProcessor<Order, Delivery> sampleItemProcessor() {
        return Delivery::of;
    }

    @Bean
    public MyBatisBatchItemWriter<Delivery> sampleMybatisPagingWriter() {
        return new MyBatisBatchItemWriterBuilder<Delivery>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.example.mybatisbatch.mybatissample.SampleMapper.insert")
                .build();
    }
}
