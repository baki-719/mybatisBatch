package com.example.mybatisbatch.pagingtasklet;

import com.example.mybatisbatch.dto.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SamplePagingTaskletJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private String[] productArr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
            , "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    @Bean
    public Job job() {
        return jobBuilderFactory.get("SamplePagingTaskletJob")
                .start(step())
                .build();
    }

    @Bean
    public Step step() {

        return stepBuilderFactory.get("SamplePagingTaskletStep")
                .tasklet(tasklet())
                .build();

    }

    @Bean
    public Tasklet tasklet() {
        return (contribution, chunkContext) -> {

            int loopCount = 0;
            Map<String, Integer> orderCountMap = new HashMap<>();
            List<Order> orderList;

            long startAt = System.currentTimeMillis();

            while((orderList = makeRandomOrderList(10000)).size() > 0) {
                orderList.forEach(order -> orderCountMap.merge(order.getProductId(), 1, Integer::sum));
                log.info("{} 번째 완료", loopCount+1);

                if (++loopCount > 10000) break;
            }

            long endAt = System.currentTimeMillis();

            log.info("{} 초 소요", (endAt - startAt) /1000);

            return RepeatStatus.FINISHED;
        };
    }

    public List<Order> makeRandomOrderList(int orderCount) {

        List<Order> result = new ArrayList<>();

        for (int i = 0; i < orderCount; i++) {
            result.add(Order.builder()
                    .orderId(i)
                    .productId(productArr[(int) (Math.random() * 52)])
                    .orderQty((int) (Math.random() * 10))
                    .createAt(LocalDateTime.now())
                    .build());
        }

        return result;
    }
}
