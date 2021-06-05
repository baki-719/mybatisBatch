package com.example.mybatisbatch.sample;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SampleMapper {

    String selectLastItemId();
}
