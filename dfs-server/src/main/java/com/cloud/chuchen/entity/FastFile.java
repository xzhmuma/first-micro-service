package com.cloud.chuchen.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_file")
public class FastFile extends IdEntity {


    private String businessId;
    private String businessType;

    private Long naturalId;
    private String fileName;
    private String fdfsDir;
    private String fdfsName;
    private String fileType;
    private Long fileSize;
    private String thumbDir;
    private String thumbName;
    private Integer width;
    private Integer height;
    private String creator;
    private Date createTime;


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Long getNaturalId() {
        return naturalId;
    }

    public void setNaturalId(Long naturalId) {
        this.naturalId = naturalId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFdfsDir() {
        return fdfsDir;
    }

    public void setFdfsDir(String fdfsDir) {
        this.fdfsDir = fdfsDir;
    }

    public String getFdfsName() {
        return fdfsName;
    }

    public void setFdfsName(String fdfsName) {
        this.fdfsName = fdfsName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getThumbDir() {
        return thumbDir;
    }

    public void setThumbDir(String thumbDir) {
        this.thumbDir = thumbDir;
    }

    public String getThumbName() {
        return thumbName;
    }

    public void setThumbName(String thumbName) {
        this.thumbName = thumbName;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}