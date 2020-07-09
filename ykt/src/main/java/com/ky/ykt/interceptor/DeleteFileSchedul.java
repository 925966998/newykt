package com.ky.ykt.interceptor;

import com.ky.ykt.utils.PathUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ky.ykt.excle.ExportHM.getUploadPath;

/**
 * @ClassName DeleteFileSchedul
 * @Description: TODO
 * @Author czw
 * @Date 2020/5/13
 **/
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling
public class DeleteFileSchedul {
    public static List<String> fileNameList = new ArrayList<String>();

    //每天00：00执行
    @Scheduled(cron = "0 00 00 ? * *")
    public void deleteFile() {
        /*String property = System.getProperty("user.dir");
        String path = property + "**********\\20200429170147.xls";
        File file = new File(path);
        file.delete();*/
        String filepath = PathUtil.getClasspath() + "upload";
        String file = filepath;
        delFolder(file);
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            //不想删除文佳夹隐藏下面
//			String filePath = folderPath;
//			filePath = filePath.toString();
//			java.io.File myFilePath = new java.io.File(filePath);
//			myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        System.out.println(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        System.out.println(tempList.length);
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
//				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
}
