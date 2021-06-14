package com.example.mybatisbatch.mybatissample;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SampleMapper {

    String selectLastItemId();
}
