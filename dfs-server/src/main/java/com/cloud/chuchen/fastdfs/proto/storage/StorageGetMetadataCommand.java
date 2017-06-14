package com.cloud.chuchen.fastdfs.proto.storage;



import com.cloud.chuchen.fastdfs.domain.MateData;
import com.cloud.chuchen.fastdfs.proto.AbstractFdfsCommand;
import com.cloud.chuchen.fastdfs.proto.storage.internal.StorageGetMetadataRequest;
import com.cloud.chuchen.fastdfs.proto.storage.internal.StorageGetMetadataResponse;

import java.util.Set;

/**
 * 设置文件标签
 * 
 * @author tobato
 *
 */
public class StorageGetMetadataCommand extends AbstractFdfsCommand<Set<MateData>> {

    /**
     * 设置文件标签(元数据)
     * 
     * @param groupName
     * @param path
     * @param metaDataSet
     * @param type
     */
    public StorageGetMetadataCommand(String groupName, String path) {
        this.request = new StorageGetMetadataRequest(groupName, path);
        // 输出响应
        this.response = new StorageGetMetadataResponse();
    }

}
