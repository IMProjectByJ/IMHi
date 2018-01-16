package com.example.star.imhi.Utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by star on 18-1-13.
 */

public class FileUtils {

    private String SDcardRoot;

    public FileUtils(){
        SDcardRoot = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator;

    }
    public File createFileInSDCard( String dir, String filename) // 在根目录下的文件中创建文件夹
    {
        File dirFile = new File(SDcardRoot + dir );
        if (!dirFile.exists())
        {
            dirFile.mkdirs();
            return  dirFile;
        }
        File file = new File(SDcardRoot + dir + File.separator + filename);
        return  file;
    }
    // 判断文件是否存在
    public Boolean isFileExsit(String fileName, String dirPath)
    {
        File file = new File(SDcardRoot + dirPath + File.separator + fileName);
        return file.exists();
    }
}
