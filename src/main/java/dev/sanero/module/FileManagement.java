package dev.sanero.module;

import dev.sanero.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class FileManagement {
    public static StringBuilder readFile(String filePath) {
        StringBuilder builder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(line -> builder.append(line).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }

    public static StringBuilder readMethodInFile(String filePath, String method) {
        StringBuilder builder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            AtomicBoolean isCorrectMethod = new AtomicBoolean(false);
            AtomicReference<String> methodKey = new AtomicReference<>("");
            HashMap<String, String> mapParam = new HashMap<>();
            AtomicInteger currentIndex = new AtomicInteger(1);

            stream.forEach(line -> {
                if (isCorrectMethod.get()) {
                    if (!StringUtils.isNullOrEmpty(methodKey.get()) && line.contains("/" + methodKey.get())) {
                        isCorrectMethod.set(false);
                        methodKey.set("");
                    } else {
                        if (line.contains("/*")) {
                            line = line.replace("/*", "--").replace("*/", "");
                        }
                        if (line.contains("#{")) {
                            while(line.contains("#{")){
                                String param = line.substring(line.indexOf("#{"), line.indexOf("}")+1);
                                if (!mapParam.containsKey(param)) {
                                    mapParam.put(param, "V_ARG_" + currentIndex.getAndIncrement());
                                }
                                line = line.replace(param,  mapParam.get(param));
                            }
                        }
                        builder.append(line).append("\n");
                    }
                }

                if (line.contains("\"" + method + "\"") && !line.contains("/*")) {
                    methodKey.set(line.trim().substring(1, line.indexOf(" ") - 1));
                    isCorrectMethod.set(true);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }
}
