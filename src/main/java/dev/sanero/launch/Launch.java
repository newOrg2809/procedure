package dev.sanero.launch;

import dev.sanero.module.ExcelReader;
import dev.sanero.module.FileManagement;
import dev.sanero.module.ScreenInformation;
import dev.sanero.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class Launch {
    private final static String MAPPER_APP_PATH_DIR = "H:\\source checkout\\No03-Source Code\\trunk\\ONTICERP_JW-Pharma\\src\\main\\resources\\mapper\\application\\";
    private final static String MAPPER_COMMON_PATH_DIR = "H:\\source checkout\\No03-Source Code\\trunk\\ONTICERP_JW-Pharma\\src\\main\\resources\\mapper\\common\\";

    public static void main(String[] args) {
        List<ScreenInformation> screenInfoList = ExcelReader.getListScreenInformation();
        ScreenInformation screenInfo = screenInfoList.get(0);
//        for (ScreenInformation screenInfo : screenInfoList) {
           String filePath = "";
           filePath = MAPPER_APP_PATH_DIR + screenInfo.getModule() + File.separator + screenInfo.getMapperFile();
            File file = new File(filePath);
            if(!file.exists()) {
                filePath = MAPPER_COMMON_PATH_DIR + screenInfo.getMapperFile();
                File file1 = new File(filePath);
                if(!file1.exists()) {
                    filePath = "";
                }
            }

            if (StringUtils.isNullOrEmpty(filePath)) {
                System.out.println(screenInfo.getKorName() + " is not have mapper file");
            } else {
                StringBuilder builder = FileManagement.readMethodInFile(filePath, "selectTbLeLotList");
                System.out.println(builder);
            }
//        }
    }
}
