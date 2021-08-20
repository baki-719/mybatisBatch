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
import org.springframework.core.convert.converter.Converter;

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
                .<Order, Order>chunk(1000)
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
    public ItemProcessor<Order, Order> sampleItemProcessor() {
        return item -> item;
    }

    @Bean
    public MyBatisBatchItemWriter<Order> sampleMybatisPagingWriter() {
        return new MyBatisBatchItemWriterBuilder<Order>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.example.mybatisbatch.mybatissample.SampleMapper.update")
//                .itemToParameterConverter(orderDeliveryConverter())
                .build();
    }

    private Converter<Order, Delivery> orderDeliveryConverter() {
        return Delivery::of;
    }
}
