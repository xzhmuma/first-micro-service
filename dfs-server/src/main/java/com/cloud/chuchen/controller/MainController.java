package com.cloud.chuchen.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.chuchen.entity.FastFile;
import com.cloud.chuchen.fastdfs.domain.MateData;
import com.cloud.chuchen.fastdfs.domain.StorePath;
import com.cloud.chuchen.fastdfs.domain.ThumbImageConfig;
import com.cloud.chuchen.fastdfs.service.FastFileStorageClient;
import com.cloud.chuchen.repo.FastFileRepo;
import com.cloud.chuchen.result.BaseResult;
import com.cloud.chuchen.result.ResultCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/8.
 */
@RestController
@RequestMapping("/fdfs/")
public class MainController {
    @Autowired
    protected FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Autowired
    private FastFileRepo fastFileRepo;


    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    /**
     * 上传且压缩 (第一次上传)
     *
     * @param file         文件
     * @param creator      上传者
     * @param businessId   业务id
     * @param businessType 业务类型
     * @return json字符串
     */
    @ApiOperation(value = "上传且压缩", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file"),
            @ApiImplicitParam(name = "creator", value = "用户", required = true, dataType = "String"),
            @ApiImplicitParam(name = "businessId", value = "业务id", dataType = "String"),
            @ApiImplicitParam(name = "businessType", value = "业务类型", dataType = "String")

    })
    @PostMapping("firstUploadAndThumb")
    public BaseResult firstUploadAndThumb(MultipartFile file, String creator, String businessId, String businessType) {

        try {
            Date now = new Date();
            String originalFilename = file.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String extName = file.getOriginalFilename().substring(index + 1);

            // 附带上传信息
            Set<MateData> metaDataSet = new HashSet<>();
            metaDataSet.add(new MateData("creator", creator));
            metaDataSet.add(new MateData("createTime", "" + now.getTime()));
            metaDataSet.add(new MateData("fileName", originalFilename));
            metaDataSet.add(new MateData("uploadType", "2"));
            // 上传文件和Metadata
            StorePath path = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), extName, metaDataSet);
            // 这里需要一个获取从文件名的能力，所以从文件名配置以后就最好不要改了
            String thumbPath = thumbImageConfig.getThumbImagePath(path.getPath());
            log.info(String.format("上传文件路径---%s,上传文件缩略图路径---%s", path, thumbPath));

            int dirIndex = path.getFullPath().lastIndexOf("/");
            String dir = path.getFullPath().substring(0, dirIndex + 1);
            String fileName = path.getFullPath().substring(dirIndex + 1);

            int thumbDirIndex = path.getFullPath().lastIndexOf("/");
            String thumbDir = path.getFullPath().substring(0, thumbDirIndex + 1);
            String thumbFileName = path.getFullPath().substring(thumbDirIndex + 1);

            FastFile fastFile = new FastFile();
            fastFile.setBusinessId(businessId);
            fastFile.setBusinessType(businessType);
            fastFile.setCreator(creator);
            fastFile.setCreateTime(now);
            fastFile.setFileName(originalFilename);
            fastFile.setFdfsDir(dir);
            fastFile.setFdfsName(fileName);
            fastFile.setFileType(extName);
            fastFile.setFileSize(file.getSize());

            fastFile.setThumbDir(thumbDir);
            fastFile.setThumbName(thumbFileName);
            if (storageClient.isSupportImage(extName)) {
                //获取上传图片的宽高
                BufferedImage bi = ImageIO.read(file.getInputStream());
                fastFile.setWidth(bi.getWidth());
                fastFile.setHeight(bi.getHeight());
            }
            fastFile = fastFileRepo.save(fastFile);

            log.info(String.format("插入FastFile数据成功(第一次上传 压缩)---%s", fastFile.getId()));


            return BaseResult.success(resultJson(fastFile));
        } catch (Exception e) {
            log.error(String.format("上传文件失败,错误为%s", e.getMessage()));
            return BaseResult.error(ResultCode.SGW_WRONG);
        }
    }

    /**
     * 上传不压缩(第一次上传)
     *
     * @param file         文件
     * @param creator      上传者
     * @param businessId   业务id
     * @param businessType 业务类型
     * @return 全路径
     */
    @ApiOperation(value = "上传不压缩(第一次上传)", notes = "上传不压缩(第一次上传)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file"),
            @ApiImplicitParam(name = "creator", value = "用户", required = true, dataType = "String"),
            @ApiImplicitParam(name = "businessId", value = "业务id", dataType = "String"),
            @ApiImplicitParam(name = "businessType", value = "业务类型", dataType = "String")

    })
    @PostMapping("firstUpload")
    public BaseResult firstUpload(MultipartFile file, String creator, String businessId, String businessType) {

        try {
            Date now = new Date();
            String originalFilename = file.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String extName = file.getOriginalFilename().substring(index + 1);

            // 附带上传信息
            Set<MateData> metaDataSet = new HashSet<>();
            metaDataSet.add(new MateData("creator", creator));
            metaDataSet.add(new MateData("createTime", "" + now.getTime()));
            metaDataSet.add(new MateData("originalFilename", originalFilename));
            metaDataSet.add(new MateData("businessId", "" + businessId));
            metaDataSet.add(new MateData("businessType", "" + businessType));

            metaDataSet.add(new MateData("uploadType", "1"));

            // 上传文件和Metadata
            StorePath path = storageClient.uploadFile(file.getInputStream(), file.getSize(), extName, metaDataSet);
            log.info(String.format("上传文件路径---%s", path));

            int dirIndex = path.getFullPath().lastIndexOf("/");
            String dir = path.getFullPath().substring(0, dirIndex + 1);
            String fileName = path.getFullPath().substring(dirIndex + 1);

            FastFile fastFile = new FastFile();
            fastFile.setBusinessId(businessId);
            fastFile.setBusinessType(businessType);
            fastFile.setCreator(creator);
            fastFile.setCreateTime(now);
            fastFile.setFileName(originalFilename);
            fastFile.setFdfsDir(dir);
            fastFile.setFdfsName(fileName);
            fastFile.setFileType(extName);
            fastFile.setFileSize(file.getSize());

            if (storageClient.isSupportImage(extName)) {
                //获取上传图片的宽高
                BufferedImage bi = ImageIO.read(file.getInputStream());
                fastFile.setWidth(bi.getWidth());
                fastFile.setHeight(bi.getHeight());
            }
            fastFile = fastFileRepo.save(fastFile);
            log.info(String.format("插入FastFile数据成功(第一次上传不压缩)---%s", fastFile.getId()));

            return BaseResult.success(resultJson(fastFile));
        } catch (Exception e) {
            log.error(String.format("上传文件失败,错误为%s", e.getMessage()));
            return BaseResult.error(ResultCode.SGW_WRONG);
        }
    }

    /**
     * 上传不压缩
     *
     * @param file      文件
     * @param creator   上传者
     * @param fdfsId    fdfsId
     * @param naturalId 自然id
     * @return json
     */
    @ApiOperation(value = "上传不压缩", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file"),
            @ApiImplicitParam(name = "creator", value = "用户", required = true, dataType = "String"),
            @ApiImplicitParam(name = "fdfsId", value = "fdfsId", dataType = "Long"),
            @ApiImplicitParam(name = "naturalId", value = "自然naturalId", dataType = "Long")
    })
    @PostMapping("upload")
    public BaseResult upload(MultipartFile file, String creator, Long fdfsId, Long naturalId) {

        try {
            Date now = new Date();
            String originalFilename = file.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String extName = file.getOriginalFilename().substring(index + 1);

            // 附带上传信息
            Set<MateData> metaDataSet = new HashSet<>();
            metaDataSet.add(new MateData("creator", creator));
            metaDataSet.add(new MateData("createTime", "" + now.getTime()));
            metaDataSet.add(new MateData("fileName", originalFilename));
            metaDataSet.add(new MateData("id", "" + fdfsId));
            metaDataSet.add(new MateData("naturalId", "" + naturalId));
            metaDataSet.add(new MateData("uploadType", "3"));
            // 上传文件和Metadata
            StorePath path = storageClient.uploadFile(file.getInputStream(), file.getSize(), extName, metaDataSet);
            log.info(String.format("上传文件路径---%s", path));

            FastFile oldFastFile = fastFileRepo.findOne(fdfsId);
            oldFastFile.setNaturalId(naturalId);
            fastFileRepo.save(oldFastFile);
            log.info(String.format("修改FastFile数据成功(上传  不压缩)---%s---naturalId--%s", oldFastFile.getId(), naturalId));

            int dirIndex = path.getFullPath().lastIndexOf("/");
            String dir = path.getFullPath().substring(0, dirIndex + 1);
            String fileName = path.getFullPath().substring(dirIndex + 1);

            FastFile fastFile = new FastFile();
            fastFile.setBusinessId(oldFastFile.getBusinessId());
            fastFile.setBusinessType(oldFastFile.getBusinessType());
            fastFile.setNaturalId(naturalId);
            fastFile.setCreator(creator);
            fastFile.setCreateTime(now);
            fastFile.setFileName(originalFilename);
            fastFile.setFdfsDir(dir);
            fastFile.setFdfsName(fileName);
            fastFile.setFileType(extName);
            fastFile.setFileSize(file.getSize());

            if (storageClient.isSupportImage(extName)) {
                //获取上传图片的宽高
                BufferedImage bi = ImageIO.read(file.getInputStream());
                fastFile.setWidth(bi.getWidth());
                fastFile.setHeight(bi.getHeight());
            }
            fastFileRepo.save(fastFile);

            log.info(String.format("插入FastFile数据成功(上传 不压缩)---%s", fastFile.getId()));

            return BaseResult.success(resultJson(fastFile));
        } catch (Exception e) {
            log.error(String.format("上传文件失败,错误为%s", e.getMessage()));
            return BaseResult.error(ResultCode.SGW_WRONG);
        }
    }

    /**
     * 上传且压缩
     *
     * @param file      文件
     * @param creator   上传者
     * @param fdfsId    fdfsId
     * @param naturalId 自然id
     * @return 全路径(原图和缩略图)
     */
    @ApiOperation(value = "上传且压缩)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file"),
            @ApiImplicitParam(name = "creator", value = "用户", required = true, dataType = "String"),
            @ApiImplicitParam(name = "fdfsId", value = "fdfsId", dataType = "Long"),
            @ApiImplicitParam(name = "naturalId", value = "自然naturalId", dataType = "Long")
    })
    @PostMapping("uploadAndThumb")
    public BaseResult uploadAndThumb(MultipartFile file, String creator, Long fdfsId, Long naturalId) {

        try {
            Date now = new Date();
            String originalFilename = file.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String extName = file.getOriginalFilename().substring(index + 1);

            // 附带上传信息
            Set<MateData> metaDataSet = new HashSet<>();
            metaDataSet.add(new MateData("creator", creator));
            metaDataSet.add(new MateData("createTime", "" + now.getTime()));
            metaDataSet.add(new MateData("fileName", originalFilename));
            metaDataSet.add(new MateData("id", "" + fdfsId));
            metaDataSet.add(new MateData("naturalId", "" + naturalId));

            metaDataSet.add(new MateData("uploadType", "4"));
            // 上传文件和Metadata
            StorePath path = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), extName, metaDataSet);

            // 这里需要一个获取从文件名的能力，所以从文件名配置以后就最好不要改了
            String thumbPath = thumbImageConfig.getThumbImagePath(path.getPath());
            log.info(String.format("上传文件路径---%s,上传文件缩略图路径---%s", path, thumbPath));


            int dirIndex = path.getFullPath().lastIndexOf("/");
            String dir = path.getFullPath().substring(0, dirIndex + 1);
            String fileName = path.getFullPath().substring(dirIndex + 1);

            int thunbDirIndex = path.getFullPath().lastIndexOf("/");
            String thumbDir = path.getFullPath().substring(0, thunbDirIndex + 1);
            String thumbFileName = path.getFullPath().substring(thunbDirIndex + 1);

            FastFile oldFastFile = fastFileRepo.findOne(fdfsId);
            oldFastFile.setNaturalId(naturalId);
            fastFileRepo.save(oldFastFile);
            log.info(String.format("修改FastFile数据成功(上传  压缩)---%s---naturalId--%s", oldFastFile.getId(), naturalId));

            FastFile fastFile = new FastFile();
            fastFile.setBusinessId(oldFastFile.getBusinessId());
            fastFile.setBusinessType(oldFastFile.getBusinessType());
            fastFile.setNaturalId(naturalId);
            fastFile.setCreator(creator);
            fastFile.setCreateTime(now);
            fastFile.setFileName(originalFilename);
            fastFile.setFdfsDir(dir);
            fastFile.setFdfsName(fileName);
            fastFile.setFileType(extName);
            fastFile.setFileSize(file.getSize());

            fastFile.setThumbDir(thumbDir);
            fastFile.setThumbName(thumbFileName);
            if (storageClient.isSupportImage(extName)) {
                //获取上传图片的宽高
                BufferedImage bi = ImageIO.read(file.getInputStream());
                fastFile.setWidth(bi.getWidth());
                fastFile.setHeight(bi.getHeight());
            }
            fastFile = fastFileRepo.save(fastFile);
            log.info(String.format("插入FastFile数据成功(上传  压缩)---%s", fastFile.getId()));

            return BaseResult.success(resultJson(fastFile));
        } catch (Exception e) {
            log.error(String.format("上传文件失败,错误为%s", e.getMessage()));
            return BaseResult.error(ResultCode.SGW_WRONG);
        }
    }


    /**
     * 删除
     *
     * @param path 路径
     */
    @ApiOperation(value = "删除", notes = "")
    @ApiImplicitParam(name = "path", value = "路径", required = true, dataType = "String")
    @PostMapping("delete")
    public BaseResult delete(String path) {
        try {
            int index = path.indexOf("/");
            String groupName = path.substring(0, index);
            String pathName = path.substring(index + 1);
            Set<MateData> fetchMateData = storageClient.getMetadata(groupName, pathName);
            for (MateData m : fetchMateData) {
                if ("uploadType".equals(m.getName())) {
                    String thumbPath = thumbImageConfig.getThumbImagePath(pathName);
                    storageClient.deleteFile(groupName, thumbPath);
                    log.info(String.format("删除缩略图---%s", thumbPath));

                    break;
                }
            }
            storageClient.deleteFile(groupName, pathName);
            log.info(String.format("删除源文件---%s", pathName));

            return BaseResult.success("删除成功");

        } catch (Exception e) {
            log.error(String.format("删除文件失败,文件路径为%s,错误为%s", path, e.getMessage()));
            return BaseResult.error(ResultCode.SGW_WRONG);
        }
    }

    private JSONObject resultJson(FastFile fastFile) {
        JSONObject json = new JSONObject(true);
        json.put("fdfsId", fastFile.getId());
        json.put("fileName", fastFile.getFileName());
        json.put("fdfsDir", fastFile.getFdfsDir());
        json.put("fdfsName", fastFile.getFdfsName());
        json.put("fileType", fastFile.getFileType());
        json.put("fileSize", fastFile.getFileSize());
        json.put("thumbDir", fastFile.getThumbDir());
        json.put("thumbName", fastFile.getThumbName());
        json.put("width", fastFile.getWidth());
        json.put("height", fastFile.getHeight());
        return json;
    }

}