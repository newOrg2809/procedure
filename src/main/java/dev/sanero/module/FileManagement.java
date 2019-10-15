package dev.sanero.module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.sanero.util.StringUtils;

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
        try {
        	Supplier<Stream<String>> supplier = () -> {
				try {
					return Files.lines(Paths.get(filePath));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			};
            AtomicBoolean isCorrectMethod = new AtomicBoolean(false);
            AtomicReference<String> methodKey = new AtomicReference<>("");
            HashMap<String, String> mapParam = new HashMap<>();
            AtomicInteger currentIndex = new AtomicInteger(1);
            final String[] tags = {""};	// tags[0] = "<choose><when><choose>....."
            AtomicBoolean inBlock = new AtomicBoolean(false);
            AtomicBoolean isFirst = new AtomicBoolean(true);
            Set<String> params = Collections.synchronizedSet(new HashSet<>());

            Stream<String> stream = supplier.get();
            stream.forEach(line -> {
                if (isCorrectMethod.get()) {
                    if (!StringUtils.isNullOrEmpty(methodKey.get()) && line.contains("/" + methodKey.get())) {
                        isCorrectMethod.set(false);
                        methodKey.set("");
                    } else {
                        if (line.contains("/*")) {
                            line = line.replace("/*", "--").replace("*/", "");
                        }
                        if (line.contains("<where>")) {
                        	line = line.replace("<where>", "WHERE 1=1");
                        }
                        if (line.contains("</where>")) {
                        	line = line.replace("</where>", "");
                        }
                        if (line.contains("<![CDATA[")) {
                        	line = line.replace("<![CDATA[", "");
                        	line = line.replace("]]>", "");
                        }
                        if(line.contains("if") || line.contains("choose") || line.contains("when") || line.contains("otherwise")) {
                        	if(line.contains("/")) {
                        		tags[0] = tags[0].substring(0, tags[0].length() - line.trim().length() + 1);
                        		if(tags[0].length() == 0) {
                        			inBlock.set(false);
                        			StringBuilder sb = new StringBuilder();
									params.stream().forEach(s -> sb.append(" AND " + mapParam.get("#{" + s + "}") + " IS NULL "));
                        			sb.append("))");
                        			line = sb.toString();
                        			line = line.replaceFirst("AND", "OR(");
                        			params.clear();
                        		}
                        	} else {
                        		if(tags[0].length() == 0) {
                        			isFirst.set(true);
                        		}
                        		tags[0] += (line.trim().substring(0, line.trim().indexOf(' ') != -1 ? line.trim().indexOf(' ') : line.trim().indexOf('>'))+">");
                        		inBlock.set(true);
                        		if(line.contains("test")) {
                        			params.addAll(extractParam(line));
                        		}
                        		
                        	}
                        }
                        if(line.matches(".*if.*") || line.matches(".*choose.*") || line.matches(".*when.*") || line.matches(".*otherwise.*")) {
                        	line = line.substring(line.indexOf(">")+1);
                        }
                        if (line.contains("#{")) {
                            while(line.contains("#{")){
                                String param = line.substring(line.indexOf("#{"), line.indexOf("}")+1);
                                if (!mapParam.containsKey(param)) {
                                    mapParam.put(param, "V_ARG_" + currentIndex.getAndIncrement());
                                }
                                line = line.replace(param,  mapParam.get(param));
                            }
                            if(line.matches(".*[ \t]+AND[ \t]+.*")){
                            	Matcher matcher = Pattern.compile("[ \t]AND[ \t]+").matcher(line);
                            	if(matcher.find()) {
                                	int indexOfAND = matcher.start()+1;
                                	if(tags[0].length() > 0) {
                                		if(inBlock.get()) {
                                			if(isFirst.get()) {
                                				line = line.substring(0, indexOfAND) + "AND(" + line.substring(indexOfAND + 3);
                                				isFirst.set(false);
                                			} else {
                                				line = line.substring(0, indexOfAND) + "OR" + line.substring(indexOfAND + 3);
                                			}
                                			
                                		}
									}
								}
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
            
//            stream = supplier.get();
//            stream.forEach(line -> {
//                if (isCorrectMethod.get()) {
//                    if (!StringUtils.isNullOrEmpty(methodKey.get()) && line.contains("/" + methodKey.get())) {
//                        isCorrectMethod.set(false);
//                        methodKey.set("");
//                    } else {
//                        convertCondition(line, mapParam);
//                    }
//                }
//
//                if (line.contains("\"" + method + "\"") && !line.contains("/*")) {
//                    methodKey.set(line.trim().substring(1, line.indexOf(" ") - 1));
//                    isCorrectMethod.set(true);
//                }
//            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder;
    }
    
    public static String convertCondition(String line, HashMap<String, String> mapParam) {
    	String pattern1 = ".*";
    	String pattern2 = ".*";
    	String initialParamName = "";
    	for(String initialParamExpression : mapParam.keySet()) {
    		initialParamName = initialParamExpression.substring(2, initialParamExpression.length() - 1);
    		pattern1 = ".*" + initialParamName + "[ ]+!=[ ]+null.*";
    		pattern2 = ".*" + initialParamName + "[ ]+!=[ ]+''.*";
    		if(line.matches(pattern1) && line.matches(pattern2)) {
    			System.out.println(line);
    		}
    	}
    	return "";
    }
    
    public static Set<String> extractParam(String line) {
    	String insideTest = line.substring(line.indexOf('\"')+1, line.lastIndexOf('\"'));
    	String tmp = insideTest.replaceAll("null", " ");
    	tmp = tmp.replaceAll("!=", " ");
    	tmp = tmp.replaceAll("''", " ");
    	tmp = tmp.replaceAll("[ ]+", " ");
    	tmp = tmp.replaceAll("[\t]+", " ");
    	tmp = tmp.replaceAll("and", " ");
    	tmp = tmp.replaceAll("or", " ");
    	String[] params = tmp.split(" ");
    	List<String> duplicated = Arrays.asList(params);
    	return duplicated.stream().filter(s -> s.trim().length() > 0).collect(Collectors.toSet());
    }
}
