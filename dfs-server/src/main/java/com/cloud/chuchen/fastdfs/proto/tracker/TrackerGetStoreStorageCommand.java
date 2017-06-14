package com.cloud.chuchen.fastdfs.proto.tracker;


import com.cloud.chuchen.fastdfs.domain.StorageNode;
import com.cloud.chuchen.fastdfs.proto.AbstractFdfsCommand;
import com.cloud.chuchen.fastdfs.proto.FdfsResponse;
import com.cloud.chuchen.fastdfs.proto.tracker.internal.TrackerGetStoreStorageRequest;
import com.cloud.chuchen.fastdfs.proto.tracker.internal.TrackerGetStoreStorageWithGroupRequest;

/**
 * 获取存储节点命令
 * 
 * @author tobato
 *
 */
public class TrackerGetStoreStorageCommand extends AbstractFdfsCommand<StorageNode> {

    public TrackerGetStoreStorageCommand(String groupName) {
        super.request = new TrackerGetStoreStorageWithGroupRequest(groupName);
        super.response = new FdfsResponse<StorageNode>() {
            // default response
        };
    }

    public TrackerGetStoreStorageCommand() {
        super.request = new TrackerGetStoreStorageRequest();
        super.response = new FdfsResponse<StorageNode>() {
            // default response
        };
    }

}
