package com.resumebuilder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resumebuilder.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
