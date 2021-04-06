package com.picture.business.async;

import com.springboot.simple.base.async.interfaces.BaseHandlerInterface;
import com.springboot.simple.base.fun.IFunction;
import com.springboot.simple.res.ResultEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 异步解析文件实现类
 */
public class FileListAsyncParsingHandler implements BaseHandlerInterface {

    private final List<MultipartFile> fileList;

    private final IFunction<MultipartFile,ResultEntity<Void>> function;

    public FileListAsyncParsingHandler(List<MultipartFile> fileList, IFunction<MultipartFile, ResultEntity<Void>> function) {
        this.fileList = fileList;
        this.function = function;
    }

    @Override
    public void handler() {
        if (CollectionUtils.isNotEmpty(fileList)) {
            // 解析文件
            fileList.forEach(file -> {
                try {
                    function.apply(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
