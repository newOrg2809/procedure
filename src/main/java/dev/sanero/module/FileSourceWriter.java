package dev.sanero.module;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileSourceWriter {
    private boolean overrideFile = false;

    public void writeSource(String filePath, Object fileContent) {
        try {
            String strContent = new String(fileContent.toString().getBytes("UTF-8"), "Cp1252");
            System.out.println(strContent);
            File file = new File(filePath);
            File folder = new File(file.getParent());
            if (!folder.exists()) {
                folder.mkdirs();
            }

            if (file.exists() && !overrideFile) {
                File bkfile = new File(file.getPath() + ".bk" + date2HHMMssNoSlash(new Date()));
                file.renameTo(bkfile);
            }

            FileOutputStream sfile = new FileOutputStream(file);
            sfile.write(strContent.getBytes());
            sfile.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static String date2HHMMssNoSlash(Date value) {
        if (value != null) {
            SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("YYYYMMDD HHmmss");
            return dateTimeNoSlash.format(value);
        }
        return "";
    }

    public boolean isOverrideFile() {
        return overrideFile;
    }

    public void setOverrideFile(boolean overrideFile) {
        this.overrideFile = overrideFile;
    }
}
