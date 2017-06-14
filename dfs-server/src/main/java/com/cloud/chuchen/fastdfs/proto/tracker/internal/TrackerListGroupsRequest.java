package com.cloud.chuchen.fastdfs.proto.tracker.internal;


import com.cloud.chuchen.fastdfs.proto.CmdConstants;
import com.cloud.chuchen.fastdfs.proto.FdfsRequest;
import com.cloud.chuchen.fastdfs.proto.ProtoHead;

/**
 * 列出分组命令
 * 
 * @author tobato
 *
 */
public class TrackerListGroupsRequest extends FdfsRequest {

    public TrackerListGroupsRequest() {
        head = new ProtoHead(CmdConstants.TRACKER_PROTO_CMD_SERVER_LIST_GROUP);
    }
}
