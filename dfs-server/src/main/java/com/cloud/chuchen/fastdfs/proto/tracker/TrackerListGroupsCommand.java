package com.cloud.chuchen.fastdfs.proto.tracker;


import com.cloud.chuchen.fastdfs.domain.GroupState;
import com.cloud.chuchen.fastdfs.proto.AbstractFdfsCommand;
import com.cloud.chuchen.fastdfs.proto.tracker.internal.TrackerListGroupsRequest;
import com.cloud.chuchen.fastdfs.proto.tracker.internal.TrackerListGroupsResponse;

import java.util.List;

/**
 * 列出组命令
 * 
 * @author tobato
 *
 */
public class TrackerListGroupsCommand extends AbstractFdfsCommand<List<GroupState>> {

    public TrackerListGroupsCommand() {
        super.request = new TrackerListGroupsRequest();
        super.response = new TrackerListGroupsResponse();
    }

}
