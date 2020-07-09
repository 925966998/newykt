package com.ky.ykt.mybatis;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Linan
 */
public interface BaseMapper {
    @Select("${sql}")
    List<Map> _executeSelect(@Param("sql") String sql);

    @Select("select count(1) from (${sql}) t")
    long _executeSelectCount(@Param("sql") String sql);

    @Insert("${sql}")
    long _executeInsert(@Param("sql") String sql);

    @Update("${sql}")
    long _executeUpdate(@Param("sql") String sql);

    @Delete("${sql}")
    long _executeDelete(@Param("sql") String sql);
}
