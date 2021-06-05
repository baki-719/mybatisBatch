package com.example.mybatisbatch;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;

import java.util.ArrayList;
import java.util.List;

public class MybatisBatchItemListWriter<T> extends MyBatisBatchItemWriter<List<T>> {
    private MyBatisBatchItemWriter<T> myBatisBatchItemWriter;

    public MybatisBatchItemListWriter(SqlSessionFactory sqlSessionFactory, String statementId) {
        this.myBatisBatchItemWriter = new MyBatisBatchItemWriterBuilder<T>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId(statementId)
                .build();
        super.setSqlSessionFactory(sqlSessionFactory);
        super.setStatementId(statementId);
    }

    @Override
    public void write(final List<? extends List<T>> items) {
        List<T> totalList = new ArrayList<>();

        for (List<T> item : items) {
            totalList.addAll(item);
        }

        myBatisBatchItemWriter.write(totalList);
    }
}
