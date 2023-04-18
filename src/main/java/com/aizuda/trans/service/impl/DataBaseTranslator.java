package com.aizuda.trans.service.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityHandler;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.extra.spring.SpringUtil;
import com.aizuda.trans.annotation.Dictionary;
import com.aizuda.trans.enums.FormatType;
import com.aizuda.trans.service.Translatable;
import com.aizuda.trans.util.NameUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 翻译默认实现
 * <p>
 * 适用于字典为单表，使用sql查询
 *
 * @author luozhan
 * @create 2020-04
 */
@Slf4j
@Component
public class DataBaseTranslator implements Translatable {
    
    /**
     * 获取数据源
     */
    private static DataSource dataSource = SpringUtil.getBean(DataSource.class);
    
    @Override
    public List<String> translate(String groupValue, String conditionValue, String origin, Dictionary dictConfig, Class dictClass) {
        // 获取参数
        String codeColumn = dictConfig.codeColumn();
        String[] textColumnArray = dictConfig.textColumn();
        String groupColumn = dictConfig.groupColumn();
        String tableName = getTableName(dictConfig, dictClass);

        Assert.isTrue(StrUtil.isNotEmpty(codeColumn), "@Dictionary注解codeColumn配置有误，找不到指定的属性名，class:" + dictClass.getSimpleName());
        Assert.isTrue(ArrayUtil.isNotEmpty(textColumnArray), "@Dictionary注解textColumn配置有误，找不到指定的属性名，class:" + dictClass.getSimpleName());

        List<String> rsList = new ArrayList<String>(textColumnArray.length);
        try {
            log.debug("---> 触发字典翻译：查询表 {} 中的字段 {} ，查询条件 {} = {}", tableName, textColumnArray, codeColumn, origin);

            // 查询条件为空时直接返回相对应个数的空数组
            if (ObjectUtil.isNull(origin) || StrUtil.isBlank(origin)) {
                log.debug("---> 触发字典翻译：查询条件为空，直接返回对应个数的空数组");
                return Opt.ofNullable(textColumnArray).stream().map(s -> StrUtil.EMPTY).collect(Collectors.toList());
            }

            Entity where = Entity.create(tableName);
            if (StrUtil.isNotEmpty(groupColumn)) {
                where.set(groupColumn, groupValue);
            }

            if (StrUtil.contains(origin, StrUtil.COMMA)) {
                // 多条记录取单列（查询结果为多条记录，循环后转为单条）
                // 传入数据：1,2,3
                // 查询结果：["张三","李四","王五"]
                // 返回结果：张三、李四、王五
                final String field = textColumnArray[0];
                where.set(codeColumn, StrUtil.split(origin, StrUtil.COMMA));
                List<Entity> entityList = Db.use(dataSource).find(where, EntityListHandler.create(), textColumnArray);
                rsList.add(entityList.stream().map(e -> e.getStr(field)).collect(Collectors.joining("、")));
            } else {
                // 单条记录多列（查询结果为单条多列，循环后为多条，根据传入的字段顺序返回）
                // 传入数据：1
                // 查询结果：{ "列1": "aa", "列2": "bb"}
                // 返回结果：["aa","bb"]
                where.set(codeColumn, origin);
                Entity entity = Db.use(dataSource).find(where, EntityHandler.create(), textColumnArray);
                for (String column : textColumnArray) {
                    rsList.add(entity.getStr(column));
                }
            }
        } catch (NullPointerException e) {
            // 空指针异常时，可能查询条件为空。返回空数组
            log.error("---> DataBaseTranslator 字典翻译，空指针异常", e);
        } catch (SQLException e) {
            log.error("---> DataBaseTranslator 字典翻译，SQL查询异常", e);
        }

        return rsList;
    }
    
    /**
     * 获取表名
     *
     * @param dictConfig dict配置
     * @param dictClass  dict类
     * @return {@link String }
     * @author nn200433
     */
    private String getTableName(Dictionary dictConfig, Class dictClass) {
        if (StrUtil.isNotEmpty(dictConfig.table())) {
            return dictConfig.table();
        }

        // 类名转表名
        final String className = dictClass.getSimpleName();
        String tName = className.substring(0, 1) + NameUtil.parseCamelTo(className.substring(1), FormatType.UPPERCASE_UNDERLINE);

        // 获取mp注解上的表名
        TableName tableName = (TableName) dictClass.getAnnotation(TableName.class);
        if (null != tableName) {
            tName = tableName.value();
        }

        return tName;
    }

}
