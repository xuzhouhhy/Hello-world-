package com.xuzhouhhy.baidumap.util;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Sdcard管理类
 */
public class UtilSDCardManager {

    /**
     * 获得SD卡目录
     *
     * @return
     */
    public static String GetSDCardPath() {

        String sdCard = Environment.getExternalStorageDirectory().getAbsolutePath();

        // 获得所有外置sdcard路径
        List<String> lstSdCards = GetAllExtenalSDCards();

        if (lstSdCards.size() == 1) {// 只有一个sdcard
            sdCard = lstSdCards.get(0);
        } else if (lstSdCards.size() > 1) {// 存在多个，容量大的作为sdcard路径
            int nIndex = 0;
            long maxSize = 0;
            long size = 0;
            // 循环获得最大总容量sdcard
            for (int i = 0; i < lstSdCards.size(); i++) {
                size = getSDAllSize(lstSdCards.get(i));
                if (size > maxSize) {
                    nIndex = i;
                    maxSize = size;
                }
            }
            sdCard = lstSdCards.get(nIndex);
        }
        if (lstSdCards.size() >= 0) {
            sdCard = Environment.getExternalStorageDirectory().getPath();
        }

        return sdCard;
    }

    /**
     * 获得所有外置sdcard
     *
     * @return
     */
    public static List<String> GetAllExtenalSDCards() {
        List<String> lstSdcards = new ArrayList<String>();
        // 得到路径
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;

                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        lstSdcards.add(columns[1]);
                    }
                } else if (line.contains("fuse")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        lstSdcards.add(columns[1]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstSdcards;
    }

    /**
     * 获得SD卡剩余空间
     *
     * @param sdCardPath
     * @return
     */
    public static long getSDFreeSize(String sdCardPath) {
        // 取得SD卡文件路径
        StatFs sf = new StatFs(sdCardPath);
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        // return freeBlocks * blockSize; //单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /***
     * 获得SD卡全部空间
     *
     * @param sdCardPath
     * @return
     */
    public static long getSDAllSize(String sdCardPath) {
        // 取得SD卡文件路径
        StatFs sf = null;
        try {
            sf = new StatFs(sdCardPath);
        } catch (Exception e) {
            return 0L;     //修改海外读取SD卡失败的问题
        }

        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 获取所有数据块数
        long allBlocks = sf.getBlockCount();
        // 返回SD卡大小
        // return allBlocks * blockSize; //单位Byte
        // return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 是否存在sdcard
     *
     * @return
     */
    public static boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取第一个SD的目录
     */
    public static String GetFirstSDCardPath() {
        String path = "";

        // 获得所有外置sdcard路径
//	    List<String> lstSdCards = GetAllExtenalSDCards();
//	    if(lstSdCards.size() > 0)
//	    {
//	    	path = lstSdCards.get(0);	    	
//	    }
//	    else 
//	    {
//			path = GetSDCardPath();
//		}

        path = GetSDCardPath();

        path += "/";

        return path;
    }

    /**
     * 返回给定目录下所有文件的列表
     */
    public static ArrayList<String> GetFiles(String path, String Extension, boolean IsIterative) {
        ArrayList<String> arrayList = new ArrayList<String>();

        File[] files = new File(path).listFiles();
        File f;
        if (files == null) {
            return arrayList;
        }

        for (int i = 0; i < files.length; i++) {
            f = files[i];
            if (!f.canRead()) {
                continue;
            }
            if (f.isFile()) {
                if (f.getName().contains(Extension))  //判断扩展名                         
                {
                    arrayList.add(f.getName());
                }
            } else if (f.isDirectory()) {
                GetFiles(f.getPath(), Extension, IsIterative);
            }
        }

        return arrayList;
    }

    /**
     * 判断指定的目录是否存在
     */
    public static boolean IsExisted(String folderPath) {
        File file = new File(folderPath);

        return file.exists();
    }

    /**
     * 创建指定的目录
     */
    public static boolean CreateFolder(String folderPath) {
        boolean boo = false;
        try {
            if (IsExisted(folderPath)) {
                return true;
            }
            File file = new File(folderPath);
            boo = file.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boo;
    }

    public static void deleFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }

    public static void deleteFolder(String delpath) {
        try {
            File file = new File(delpath);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "/" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                    } else if (delfile.isDirectory()) {
                        deleteFolder(delpath + "/" + filelist[i]);
                    }
                }

                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除过时文件
     *
     * @param dayValue
     * @param strFilePath
     */
    public static void DelOldFile(int dayValue, String strFilePath) {
        File file = new File(strFilePath);
        if (file.isFile()) {// 文件
            if (canDeleteSDFile(file.lastModified(), dayValue)) {
                file.delete();
            }
        } else if (file.isDirectory()) {// 目录
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {// 循环删除过期文件
                    if (canDeleteSDFile(childFile.lastModified(), dayValue)) {
                        if (childFile.delete()) {
                            Log.i("changchun", "文件删除成功：" + childFile.getName());
                        } else {
                            Log.e("changchun", "文件删除失败：" + childFile.getName());
                        }
                    }
                }
            }
        }
    }

    /**
     * 判断sdcard上的文件是否可以删除
     *
     * @param createDateStr
     * @return
     */
    public static boolean canDeleteSDFile(long time, int days_before) {
        boolean canDel = false;
        Calendar calendar = Calendar.getInstance();
        // 删除x天之前日志信息
        calendar.add(Calendar.DAY_OF_MONTH, -1 * days_before);
        Date expiredDate = calendar.getTime();
        // 获得文件的最新时间
        calendar.setTimeInMillis(time);
        Date lastDate = calendar.getTime();
        // 判断文件的最新时间是否是x天前
        canDel = lastDate.before(expiredDate);
        return canDel;
    }


}
