package com.aizuda.trans.entity;

import com.aizuda.trans.annotation.Dictionary;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户(数据库)
 *
 * @author nn200433
 * @date 2022-12-16 016 14:07:27
 */
@Dictionary(codeColumn = "id", textColumn = {"user_name"})
@TableName("sys_user")
public class UserDB {
    
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    
    @TableField(value = "user_name")
    private String name;
    
}
