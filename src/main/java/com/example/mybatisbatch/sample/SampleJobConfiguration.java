package com.example.mybatisbatch.sample;

import com.example.mybatisbatch.MybatisBatchItemListWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SampleJobConfiguration {

    private final SampleMapper mapper;
    private final SqlSessionFactory sqlSessionFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private HashMap<String, OrderQtyByProduct> orderSummaryMap = new HashMap<>();
    private String lastOrderId;

    @Bean
    public Job job(){
        return jobBuilderFactory.get("SampleJob")
                .start(step())
                .build();
    }

    @Bean
    public Step step(){
        return stepBuilderFactory.get("SampleStep")
                .<Order, List<OrderQtyByProduct>>chunk(1000)
                .reader(reader())
                .processor(processor())
                .writer(writerList())
                .listener(listener())
                .build();
    }

    @Bean
    public StepExecutionListener listener() {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                lastOrderId = mapper.selectLastItemId();
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                return null;
            }
        };
    }

    @Bean
    public MyBatisPagingItemReader<Order> reader() {
        return new MyBatisPagingItemReaderBuilder<Order>()
                .sqlSessionFactory(sqlSessionFactory)
                .pageSize(2)
                .queryId("com.example.mybatisbatch.sample.SampleMapper.selectOrder")
                .build();
    }

    @Bean
    public ItemProcessor<Order, List<OrderQtyByProduct>> processor() {
        return item -> {
            orderSummaryMap.merge(item.getProductId()
                    , OrderQtyByProduct.builder()
                            .productId(item.getProductId())
                            .totalOrderQty(1)
                            .build()
                    , (key, value) -> value.addCount());

            if(item.getOrderId() == Integer.parseInt(lastOrderId)) {
                log.info("{}", orderSummaryMap);
                return new ArrayList<>(orderSummaryMap.values());
            }
            else
                return null;
        };
    }

    @Bean
    public MyBatisBatchItemWriter<List<OrderQtyByProduct>> writerList() {
        String statementId = "com.example.mybatisbatch.sample.SampleMapper.insert";

        return new MybatisBatchItemListWriter<>(sqlSessionFactory, statementId);
    }
}
