package com.aizuda.trans.service.impl.convert;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.TimeZone;

/**
 * 自定义字符串转换器实现
 *
 * @author nn200433
 * @date 2023-08-03 003 20:27:29
 */
@Slf4j
public class CustomerStrConvertImpl extends AbstractConverter<String> {

    public CustomerStrConvertImpl() {
        log.info("---> 使用 SPI 机制加载并覆盖 Hutool 默认的 Convert.toStr() 方法....");
    }

    /**
     * Clob字段值转字符串
     *
     * @param clob {@link Clob}
     * @return 字符串
     * @since 5.4.5
     */
    private static String clobToStr(Clob clob) {
        Reader reader = null;
        try {
            reader = clob.getCharacterStream();
            return IoUtil.read(reader);
        } catch (SQLException e) {
            throw new ConvertException(e);
        } finally {
            IoUtil.close(reader);
        }
    }

    /**
     * Blob字段值转字符串
     *
     * @param blob {@link Blob}
     * @return 字符串
     * @since 5.4.5
     */
    private static String blobToStr(Blob blob) {
        InputStream in = null;
        try {
            in = blob.getBinaryStream();
            return IoUtil.read(in, CharsetUtil.CHARSET_UTF_8);
        } catch (SQLException e) {
            throw new ConvertException(e);
        } finally {
            IoUtil.close(in);
        }
    }

    @Override
    protected String convertInternal(Object value) {
        if (value instanceof TimeZone) {
            return ((TimeZone) value).getID();
        } else if (value instanceof org.w3c.dom.Node) {
            return XmlUtil.toStr((org.w3c.dom.Node) value);
        } else if (value instanceof Clob) {
            return clobToStr((Clob) value);
        } else if (value instanceof Blob) {
            return blobToStr((Blob) value);
        } else if (value instanceof Type) {
            return ((Type) value).getTypeName();
        } else if (value instanceof Collection) {
            return StrUtil.join(StrUtil.COMMA, value);
        } else if (ArrayUtil.isArray(value)) {
            return StrUtil.join(StrUtil.COMMA, value);
        }

        // 其它情况
        return convertToStr(value);
    }

}
