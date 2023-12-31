![logo](./doc/imgs/logo.png)

# dict-trans

## 前言

简单的字典翻译组件

## 功能

* [x] 字典翻译
* [x] 枚举翻译
* [x] 数据库翻译
* [x] 自定义翻译
* [x] 翻译结果脱敏
* [x] json字符串翻译为json对象
* [x] 文章摘要提取（富文本将会变为纯文本）
* [x] 嵌套翻译

## 项目结构

```
dict-trans
 ├── dict-trans-core
 │   └── src
 │       └── main
 │           └── java
 │               └── com
 │                   └── aizuda
 │                       └── trans
 │                           ├── annotation                                               翻译注解
 │                           │   ├── Dictionary.java                                      字典注解，标识在字典数据类上（自动生成查询 SQL）
 │                           │   ├── Translate.java                                       翻译字段注解，标识在需要翻译的字段上
 │                           │   └── Translator.java                                      翻译方法注解，对方法返回值进行翻译
 │                           ├── constants                                                常量配置
 │                           │   └── DesensitizedTypeConstants.java
 │                           ├── desensitized                                             脱敏相关
 │                           │   ├── Desensitized.java
 │                           │   └── IDesensitized.java
 │                           ├── dict                                                     数据字典相关
 │                           │   └── DictTranslate.java
 │                           ├── entity                                                   参数实体相关
 │                           │   └── ExtendParam.java
 │                           ├── enums                                                    枚举相关
 │                           │   ├── EnumPool.java
 │                           │   ├── FormatType.java
 │                           │   └── IEnum.java
 │                           ├── json                                                     json相关
 │                           │   ├── IJsonConvert.java
 │                           │   └── JSONConvert.java
 │                           ├── summary                                                  摘要提取相关
 │                           │   ├── ISummaryExtract.java
 │                           │   └── SummaryExtract.java
 │                           ├── service
 │                           │   ├── DictTranslateService.java                            字典翻译接口（用户可自定义实现字典翻译功能）
 │                           │   ├── SummaryExtractService.java                           摘要提取服务接口（用户可自定义实现摘要提取功能）
 │                           │   └── Translatable.java                                    翻译接口（字典、枚举、....接实现该接口）
 │                           └── util                                                     一些工具类
 │                               ├── LambdaUtil.java
 │                               └── NameUtil.java
 ├── dict-trans-demo                                                                      demo演示
 │   └── src
 │       ├── main
 │       │   ├── java
 │       │   │   └── com
 │       │   │       └── aizuda
 │       │   │           └── trans
 │       │   │               ├── controller
 │       │   │               │   └── TestController.java
 │       │   │               ├── demo
 │       │   │               │   ├── DemoService.java
 │       │   │               │   └── impl
 │       │   │               │       ├── CustomerTranslateServiceImpl.java
 │       │   │               │       ├── DemoServiceImpl.java
 │       │   │               │       └── ResultUnWrapper.java                             业务统一返回 解包器实现
 │       │   │               ├── dict
 │       │   │               │   └── CustomerDictImpl.java
 │       │   │               ├── entity
 │       │   │               │   ├── Device.java
 │       │   │               │   ├── Dict.java
 │       │   │               │   ├── People.java
 │       │   │               │   ├── People2.java
 │       │   │               │   ├── People3.java
 │       │   │               │   ├── Result.java
 │       │   │               │   └── UserDB.java
 │       │   │               ├── enums
 │       │   │               │   └── DeviceStatus.java
 │       │   │               └── TranslatorBootApplication.java
 │       │   └── resources
 │       │       └── application.yml
 │       └── test
 │           └── java
 │               └── com
 │                   └── aizuda
 │                       └── trans
 │                           └── TranslatorTest.java
 ├── dict-trans-spring-boot-starter
 │   └── src
 │       └── main
 │           └── java
 │               └── com
 │                   └── aizuda
 │                       └── trans
 │                           ├── aspect                                                   翻译切面
 │                           │   └── TranslateAspect.java
 │                           ├── config
 │                           │   └── TranslatorConfig.java                                默认翻译方法注入配置
 │                           ├── handler                                                  主要操作类
 │                           │   └── TranslatorHandle.java
 │                           ├── util                                                     工具库
 │                           │   └── TranslatorUtil.java                                  提供翻译工具类（免写 @Translator 注解）
 │                           └── service
 │                               ├── impl
 │                               │   ├── convert
 │                               │   │   └── CustomerStrConvertImpl.java                  改动 Hutool 的字符串转换类
 │                               │   ├── DataBaseTranslator.java                          数据库翻译服务
 │                               │   ├── DefaultDictTranslateServiceImpl.java             默认数据字典翻译实现（实现字典翻译接口。仿照该方法，实现自己的业务）
 │                               │   ├── DesensitizedTranslator.java                      脱敏实现（没啥操作，就返回原值）
 │                               │   ├── DictCacheTranslator.java                         数据字典翻译实现（调用 字典翻译接口实现）
 │                               │   ├── EnumTranslator.java                              枚举翻译实现
 │                               │   ├── JsonConvertTranslator.java                       json翻译实现
 │                               │   ├── DefaultSummaryExtractServiceImpl.java            摘要提取服务默认实现
 │                               │   ├── SummaryExtractTranslator.java                    摘要提取翻译实现
 │                               │   └── wrapper
 │                               │       └── IPageUnWrapper.java                          mybatis-plus 解包实现（就是取 records 而已）
 │                               └── UnWrapper.java                                       解包接口
 └── doc
     ├── imgs
     │   └── demo.png
     └── t_test.sql                                                                       demo 数据库脚本
```

## 快速开始

### 引入jar包

> 不要问为什么强依赖 `Hutool` 、 `MyBatis-Plus` 。
>
> 就是爱！
>
> 不用这两个的，下面可以不用看了！！！

```xml
<dependencies>

  <!-- 可自行编译 或 从中央仓库引入 -->
  <!-- https://central.sonatype.com/artifact/com.aizuda/dict-trans -->
  <dependency>
    <groupId>com.aizuda</groupId>
    <artifactId>dict-trans</artifactId>
    <version>0.5</version>
  </dependency>
  
  <!-- hutool工具类（必须） -->
  <dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.8.21</version>
  </dependency>
  
  <!-- mybatis-plus 工具（必须） -->
  <dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.3.1</version>
  </dependency>
  
  <!--  ====================== 以下的非必须 ====================== -->
  
  <!-- spring-boot -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>2.7.12</version>
  </dependency>
  
  <!-- mysql 驱动 -->
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.49</version>
  </dependency>

</dependencies>
```

### 配置

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/t_test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&allowMultiQueries=true&useSSL=false&rewriteBatchedStatements=true
    username: root
    password: 123456
```

### 注解说明

#### @Dictionary

> 需配合 `@Translate` 、`@Translator` 食用

说明：标识在字典数据类上（自动生成查询 SQL），**只能用在类上**

参数：
* table：字典表名，为空时取 `TableName` ，取不到则取 类名（驼峰转大写下划线）
* codeColumn：字典编码的属性，对应 查询条件的列字段（需要的是表字段）
* textColumn：字典值的列名，对应 要查询的字段（需要的是表字段）
* groupColumn：字典组别属性，对应 要查询的字段（需要的是表字段，某些字典可能会需要根据某个类别划分，再进行翻译，如静态字典中的DICT_ID）
* translator：自定义翻译方法，遇到特殊的翻译场景可自定义翻译实现，需要自行编写实现类实现Translatable接口并实现翻译方法，程序将使用该方法进行翻译，该注解中的所有配置信息将传递到实现方法中

示例：
```java
// =========================== 示例1 ===========================
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

// =========================== 示例2 ===========================
/**
 * 人
 *
 * @author nn200433
 * @date 2022-12-16 016 11:40:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class People {
    
    @Translate(dictClass = Dict.class, groupValue = "sex", translateField = "sexName")
    private String sex;
    
    private String sexName;
    
    @Translate(dictClass = Desensitized.class, translateField = "phone", desensitizedModel = DesensitizedTypeConstants.MOBILE_PHONE)
    private String phone;
    
    @Translate(dictionary = @Dictionary(translator = CustomerTranslateServiceImpl.class), translateField = "name")
    private String id;
    
    private String name;

}
```

#### @Translate

> 需配合 `@Dictionary` 、`@Translator` 食用

说明：标识在需要翻译的字段上，**只能用在字段上**

参数：
* dictClass：字典配置类，指定的 class 上必须是 `DictTranslate.class` 实现类 、 `IEnum` 接口的实现类 、 `Desensitized` 、 `SummaryExtract` 或者是 `@Dictionary` 注解；
* translateField：翻译后的属性名，注意使用驼峰命名，默认为原属性名去除末尾的 "Id" 和 "Code" 再接上 "Name"；
* groupValue：组属性值，在静态字典表这种拥有组属性的字典中需要手动传入一个定值（即：字典分组的 code）；
* dictionary：指定 `Dictionary` 并设置其属性，将覆盖 `dictClass` 上的 `Dictionary` 注解的配置，指定了该属性后也可不指定 `dictClass` ，一般情况下不会使用；
* conditionField：指定判断条件字段（仅自定义翻译实现时用来进行判断）『20230822更新后支持固定值，格式：V:<值>』;
* desensitizedModel：脱敏模型，用来给数据脱敏打 `*` 使用。常见模型在 `DesensitizedTypeConstants` 常量中。也可自定义，格式：`{含开始位置,含结束位置}` ，举例：`{1,2}` ；

  *注：字段自身脱敏时，需将 `dictClass` 设置为 `Desensitized.class`（此时字段仅返回原值后脱敏。可与翻译共用，那样先翻译后脱敏。）*

* value：dictClass的别名，当只需要配置 `dictClass` 时，可以简写成 `@Translate(XX.class)` ；

示例：
```java
// =========================== 示例1 字典 ===========================

// 1.1 字典 code（需要在实现字典翻译服务时自己实现）

@Translate(dictClass = Dict.class, groupValue = "sex", translateField = "sexName")
private String sex;

private String sexName;

// 1.2 自定义完整字典（需要在实现字典翻译服务时自己实现）

@Translate(dictClass = Dict.class, groupValue = "{男:1;女:2;}", translateField = "sexName")
private String sex;

private String sexName;

// =========================== 示例2 数据库 ===========================

// 2.1. 字典定义
@Dictionary(codeColumn = "id", textColumn = {"user_name"})
@TableName("sys_user")
// 省略 @Data 等
// ....
public class UserDB {
}

// 2.2. 字段翻译
@Translate(dictClass = UserDB.class, translateField = "name")
private String id;
    
private String name;

// =========================== 示例3 枚举 ===========================

// 3.1. 枚举定义
public interface MyDict {
    /**
     * 示例1：性别枚举
     */
    enum SexDict implements IDict {
        //
        MALE("0", "男"),
        FEMALE("1", "女");

        SexDict(String code, String text) {
            // 构造方法中只需要调用接口的init方法即可，省略了属性的定义和赋值，也不用定义累赘的get方法
            init(code, text);
        }
    }
}

// 3.2. 枚举翻译
/**
 * 性别
 */
@Translate(MyDict.SexDict.class)
private String sex;

private String sexName;

// =========================== 示例4 脱敏 ===========================

// 4.1 字段给自身脱敏
@ApiModelProperty("联系电话")
@Translate(dictClass = Desensitized.class, desensitizedModel = DesensitizedTypeConstants.MOBILE_PHONE, translateField = "phone")
private String phone;

// 4.2 字段翻译并脱敏
@ApiModelProperty("用户等级")
@Translate(dictClass = Dict.class, groupValue = DictConstants.APP_USER_LEVEL, translateField = "userLevelName", desensitizedModel = "{0,1}")
private String userLevel;

@ApiModelProperty("用户等级中文")
private String userLevelName;

// =========================== 示例5 摘要提取 ===========================

/** 摘要提取 */
@Translate(dictClass = SummaryExtract.class, maxLen = 10)
private String introduce;

// =========================== 示例6 自定义 ===========================
    
// 5.1 翻译字段
@Translate(dictionary = @Dictionary(translator = CustomerTranslateServiceImpl.class), translateField = "name")
private String id;
    
private String name;

// 5.2 自定义翻译方法
@Component
public class CustomerTranslateServiceImpl implements Translatable {
    
    @Override
    public List<String> translate(String groupValue, String conditionValue, String origin, Dictionary dictConfig,
                                  Class dictClass) {
        List<String> rList = new ArrayList<String>(1);
        if (StrUtil.equals(origin, "1")) {
            rList.add("结果1");
        } else {
            rList.add("结果2");
        }
        return rList;
    }

}

// =========================== 示例6 数据库查询多字段进行字段映射 ===========================

// 6.1 数据库字典类
@Dictionary(codeColumn = "id", textColumn = {"user_name", "real_name"})
@TableName("sys_user")
// 省略 @Data 等
// ....
public class UserDB {
}


// 6.2 翻译字段
// 注意：translateField 需要跟 textColumn 的下标一一对应！！！
@Translate(dictClass = UserDB.class, translateField = {"zh", "zsxm"})
private String id;

private String zh;

private String zsxm;
    
// =========================== 示例7 json字符串翻译为json对象 ===========================
    
@Translate(dictClass = JSONConvert.class, translateField = "jsonObj")
private String json;

private Object jsonObj;
```

#### @Translator

> 需配合 `@Dictionary` 、`@Translate` 食用
>
> 方法返回类型支持 `Map` 、 `Entity` 、 `List` 、 `IPage`


说明：对方法返回值进行翻译，**可用在方法、字段上**

参数：无

示例：

```java
// =========================== 示例1 用在方法上 ===========================
@Component
public class DemoServiceImpl implements DemoService {
    
    @Translator
    @Override
    public List<People> dictDemo() {
        People man   = People.builder().sex("1").id("1").phone("18612345678").build();
        People woman = People.builder().sex("2").id("2").phone("18612345678").build();
        return CollUtil.newArrayList(man, woman);
    }
    
    @Translator
    @Override
    public List<Device> enumDemo() {
        Device man   = Device.builder().status("1").build();
        Device woman = Device.builder().status("2").build();
        return CollUtil.newArrayList(man, woman);
    }
    
    @Translator
    @Override
    public List<People2> dbDemo() {
        People2 man   = People2.builder().id("1").build();
        People2 woman = People2.builder().id("17ed02e3f05c629385371ce561f2dc50").build();
        return CollUtil.newArrayList(man, woman);
    }

}

// =========================== 示例2 用在字段上 ===========================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class People {
    
    @Translate(dictClass = Dict.class, groupValue = "sex", translateField = "sexName")
    private String sex;
    
    private String sexName;
    
    /** 手机号脱敏 */
    @Translate(dictClass = Desensitized.class, translateField = "phone", desensitizedModel = DesensitizedTypeConstants.MOBILE_PHONE)
    private String phone;
    
    /** 自定义翻译 */
    @Translate(dictionary = @Dictionary(translator = CustomerTranslateServiceImpl.class), translateField = "name")
    private String id;
    
    private String name;

    /** 用在字段上，可嵌套翻译 */
    @Translator
    private Device device;
    
}
```

嵌套翻译示例：

```java
package com.aizuda.trans.demo.impl;

import com.aizuda.trans.entity.Result;
import com.aizuda.trans.service.UnWrapper;
import org.springframework.stereotype.Component;

/**
 * 结果解包器
 *
 * @author nn200433
 * @date 2023-05-25 025 14:48:43
 */
@Component
public class ResultUnWrapper<T> implements UnWrapper<Result<T>> {

    @Override
    public Object unWrap(Result<T> source) {
        return source.getData();
    }

}
```

## 演示

> 完整示例参考 `dict-trans-demo` 模块

```java
/**
 * 转换测试
 *
 * @author nn200433
 * @date 2020年05月18日 0018 15:05:05
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TranslatorTest {

    @Autowired
    private DemoService demoService;

    @Test
    public void demo1() {
        List<People> peopleList = demoService.dictDemo();
        Console.log("---> 字典 & 脱敏 翻译结果：{}", JSONUtil.toJsonStr(peopleList));
    }

    @Test
    public void demo2() {
        List<Device> deviceList = demoService.enumDemo();
        Console.log("---> 枚举 翻译结果：{}", JSONUtil.toJsonStr(deviceList));
    }

    @Test
    public void demo3() {
        List<People2> peopleList = demoService.dbDemo();
        Console.log("---> 数据库 翻译结果：{}", JSONUtil.toJsonStr(peopleList));
    }

    @Test
    public void demo4() {
        List<People3> peopleList = demoService.jsonDemo();
        Console.log("---> json 翻译结果：{}", JSONUtil.toJsonStr(peopleList));
    }

    @Test
    public void demo5() {
        Result result = demoService.responseNestedMockTest();
        Console.log("---> 响应嵌套数据：{}", JSONUtil.toJsonStr(result));
    }

    @Test
    public void demo6() {
        List<People> peopleList = demoService.dictDemo2();
        Console.log("---> 字典 & 脱敏 & 摘要提取 翻译结果：{}", JSONUtil.toJsonStr(peopleList));
    }

}
```

![运行示例](./doc/imgs/demo.png)

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::               (v2.7.12)

2023-06-07 16:41:13.752  INFO 30996 --- [           main] com.aizuda.trans.TranslatorTest          : Starting TranslatorTest using Java 1.8.0_311 on LAPTOP-N9LDSE74 with PID 30996 (started by nn200 in D:\idea_hengfeng\dict-trans\dict-trans-demo)
2023-06-07 16:41:13.755  INFO 30996 --- [           main] com.aizuda.trans.TranslatorTest          : No active profile set, falling back to 1 default profile: "default"
2023-06-07 16:41:16.453  WARN 30996 --- [           main] o.m.s.mapper.ClassPathMapperScanner      : No MyBatis mapper was found in '[com.aizuda.trans]' package. Please check your configuration.
 _ _   |_  _ _|_. ___ _ |    _ 
| | |\/|_)(_| | |_\  |_)||_|_\ 
     /               |         
                        3.5.3.1 
2023-06-07 16:41:24.532  INFO 30996 --- [           main] com.aizuda.trans.TranslatorTest          : Started TranslatorTest in 12.088 seconds (JVM running for 15.043)
---> 字典 & 脱敏 翻译结果：[{"sex":"1","sexName":"男","phone":"186****5678","id":"1","name":"结果1"},{"sex":"2","sexName":"女","phone":"186****5678","id":"2","name":"结果2"}]
---> 枚举 翻译结果：[{"status":"1","statusName":"未使用"},{"status":"2","statusName":"试运行"}]
2023-06-07 16:41:25.435  INFO 30996 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2023-06-07 16:41:26.050  INFO 30996 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
---> 数据库 翻译结果：[{"id":"1","name":"张三"},{"id":"2","name":"李四"}]
---> json 翻译结果：[{"id":"1","name":"张三","json":"{\"abc\":\"def\", \"eg\":3}","jsonObj":{"abc":"def","eg":3}},{"id":"2","name":"李四","json":"[{\"a\":\"b\",\"c\":6},{\"d\":\"f\",\"e\":{\"a\":\"6\"}}]","jsonObj":[{"a":"b","c":6},{"d":"f","e":{"a":"6"}}]}]
---> 响应嵌套数据：{"status":200,"data":[{"sex":"1","sexName":"男","phone":"186****5678","id":"1","name":"结果1","device":{"status":"1","statusName":"未使用"}},{"sex":"2","sexName":"女","phone":"186****5678","id":"2","name":"结果2","device":{"status":"2","statusName":"试运行"}}]}
---> 字典 & 脱敏 & 摘要提取 翻译结果：[{"sex":"1","sexName":"男","phone":"186****5678","introduce":"我是一名热爱技术的软...","id":"1","name":"结果1"},{"sex":"2","sexName":"女","phone":"186****5678","introduce":"我是一名热爱艺术的画...","id":"2","name":"结果2"}]
2023-06-07 16:41:26.390  INFO 30996 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2023-06-07 16:41:26.403  INFO 30996 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
Disconnected from the target VM, address: '127.0.0.1:35075', transport: 'socket'

Process finished with exit code 0
```

## 附注

该项目得益于 [Transformer v1 版本](https://github.com/luo-zhan/Transformer) 增加了 脱敏，完善了 数据库翻译 等功能。（Transformer可能是最简单，但最强大的字段转换插件，一个注解搞定任意转换，让开发变得更加丝滑），基本上就是在此项目上增加功能。

## 特别鸣谢

> 感谢以下的项目,排名不分先后

* [Hutool](https://hutool.cn) Hutool是一个Java工具包，让Java语言也可以“甜甜的”。
* [MyBatis-Plus](https://baomidou.com/) MyBatis-Plus (opens new window)（简称 MP）是一个 MyBatis (opens new window)的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。
* [Transformer](https://github.com/luo-zhan/Transformer) Transformer 是一款功能全面的数据转换工具，只需要几个简单的注解配置，即可实现各种姿势的字段转换，抛弃连表查询和累赘的转换逻辑，让开发更简单。