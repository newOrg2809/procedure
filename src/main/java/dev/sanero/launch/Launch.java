package dev.sanero.launch;

import java.io.File;
import java.util.List;

import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;

import dev.sanero.module.ExcelReader;
import dev.sanero.module.FileManagement;
import dev.sanero.module.ScreenInformation;
import dev.sanero.util.StringUtils;

public class Launch {
    private final static String MAPPER_APP_PATH_DIR = "D:\\ontic_pj_documents\\fromSVN\\No03-Source Code\\trunk\\ONTICERP_JW-Pharma\\src\\main\\resources\\mapper\\application\\";
    private final static String MAPPER_COMMON_PATH_DIR = "D:\\ontic_pj_documents\\fromSVN\\No03-Source Code\\trunk\\ONTICERP_JW-Pharma\\src\\main\\resources\\mapper\\common\\";

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
                String formatted = new BasicFormatterImpl().format(builder.toString());
//                System.out.println(builder.toString().replaceAll("[\n]+", "\n").replaceAll("\n[ \t]*OR", " OR"));
                System.out.println(formatted);
            }
//        }
    }
}
